package ru.trinitydigital.pagingcashe.utils

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatToggleButton
import androidx.core.animation.doOnEnd

class AnimatedToggleButton : AppCompatToggleButton {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    init {
        text = ""
        textOn = ""
        textOff = ""
    }

    private fun animateClick() {
        this.isClickable = false
        val scale = setScaleAnimation()
        val scaleDown: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            this,
            PropertyValuesHolder.ofFloat("scaleX", scale),
            PropertyValuesHolder.ofFloat("scaleY", scale)
        )
        scaleDown.duration = 200
        scaleDown.repeatCount = 1
        scaleDown.repeatMode = ObjectAnimator.REVERSE
        scaleDown.start()

        scaleDown.doOnEnd {
            this.isClickable = true
        }
    }

    private fun setScaleAnimation(): Float {
        return if (isChecked) 1.4f else 0.6f
    }

    fun customClickListener(onSafeCLick: () -> Unit) {
        this.setOnDelayedClickListenerToggleButton {
            animateClick()
            onSafeCLick.invoke()
        }
    }
}