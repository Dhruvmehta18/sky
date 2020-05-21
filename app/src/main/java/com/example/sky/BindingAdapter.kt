package com.example.sky

import android.util.Log
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText

private fun setError(view: TextInputEditText, id: Int) {
    Log.i("Binding", view.resources.getString(id))
    view.error = view.resources.getString(id)
}

@BindingAdapter("passwordError")
fun passwordError(view: TextInputEditText, text: String?) {
    if (text.isNullOrBlank() || isPasswordValid(text)) {
        view.error = null
    } else {
        setError(view, R.string.invalid_password)
    }
}

@BindingAdapter("confirmPasswordError", "android:text")
fun confirmPasswordError(view: TextInputEditText, password: String?, confirmPassword: String?) {
    if (password.isNullOrBlank() || confirmPassword.isNullOrBlank() || isConfirmPasswordValid(
            password,
            confirmPassword
        )
    ) {
        view.error = null
    } else {
        setError(view, R.string.invalid_confirm_password)
    }
}

@BindingAdapter("onlyTextError")
fun onlyTextError(view: TextInputEditText, text: String?) {
    Log.i("BindingAdapter", text)
    if (text.isNullOrBlank() || isValidOnlyText(text)) {
        view.error = null
    } else {
        setError(view, R.string.only_letters)
    }
}

@BindingAdapter("emailError")
fun emailError(view: TextInputEditText, email: String?) {
    if (email.isNullOrBlank() || isValidEmail(email)) {
        view.error = null
    } else {
        setError(view, R.string.invalid_email)
    }
}
