package com.lollipop.superslider.layer

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.graphics.withTranslation
import com.lollipop.superslider.SuperSlider
import com.lollipop.superslider.basic.LayerHelper
import kotlin.math.min

class DrawableThumbLayer(
    val drawable: Drawable,
    val thumbWidth: Float,
    val thumbHeight: Float
) : SuperSlider.ThumbLayer {

    private val helper = LayerHelper()
    private var drawWidth = thumbWidth
    private var drawHeight = thumbHeight

    override fun drawThumb(canvas: Canvas, progress: Float) {
        val thumbBounds = helper.getThumbBounds(
            progress = progress,
            thumbWidth = drawWidth,
            thumbHeight = drawHeight,
            fixBounds = true
        )
        val left = thumbBounds.left
        val top = thumbBounds.top
        canvas.withTranslation(left, top) {
            drawable.draw(canvas)
        }
    }

    override fun onSizeChanged(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        helper.onSizeChanged(left, top, right, bottom)
        val thumbBounds = helper.getThumbBounds(
            progress = 0F,
            thumbWidth = thumbWidth,
            thumbHeight = thumbHeight,
            fixBounds = true
        )
        // 缩放的时候保持比例
        val widthWeight = thumbBounds.width() / thumbWidth
        val heightWeight = thumbBounds.height() / thumbHeight
        val weight = min(widthWeight, heightWeight)
        drawWidth = thumbWidth * weight
        drawHeight = thumbHeight * weight
        drawable.setBounds(0, 0, drawWidth.toInt(), drawHeight.toInt())
    }


}