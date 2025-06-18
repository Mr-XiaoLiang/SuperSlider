package com.lollipop.superslider.basic

import android.graphics.RectF

class LayerHelper {

    private val viewBounds = RectF()
    private val outBounds = RectF()
    val padding = RectF()

    fun onSizeChanged(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        viewBounds.set(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
    }

    fun setPadding(
        paddingLeft: Int,
        paddingTop: Int,
        paddingRight: Int,
        paddingBottom: Int
    ): LayerHelper {
        return setPadding(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            paddingRight.toFloat(),
            paddingBottom.toFloat()
        )
    }

    fun setPadding(
        paddingLeft: Float,
        paddingTop: Float,
        paddingRight: Float,
        paddingBottom: Float
    ): LayerHelper {
        padding.set(paddingLeft, paddingTop, paddingRight, paddingBottom)
        return this
    }

    fun setPadding(padding: RectF): LayerHelper {
        padding.set(padding)
        return this
    }

    fun clearPadding(): LayerHelper {
        return setPadding(0F, 0F, 0F, 0F)
    }

    fun getFillBounds(): RectF {
        outBounds.set(
            viewBounds.left + padding.left,
            viewBounds.top + padding.top,
            viewBounds.right - padding.right,
            viewBounds.bottom - padding.bottom
        )
        return outBounds
    }

    fun getActiveBounds(progress: Float, reversal: Boolean = false): RectF {
        val p = if (reversal) {
            1F - progress
        } else {
            progress
        }
        val left = viewBounds.left + padding.left
        val right = (viewBounds.width() * p) + left - padding.right
        outBounds.set(
            left,
            viewBounds.top + padding.top,
            right,
            viewBounds.bottom - padding.bottom
        )
        return outBounds
    }

    fun getInactiveBounds(progress: Float, reversal: Boolean = false): RectF {
        val p = if (reversal) {
            1F - progress
        } else {
            progress
        }
        val length = viewBounds.width() * p
        val left = viewBounds.left + length + padding.left
        val right = viewBounds.right - padding.right
        outBounds.set(
            left,
            viewBounds.top + padding.top,
            right,
            viewBounds.bottom - padding.bottom
        )
        return outBounds
    }

    fun getThumbBounds(
        progress: Float,
        thumbWidth: Float,
        thumbHeight: Float,
        reversal: Boolean = false,
        fixBounds: Boolean = true
    ): RectF {
        val p = if (reversal) {
            1F - progress
        } else {
            progress
        }
        val centerX = (viewBounds.width() * p) + viewBounds.left
        val centerY = viewBounds.centerY()
        val thumbRadiusX = thumbWidth / 2
        val thumbRadiusY = thumbHeight / 2
        outBounds.set(
            centerX - thumbRadiusX,
            centerY - thumbRadiusY,
            centerX + thumbRadiusX,
            centerY + thumbRadiusY
        )
        if (fixBounds) {
            fixThumbBounds(outBounds)
        }
        return outBounds
    }

    fun fixThumbBounds(out: RectF) {
        if (out.height() > viewBounds.height()) {
            // 过高就整体重置
            out.top = viewBounds.top
            out.bottom = viewBounds.bottom
        } else {
            // 位置越界，就只是偏移，而不是压缩尺寸
            if (out.top < viewBounds.top) {
                out.offset(0F, viewBounds.top - out.top)
            } else if (out.bottom > viewBounds.bottom) {
                out.offset(0F, viewBounds.bottom - out.bottom)
            }
        }
        if (out.width() > viewBounds.width()) {
            // 宽度过高就整体重置
            out.left = viewBounds.left
            out.right = viewBounds.right
        } else {
            // 宽度越界，就只是偏移，而不是压缩尺寸
            if (out.left < viewBounds.left) {
                out.offset(viewBounds.left - out.left, 0F)
            } else if (out.right > viewBounds.right) {
                out.offset(viewBounds.right - out.right, 0F)
            }
        }
    }

}