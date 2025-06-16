package com.aluth.storyapp.view.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.aluth.storyapp.R

class MyEditTextPassword @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private val visibleDrawable: Drawable
    private val invisibleDrawable: Drawable
    private var isPasswordVisible = false

    init {
        visibleDrawable = ContextCompat.getDrawable(context, R.drawable.baseline_visibility_24)!!
        invisibleDrawable = ContextCompat.getDrawable(context, R.drawable.baseline_visibility_off_24)!!

        setOnTouchListener(this)
        isSingleLine = true
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        imeOptions = EditorInfo.IME_ACTION_DONE

        showInvisibleButton()

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                error = if (password.length < 8) context.getString(R.string.password_kurang_dari_8_karakter) else null
            }

        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.masukan_password_anda)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event == null || compoundDrawables[2] == null) return false

        val drawableEnd = compoundDrawables[2]
        val drawableWidth = drawableEnd.intrinsicWidth

        val touchX = event.x
        val isRTL = layoutDirection == View.LAYOUT_DIRECTION_RTL

        val drawableClicked = if (isRTL) {
            touchX < paddingStart + drawableWidth
        } else {
            touchX > width - paddingEnd - drawableWidth
        }

        if (drawableClicked && event.action == MotionEvent.ACTION_UP) {
            togglePasswordVisibility()
            return true
        }

        return false
    }

    private fun togglePasswordVisibility() {
        val selection = selectionEnd

        if (isPasswordVisible) {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            showInvisibleButton()
        } else {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            showVisibleButton()
        }

        isPasswordVisible = !isPasswordVisible
        setSelection(selection)
    }

    private fun showVisibleButton() {
        setButtonDrawables(endOfTheText = visibleDrawable)
    }

    private fun showInvisibleButton() {
        setButtonDrawables(endOfTheText = invisibleDrawable)
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }
}
