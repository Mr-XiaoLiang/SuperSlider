package com.lollipop.superslider.layer

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.lollipop.superslider.SuperSlider

class FillColorLayer : SuperSlider.BackgroundLayer, SuperSlider.ForegroundLayer,
    SuperSlider.ActiveLayer {

    private val paint by lazy {
        Paint().apply {
            isDither = true
            isAntiAlias = true
        }
    }

    var fillColor: Int
        get() {
            return paint.color
        }
        set(value) {
            paint.color = value
        }

    var radius: Float = 0F

    private val bounds = RectF()
    private val activeBounds = RectF()

    override fun onSizeChanged(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        bounds.set(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
    }

    override fun drawBackground(canvas: Canvas, progress: Float) {
        canvas.drawRoundRect(bounds, radius, radius, paint)
    }

    override fun drawForeground(canvas: Canvas, progress: Float) {
        canvas.drawRoundRect(bounds, radius, radius, paint)
    }

    override fun drawActive(canvas: Canvas, progress: Float) {
        activeBounds.set(
            bounds.left,
            bounds.top,
            bounds.left + progress * bounds.width(),
            bounds.bottom
        )
        canvas.drawRoundRect(activeBounds, radius, radius, paint)
    }
}