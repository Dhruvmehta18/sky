package com.example.sky.ui.signup

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sky.*
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
    private val confirmPassword: LiveData<String>
        get() = _confirmPassword

    private val signUpForm = MutableLiveData<SignUpFormState>().apply {
        this.value = SignUpFormState(isDataValid = false)
    }
    val signUpFormState: LiveData<SignUpFormState>
        get() = signUpForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult>
        get() = _loginResult

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

    private fun fullNameDataChanged(fullName: String) {
        _fullName.value = fullName
        signUpDataChanged()
    }

    private fun emailDataChanged(email: String) {
        _email.value = email
        signUpDataChanged()
    }

    private fun passwordDataChanged(password: String) {
        _password.value = password
        signUpDataChanged()
    }

    private fun confirmPasswordDataChanged(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
        signUpDataChanged()
    }

    private fun signUpDataChanged() {
        signUpForm.value = SignUpFormState(
            isDataValid = isValidOnlyText(fullName.value.orEmpty()) &&
                    isValidEmail(email.value.orEmpty()) &&
                    isPasswordValid(password.value.orEmpty()) &&
                    isConfirmPasswordValid(
                        password.value.orEmpty(),
                        confirmPassword.value.orEmpty()
                    )
        )
    }

    inner class SignUpFormModel(
        private var _mFullName: String,
        private var _mEmail: String,
        private var _mPassword: String,
        private var _mConfirmPassword: String
    ) : BaseObservable() {
        init {
            fullNameDataChanged(_mFullName)
            emailDataChanged(_mEmail)
            passwordDataChanged(_mPassword)
            confirmPasswordDataChanged(_mConfirmPassword)
        }

        var mFullName: String
            @Bindable get() = _mFullName
            set(value) {
                _mFullName = value
                notifyPropertyChanged(BR.signUpFormModel)
                fullNameDataChanged(value)
                Log.i("SignUpForm", value)
            }

        var mEmail: String
            @Bindable get() = _mEmail
            set(value) {
                _mEmail = value
                notifyPropertyChanged(BR.signUpFormModel)
                emailDataChanged(value)
            }

        var mPassword: String
            @Bindable get() = _mPassword
            set(password: String) {
                _mPassword = password
                notifyPropertyChanged(BR.signUpFormModel)
                passwordDataChanged(password)
            }

        var mConfirmPassword: String
            @Bindable get() = _mConfirmPassword
            set(value) {
                _mConfirmPassword = value
                notifyPropertyChanged(BR.mConfirmPassword)
                confirmPasswordDataChanged(value)
            }
    }
}
