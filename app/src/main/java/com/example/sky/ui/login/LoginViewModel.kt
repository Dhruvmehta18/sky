package com.example.sky.ui.login

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

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email
    private val _password = MutableLiveData<String>()
    val password: LiveData<String>
        get() = _password

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState>
        get() = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult>
        get() = _loginResult

    init {
        _loginForm.value = LoginFormState()
    }

    fun login(email: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(email, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(
                    success = LoggedInUserView(
                        displayName = result.data.displayName,
                        email = result.data.email
                    )
                )
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    private fun loginDataChanged() {
        if (!isValidUsername(email.value.orEmpty())) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password.value.orEmpty())) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isValidUsername(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 8
    }

    private fun emailDataChanged(email: String) {
        _email.value = email
        loginDataChanged()
    }

    private fun passwordDataChanged(password: String) {
        _password.value = password
        loginDataChanged()
    }


    inner class LoginFormModel(
        private var _mEmail: String,
        private var _mPassword: String
    ) : BaseObservable() {
        init {
            emailDataChanged(_mEmail)
            passwordDataChanged(_mPassword)
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
    }
}
