package com.example.storyapp.CustomView

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class CustomViewPassword : AppCompatEditText, View.OnTouchListener {
    var userPasswordIsValid: Boolean = false
    private lateinit var passwordIcon: Drawable


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        passwordIcon = ContextCompat.getDrawable(context, R.drawable.ic_password) as Drawable
        transformationMethod = PasswordTransformationMethod.getInstance()
        onShowVisibilityIcon(passwordIcon)
    }

    private fun onShowVisibilityIcon(icon: Drawable) {
        setButtonDrawables(startOfDrawable = icon)
    }

    private fun setButtonDrawables(
        startOfDrawable: Drawable? = null,
        topOfDrawable: Drawable? = null,
        endOfDrawable: Drawable? = null,
        bottomOfDrawable: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfDrawable,
            topOfDrawable,
            endOfDrawable,
            bottomOfDrawable
        )

    }

    private fun checkPass() {
        val password = text?.trim()
        when {
            password.isNullOrEmpty() -> {
                userPasswordIsValid = false
                error = resources.getString(R.string.password_is_empty)
            }
            password.length < 6 -> {
                userPasswordIsValid = false
                error = resources.getString(R.string.password_length)
            }
            else -> {
                userPasswordIsValid = true
            }
        }
    }

    override fun onFocusChanged(focuse: Boolean, directionFocus: Int, previousFocus: Rect?) {
        super.onFocusChanged(focuse, directionFocus, previousFocus)
        if (!focuse) checkPass()
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return false
    }
}