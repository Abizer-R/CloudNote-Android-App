package com.example.cloudnote.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cloudnote.R
import com.example.cloudnote.data.models.userModels.UserRequest
import com.example.cloudnote.databinding.FragmentLoginBinding
import com.example.cloudnote.utils.NetworkResult
import com.example.cloudnote.utils.TokenManager
import com.example.cloudnote.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val TAG = LoginFragment::class.java.simpleName + "_TESTING"
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutSignUp.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSignIn.setOnClickListener {
            val userRequest = getUserRequest()
            val userRequestValidation = authViewModel.validateCredentials(userRequest, true)

            if(userRequestValidation.first) {
                authViewModel.loginUser(userRequest)
            } else {
                binding.tvError.text = userRequestValidation.second
            }

//            val dummyUserRequest = UserRequest(
//                username = "dummy1",
//                email = "dummy1@gmail.com",
//                password = "dummy1"
//            )
//            authViewModel.loginUser(dummyUserRequest)
        }

        setupObservers()
    }

    private fun getUserRequest(): UserRequest {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        return UserRequest("", email, password)
    }

    private fun setupObservers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            stopLoading()
            when(it) {
                is NetworkResult.Loading -> {
                    startLoading()
                    // Hiding Error View
                    binding.tvError.text = ""
                }
                is NetworkResult.Error -> {
                    // Showing Error Views
                    binding.tvError.text = it.message
                }
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }
        }
    }

    private fun startLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSignIn.isClickable = false
        binding.btnSignIn.isEnabled = false

        binding.layoutSignUp.isClickable = false
        binding.layoutSignUp.isEnabled = false
    }

    private fun stopLoading() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnSignIn.isClickable = true
        binding.btnSignIn.isEnabled = true

        binding.layoutSignUp.isClickable = true
        binding.layoutSignUp.isEnabled = true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}