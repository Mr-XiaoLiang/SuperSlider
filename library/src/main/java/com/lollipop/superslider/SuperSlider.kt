package com.lollipop.superslider

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.abs

class SuperSlider @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null
) : AppCompatImageView(context, attributeSet) {

    private val touchHelper by lazy {
        TouchHelper(
            this,
            onProgressChanged = ::onTouchMoved,
            clickCallback = ::onPositionClick
        )
    }

    /**
     * 背景层
     */
    var backgroundLayer: BackgroundLayer? = null

    /**
     * 前景层
     */
    var foregroundLayer: ForegroundLayer? = null

    /**
     * 激活的进度层
     */
    var activeLayer: ActiveLayer? = null

    /**
     * 手指滑块层
     */
    var thumbLayer: ThumbLayer? = null

    /**
     * 触摸控制的回调函数
     */
    var progressChangeListener: OnProgressChangeListener? = null

    /**
     * 触摸拖拽控制器
     */
    var touchController: TouchController?
        get() {
            return touchHelper.controller
        }
        set(value) {
            touchHelper.controller = value
        }

    /**
     * 打开的情况下，会在计算手指位置的时候
     * 规避Padding
     */
    var touchFixPadding = true
        set(value) {
            field = value
            updateTouchEdge()
        }

    var currentProgress: Float = 0F
        private set

    private fun onPositionClick(progress: Float) {
        callOnClick()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val layerLeft = paddingLeft
        val layerTop = paddingTop
        val layerRight = width - paddingRight
        val layerBottom = height - paddingBottom
        backgroundLayer?.onSizeChanged(layerLeft, layerTop, layerRight, layerBottom)
        foregroundLayer?.onSizeChanged(layerLeft, layerTop, layerRight, layerBottom)
        activeLayer?.onSizeChanged(layerLeft, layerTop, layerRight, layerBottom)
        thumbLayer?.onSizeChanged(layerLeft, layerTop, layerRight, layerBottom)
        updateTouchEdge()
    }

    fun updateProgress(progress: Float) {
        onProgressChanged(progress, false)
    }

    private fun onTouchMoved(progress: Float) {
        onProgressChanged(progress, true)
    }

    private fun onProgressChanged(progress: Float, fromUser: Boolean) {
        currentProgress = progress
        progressChangeListener?.onProgressChanged(progress, fromUser)
        invalidate()
    }

    private fun updateTouchEdge() {
        val padding = touchFixPadding
        touchHelper.touchStartEdge = if (padding) {
            paddingLeft
        } else {
            0
        }
        touchHelper.touchEndEdge = if (padding) {
            width - paddingRight
        } else {
            width
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        backgroundLayer?.drawBackground(canvas, currentProgress)
        activeLayer?.drawActive(canvas, currentProgress)
        thumbLayer?.drawThumb(canvas, currentProgress)
        foregroundLayer?.drawForeground(canvas, currentProgress)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return super.onTouchEvent(event)
        return touchHelper.onTouchEvent(event)
    }

    class TouchHelper(
        private val view: SuperSlider,
        private val onProgressChanged: (progress: Float) -> Unit,
        private val clickCallback: (progress: Float) -> Unit
    ) {

        var touchId = -1
            private set
        var touchPosition = 0F
            private set
        var touchX = 0F
            private set
        var controller: TouchController? = null

        var touchStartEdge = 0
        var touchEndEdge = 0

        val touchSlop = ViewConfiguration.get(view.context).scaledTouchSlop

        private var isClickable = false

        private fun log(message: String) {
            Log.d("SuperSlider.TOUCH", message)
        }

        fun onTouchEvent(event: MotionEvent): Boolean {
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    touchId = event.getPointerId(0)
                    touchX = getTouchX(event)
                    isClickable = true
                    touchPosition = getTouchPosition(touchX)
                    controller?.beforeTouch(view.currentProgress)
                    val result = controller?.onTouchDown(touchPosition) ?: false
                    if (result) {
                        dispatchProgress()
                    }
                    log("Down, $touchX, $touchPosition, $result")
                    return result
                }

                MotionEvent.ACTION_MOVE -> {
                    val x = getTouchX(event)
                    if (isClickable && abs(x - touchX) > touchSlop) {
                        isClickable = false
                    }
                    touchPosition = getTouchPosition(x)
                    val result = controller?.onTouchMove(touchPosition) ?: false
                    if (result) {
                        dispatchProgress()
                    }
                    log("Move, $touchX, $touchPosition, $result")
                    return result
                }

                MotionEvent.ACTION_UP -> {
                    val result = controller?.onTouchUp(touchPosition) ?: false
                    if (result) {
                        dispatchProgress()
                    }
                    if (isClickable) {
                        val clickResult = controller?.onClick(touchPosition) ?: false
                        if (!clickResult) {
                            dispatchClick()
                        }
                    }
                    log("Up, $touchX, $touchPosition, $result")
                    return result
                }

                MotionEvent.ACTION_CANCEL -> {
                    val result = controller?.onTouchUp(touchPosition) ?: false
                    if (result) {
                        dispatchProgress()
                    }
                    log("Cancel, $touchX, $touchPosition, $result")
                    return result
                }

                MotionEvent.ACTION_POINTER_UP -> {
                    // 抬起手指之后，获取新的触摸点
                    val pointerIndex = event.actionIndex
                    val pointerId = event.getPointerId(pointerIndex)
                    if (pointerId == touchId) {
                        val newPointerIndex = if (pointerIndex == 0) 1 else 0
                        touchId = event.getPointerId(newPointerIndex)
                        touchX = event.getX(newPointerIndex)
                        isClickable = false
                        touchPosition = getTouchPosition(touchX)
                    }
                    log("Pointer Up, $touchX, $touchPosition")
                }
            }
            return true
        }

        private fun dispatchProgress() {
            val progress = controller?.transform(touchPosition) ?: touchPosition
            onProgressChanged(progress)
        }

        private fun dispatchClick() {
            val progress = controller?.transform(touchPosition) ?: touchPosition
            clickCallback(progress)
        }

        private fun getTouchX(event: MotionEvent): Float {
            val pointerIndex = event.findPointerIndex(touchId)
            if (pointerIndex < 0) {
                val pointerId = event.getPointerId(0)
                touchId = pointerId
                return event.getX(0)
            } else {
                return event.getX(pointerIndex)
            }
        }

        private fun getTouchPosition(x: Float): Float {
            return (x - touchStartEdge) / (touchEndEdge - touchStartEdge)
        }

    }

    interface Layer {
        fun onSizeChanged(left: Int, top: Int, right: Int, bottom: Int)
    }

    interface BackgroundLayer : Layer {
        fun drawBackground(canvas: Canvas, progress: Float)
    }

    interface ForegroundLayer : Layer {
        fun drawForeground(canvas: Canvas, progress: Float)
    }

    interface ActiveLayer : Layer {
        fun drawActive(canvas: Canvas, progress: Float)
    }

    interface ThumbLayer : Layer {
        fun drawThumb(canvas: Canvas, progress: Float)
    }

    interface TouchController {

        fun beforeTouch(progress: Float) {}

        fun onTouchDown(position: Float): Boolean {
            return true
        }

        fun onTouchMove(position: Float): Boolean {
            return true
        }

        fun onTouchUp(position: Float): Boolean {
            return true
        }

        fun onClick(position: Float): Boolean {
            return false
        }

        fun transform(progress: Float): Float {
            if (progress > 1F) {
                return 1F
            }
            if (progress < 0F) {
                return 0F
            }
            return progress
        }

    }

    fun interface OnProgressChangeListener {
        fun onProgressChanged(progress: Float, fromUser: Boolean)
    }

}