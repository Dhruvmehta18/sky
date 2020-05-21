package com.example.sky.ui.signup

data class SignUpFormState(
    val fullNameError: Int? = null,
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val confirmPasswordError: Int? = null,
    val isDataValid: Boolean = false
)