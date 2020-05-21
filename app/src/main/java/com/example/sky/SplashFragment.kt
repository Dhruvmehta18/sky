package com.example.sky

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.sky.databinding.FragmentSplashBinding

/**
 * A simple [Fragment] subclass.
 */
class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_splash, container, false)
        binding.loginButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_splashFragment_to_loginFragment)
        )
        binding.signupButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_splashFragment_to_signUpFragment)
        )

        return binding.root
    }

}
