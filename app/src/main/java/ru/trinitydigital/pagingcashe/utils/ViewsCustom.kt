package ru.trinitydigital.pagingcashe.utils

import android.content.Context
import android.util.AttributeSet
import android.view.ViewOutlineProvider
import androidx.constraintlayout.widget.ConstraintLayout

class ViewsCustom {
}

class RoundedConstraint (context : Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {

    init {
        outlineProvider = ViewOutlineProvider.BACKGROUND
        clipToOutline = true
    }
}