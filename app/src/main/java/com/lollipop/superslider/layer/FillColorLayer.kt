package com.lollipop.superslider.layer

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.lollipop.superslider.SuperSlider
import com.lollipop.superslider.basic.LayerHelper

class FillColorLayer : SuperSlider.BackgroundLayer, SuperSlider.ForegroundLayer,
    SuperSlider.ActiveLayer {

    private val paint by lazy {
        Paint().apply {
            isDither = true
            isAntiAlias = true
        }
    }

    private val layerHelper = LayerHelper()

    var fillColor: Int
        get() {
            return paint.color
        }
        set(value) {
            paint.color = value
        }

    val padding: RectF
        get() {
            return layerHelper.padding
        }

    var isReversal = false

    var radius: Float = 0F

    override fun onSizeChanged(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        layerHelper.onSizeChanged(left, top, right, bottom)
    }

    override fun drawBackground(canvas: Canvas, progress: Float) {
        canvas.drawRoundRect(
            layerHelper.getFillBounds(),
            radius,
            radius,
            paint
        )
    }

    override fun drawForeground(canvas: Canvas, progress: Float) {
        canvas.drawRoundRect(
            layerHelper.getFillBounds(),
            radius,
            radius,
            paint
        )
    }

    override fun drawActive(canvas: Canvas, progress: Float) {
        canvas.drawRoundRect(
            layerHelper.getActiveBounds(progress = progress, reversal = isReversal),
            radius,
            radius,
            paint
        )
    }
}