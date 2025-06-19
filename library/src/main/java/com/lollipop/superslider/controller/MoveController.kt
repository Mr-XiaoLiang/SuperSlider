package com.lollipop.superslider.controller

import com.lollipop.superslider.SuperSlider

class MoveController : SuperSlider.TouchController {

    private var currentProgress = 0F
    private var progressOffset = 0F

    override fun beforeTouch(progress: Float) {
        currentProgress = progress
    }

    override fun onTouchDown(position: Float): Boolean {
        progressOffset = currentProgress - position
        return true
    }

    override fun transform(progress: Float): Float {
        var p = progress + progressOffset
        if (p < 0) {
            p = 0F
        }
        if (p > 1) {
            p = 1F
        }
        return p
    }
}