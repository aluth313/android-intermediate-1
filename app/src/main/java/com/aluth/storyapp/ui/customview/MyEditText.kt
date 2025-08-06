package com.aluth.storyapp.ui.customview

import android.content.Context
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
import androidx.core.content.withStyledAttributes

class MyEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var inputMode = 0
    private var customHint: String? = null
    private var showPasswordToggle = false
    private var isPasswordVisible = false

    init {
        isSingleLine = true
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        imeOptions = EditorInfo.IME_ACTION_DONE

        if (attrs != null) {
            context.withStyledAttributes(attrs, R.styleable.MyEditText) {
                inputMode = getInt(R.styleable.MyEditText_inputMode, 0)
                customHint = getString(R.styleable.MyEditText_customHint)
                showPasswordToggle = getBoolean(R.styleable.MyEditText_showPasswordToggle, false)
            }
        }

        applyInputMode()

        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (inputMode == 1) {
                    val email = s.toString()
                    if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        error = context.getString(R.string.format_email_tidak_valid)
                    }
                }
                if (inputMode == 2) {
                    if (s.toString().length < 8) {
                        error = context.getString(R.string.password_kurang_dari_8_karakter)
                    }
                }
            }
        })
    }

    private fun applyInputMode() {
        inputType = when (inputMode) {
            1 -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            2 -> if (isPasswordVisible)
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            else -> InputType.TYPE_CLASS_TEXT
        }

        hint = customHint ?: when (inputMode) {
            1 -> context.getString(R.string.masukkan_email_anda)
            2 -> context.getString(R.string.masukan_password_anda)
            else -> ""
        }

        if (inputMode == 2 && showPasswordToggle) {
            updatePasswordToggleIcon()
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }
    }

    private fun updatePasswordToggleIcon() {
        val icon = ContextCompat.getDrawable(
            context, if (isPasswordVisible)
                R.drawable.baseline_visibility_off_24
            else
                R.drawable.baseline_visibility_24
        )
        setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null)
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
        isPasswordVisible = !isPasswordVisible
        applyInputMode()
        updatePasswordToggleIcon()
        setSelection(selection)
    }
}