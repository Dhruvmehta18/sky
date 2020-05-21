package com.example.sky.ui.signup

import android.util.Log
import android.util.Patterns
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sky.R
import com.example.sky.data.LoginRepository
import com.example.sky.data.Result
import com.example.sky.ui.login.LoggedInUserView
import com.example.sky.ui.login.LoginResult

class SignUpViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _fullName = MutableLiveData<String>()
    val fullName: LiveData<String>
        get() = _fullName
    private val _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email
    private val _password = MutableLiveData<String>()
    val password: LiveData<String>
        get() = _password
    private val _confirmPassword = MutableLiveData<String>()
    val confirmPassword: LiveData<String>
        get() = _confirmPassword

    private val signUpForm = MutableLiveData<SignUpFormState>()
    val signUpFormState: LiveData<SignUpFormState>
        get() = signUpForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult>
        get() = _loginResult

    init {
        _fullName.value = ""
        _email.value = ""
        _password.value = ""
        _confirmPassword.value = ""
        signUpForm.value = SignUpFormState()
    }

    fun signUp(fullName: String, email: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.signUp(fullName, email, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(
                    success = LoggedInUserView(
                        displayName = result.data.displayName,
                        email = result.data.email
                    )
                )
        } else {
            _loginResult.value = LoginResult(error = R.string.sign_up_failed)
        }
    }

    fun fullNameDataChanged(fullName: String) {
        if (!isValidFullName(fullName)) {
            signUpForm.value = SignUpFormState(fullNameError = R.string.invalid_fullName)
        } else {
            signUpDataChanged()
        }

        Log.i("SignUpFragment", fullName)
    }

    fun emailDataChanged(email: String) {
        if (!isValidEmail(email)) {
            signUpForm.value = SignUpFormState(emailError = R.string.invalid_email)
        } else {
            signUpDataChanged()
        }
    }

    fun passwordDataChanged(password: String) {
        if (!isPasswordValid(password)) {
            signUpForm.value = SignUpFormState(passwordError = R.string.invalid_password)
        } else {
            signUpDataChanged()
        }
    }

    fun confirmPasswordChanged(confirmPassword: String) {
        if (!isConfirmPasswordValid(password.value, confirmPassword)) {
            signUpForm.value =
                SignUpFormState(confirmPasswordError = R.string.invalid_confirm_password)
        } else {
            signUpDataChanged()
        }
    }

    private fun signUpDataChanged() {
        if (isValidFullName(fullName.value) &&
            isValidEmail(email.value) &&
            isPasswordValid(password.value) &&
            isConfirmPasswordValid(password.value, confirmPassword.value)
        )
            signUpForm.value = SignUpFormState(isDataValid = true)
    }

    private fun isValidFullName(fullName: String?): Boolean {
        return fullName != null && !fullName.isBlank()
    }

    // A placeholder username validation check
    private fun isValidEmail(email: String?): Boolean {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String?): Boolean {
        return password != null && password.length > 8
    }

    private fun isConfirmPasswordValid(password: String?, confirmPassword: String?): Boolean {
        return password == confirmPassword
    }

    inner class SignUpFormModel : BaseObservable() {
        @get:Bindable
        var fullName: String = ""
            set(value) {
                field = value
                notifyPropertyChanged(BR.fullName)

            }

        @get:Bindable
        var email: String = ""
            set(value) {
                field = value
                notifyPropertyChanged(BR.email)
            }

        @get:Bindable
        var password: String = ""
            set(value) {
                field = value
                notifyPropertyChanged(BR.password)
            }

        @get:Bindable
        var confirmPassword: String = ""
            set(value) {
                field = value
                notifyPropertyChanged(BR.confirmPassword)
            }
    }
}
