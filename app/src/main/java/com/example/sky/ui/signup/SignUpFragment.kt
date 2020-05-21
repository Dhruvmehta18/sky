package com.example.sky.ui.signup

import android.os.Bundle
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
import androidx.navigation.Navigation
import com.example.sky.R
import com.example.sky.databinding.FragmentSignUpBinding
import com.example.sky.ui.login.LoggedInUserView

class SignUpFragment : Fragment() {

    private lateinit var signUpViewModel: SignUpViewModel

    private lateinit var binding: FragmentSignUpBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        binding.lifecycleOwner = this
        signUpViewModel = ViewModelProvider(
            this,
            SignUpViewModelFactory()
        ).get(SignUpViewModel::class.java)
        binding.signUpViewModel = signUpViewModel
        signUpViewModel.signUpFormState.observe(viewLifecycleOwner, Observer { signUpFormState ->
            if (signUpFormState == null) {
                return@Observer
            }
            binding.signUp.isEnabled = signUpFormState.isDataValid
            signUpFormState.fullNameError?.let {
                binding.fullNameSignUp.error = getString(it)
            }
            signUpFormState.emailError?.let {
                binding.emailSignUp.error = getString(it)
            }
            signUpFormState.passwordError?.let {
                binding.passwordSignUp.error = getString(it)
            }
            signUpFormState.confirmPasswordError?.let {
                binding.passwordCheckSignUp.error = getString(it)
            }

        })

        signUpViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                binding.loadingSignUp.visibility = View.GONE
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    updateUiWithUser(it)
                }
            })

        signUpViewModel.fullName.observe(viewLifecycleOwner, Observer { fullName ->
            signUpViewModel.fullNameDataChanged(fullName)
        })
        signUpViewModel.email.observe(viewLifecycleOwner, Observer { email ->
            signUpViewModel.emailDataChanged(email)
        })
        signUpViewModel.password.observe(viewLifecycleOwner, Observer { password ->
            signUpViewModel.passwordDataChanged(password)
        })
        signUpViewModel.confirmPassword.observe(viewLifecycleOwner, Observer { confirmPassword ->
            signUpViewModel.confirmPasswordChanged(confirmPassword)
        })

        binding.passwordCheckSignUp.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                signUpViewModel.signUp(
                    binding.fullNameSignUp.text.toString(),
                    binding.emailSignUp.text.toString(),
                    binding.passwordSignUp.text.toString()
                )
            }
            false
        }

        binding.signUp.setOnClickListener {
            binding.loadingSignUp.visibility = View.VISIBLE
            signUpViewModel.signUp(
                binding.fullNameSignUp.text.toString(),
                binding.emailSignUp.text.toString(),
                binding.passwordSignUp.text.toString()
            )
        }

        binding.signUpLoginButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                SignUpFragmentDirections.actionSignUpFragmentToLoginFragment(
                    signUpViewModel.email.value,
                    signUpViewModel.password.value
                )
            )
        )
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
