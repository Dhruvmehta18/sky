package com.example.sky

import android.util.Log
import android.util.Patterns

fun isValidOnlyText(fullName: String): Boolean {
    Log.i("Util", fullName.toString())
    return fullName.length > 2
}

// A placeholder username validation check

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isPasswordValid(password: String): Boolean {
    return password.length > 8
}

fun isConfirmPasswordValid(password: String, confirmPassword: String): Boolean {
    return password == confirmPassword
}