package ru.trinitydigital.pagingcashe.utils

import android.app.Activity
import android.content.Context
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.InputMethodManager


fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Activity.hideKeyboard() {
    val view = if (currentFocus == null) View(this) else currentFocus
    view?.hideKeyboard()
}

fun AnimatedToggleButton.setOnDelayedClickListenerToggleButton(
    listener: (view: View) -> Unit
) {
    isClickable = true
    isFocusableInTouchMode = false
    val safeClickListener = SafeClickListenerToggleButton(this) {
        listener(it)
        it.hideKeyboard()
    }
    setOnClickListener(safeClickListener)
}

class SafeClickListenerToggleButton(
    private val view: AnimatedToggleButton,
    private var defaultInterval: Int = 700,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            view.isChecked = !view.isChecked
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}
