package com.lollipop.superslider.controller

import com.lollipop.superslider.SuperSlider
import kotlin.math.abs

class HoldController(
    val touchRadius: Float = 0.05F
) : SuperSlider.TouchController {

    private var touchPosition = 0F

    override fun beforeTouch(progress: Float) {
        touchPosition = progress
    }

    override fun onTouchDown(position: Float): Boolean {
        val offset = abs(touchPosition - position)
        if (offset > touchRadius) {
            return false
        }
        return true
    }

}