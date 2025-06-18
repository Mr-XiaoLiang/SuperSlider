package com.lollipop.superslider.layer

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.lollipop.superslider.SuperSlider
import com.lollipop.superslider.basic.LayerHelper

class OvalThumbLayer : SuperSlider.ThumbLayer {

    private val helper = LayerHelper()
    private val paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        isDither = true
    }
    var color: Int
        get() {
            return paint.color
        }
        set(value) {
            paint.color = value
        }

    var radius: Float = 0F

    val padding: RectF
        get() {
            return helper.padding
        }

    override fun drawThumb(canvas: Canvas, progress: Float) {
        if (radius < 1F) {
            return
        }
        val bounds = helper.getThumbBounds(
            progress = progress,
            thumbWidth = radius * 2F,
            thumbHeight = radius * 2F,
            reversal = false,
            fixBounds = true
        )
        canvas.drawOval(bounds, paint)
    }

    override fun onSizeChanged(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        helper.onSizeChanged(left, top, right, bottom)
    }
}