package com.example.sky.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.sky.R
import com.example.sky.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory()).get(LoginViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.loginViewModel = loginViewModel
        val args = LoginFragmentArgs.fromBundle(requireArguments())

        val email = args.email
        val password = args.password
        Log.i("sdsd", "$email $password")
        Toast.makeText(context, "$email $password", Toast.LENGTH_SHORT).show()
        binding.loginFormModel = loginViewModel.LoginFormModel(email.orEmpty(), password.orEmpty())

        loginViewModel.loginFormState.observe(viewLifecycleOwner, Observer { loginFormState ->
            if (loginFormState == null) {
                return@Observer
            }
            binding.login.isEnabled = loginFormState.isDataValid
            loginFormState.usernameError?.let {
                binding.emailLogin.error = getString(it)
            }
            loginFormState.passwordError?.let {
                binding.passwordLogin.error = getString(it)
            }
        })

        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                binding.loadingLogin.visibility = View.GONE
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    updateUiWithUser(it)
                }
            })

        binding.passwordLogin.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    binding.emailLogin.text.toString(),
                    binding.passwordLogin.text.toString()
                )
            }
            false
        }

        binding.login.setOnClickListener {
            binding.loadingLogin.visibility = View.VISIBLE
            loginViewModel.login(
                binding.emailLogin.text.toString(),
                binding.passwordLogin.text.toString()
            )
        }
        binding.loginSignUpButton.setOnClickListener {
            it.findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToSignUpFragment(
                    loginViewModel.email.value,
                    loginViewModel.password.value
                )
            )
        }
        return binding.root
    }


    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }
}
