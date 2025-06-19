package com.lollipop.demo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lollipop.demo.databinding.ActivityMainBinding
import com.lollipop.superslider.SuperSlider
import com.lollipop.superslider.controller.HoldController
import com.lollipop.superslider.controller.MoveController
import com.lollipop.superslider.controller.SimpleController
import com.lollipop.superslider.layer.DrawableThumbLayer
import com.lollipop.superslider.layer.FillColorLayer
import com.lollipop.superslider.layer.OvalThumbLayer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initSlider(binding)
    }

    @SuppressLint("SetTextI18n")
    private fun initSlider(binding: ActivityMainBinding) {
        val dp20 = dp(20F)
        val dp10 = dp(10F)
        val dp15 = dp(15F)

        binding.slider1.backgroundLayer = FillColorLayer().apply {
            fillColor = 0x55F44336.toInt()
            radius = dp20
        }
        binding.slider1.activeLayer = FillColorLayer().apply {
            fillColor = 0xFFFF0000.toInt()
            radius = dp20
            padding.top = dp10
            padding.bottom = dp10
        }
        binding.slider1.thumbLayer = OvalThumbLayer().apply {
            color = 0xFFFFFFFF.toInt()
            radius = dp20
        }
        binding.slider1.touchController = SimpleController()
        binding.slider1.progressChangeListener =
            SuperSlider.OnProgressChangeListener { value, user ->
                binding.valueView1.text = "进度：${value * 100}"
            }

        binding.slider2.backgroundLayer = FillColorLayer().apply {
            fillColor = 0x55F44336.toInt()
            radius = dp20
        }
        binding.slider2.activeLayer = FillColorLayer().apply {
            fillColor = 0xFFFF0000.toInt()
            radius = dp20
        }
        binding.slider2.thumbLayer = OvalThumbLayer().apply {
            color = 0xFFFFFFFF.toInt()
            radius = dp20
        }
        binding.slider2.touchController = MoveController()
        binding.slider2.progressChangeListener =
            SuperSlider.OnProgressChangeListener { value, user ->
                binding.valueView2.text = "进度：${value * 100}"
            }

        binding.slider3.backgroundLayer = FillColorLayer().apply {
            fillColor = 0x55F44336.toInt()
            radius = dp20
            padding.top = dp15
            padding.bottom = dp15
        }
        binding.slider3.activeLayer = FillColorLayer().apply {
            fillColor = 0xFFFF0000.toInt()
            radius = dp20
        }
        binding.slider3.thumbLayer = OvalThumbLayer().apply {
            color = 0xFFFFFFFF.toInt()
            radius = dp20
        }
        binding.slider3.touchController = HoldController()
        binding.slider3.progressChangeListener =
            SuperSlider.OnProgressChangeListener { value, user ->
                binding.valueView3.text = "进度：${value * 100}"
            }

        binding.slider4.backgroundLayer = FillColorLayer().apply {
            fillColor = 0x55F44336.toInt()
            radius = dp20
            padding.top = dp(35F)
            padding.left = dp20
            padding.right = dp20
        }
        binding.slider4.activeLayer = FillColorLayer().apply {
            fillColor = 0xFFFF0000.toInt()
            radius = dp20
        }
        binding.slider4.thumbLayer = OvalThumbLayer().apply {
            color = 0xFFFFFFFF.toInt()
            radius = dp20
        }
        binding.slider4.touchController = MoveController()
        binding.slider4.progressChangeListener =
            SuperSlider.OnProgressChangeListener { value, user ->
                binding.valueView4.text = "进度：${value * 100}"
            }

        binding.slider5.backgroundLayer = FillColorLayer().apply {
            fillColor = 0x55F44336.toInt()
            radius = dp20
        }
        binding.slider5.activeLayer = FillColorLayer().apply {
            fillColor = 0xFFFF0000.toInt()
            radius = dp10
        }
        binding.slider5.thumbLayer = DrawableThumbLayer(
            ContextCompat.getDrawable(
                this,
                com.lollipop.superslider.R.drawable.cat
            )!!,
            dp20,
            dp20
        )
        binding.slider5.touchController = MoveController()
        binding.slider5.progressChangeListener =
            SuperSlider.OnProgressChangeListener { value, user ->
                binding.valueView5.text = "进度：${value * 100}"
            }
    }

    private fun dp(value: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            resources.displayMetrics
        )
    }

}