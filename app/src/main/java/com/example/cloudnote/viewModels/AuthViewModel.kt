package com.example.cloudnote.viewModels

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloudnote.data.models.userModels.UserRequest
import com.example.cloudnote.data.models.userModels.UserResponse
import com.example.cloudnote.data.respositories.AuthRepository
import com.example.cloudnote.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
@Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val TAG = AuthViewModel::class.java.simpleName + "_TESTING"

    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = authRepository.userResponseLiveData

    fun registerUser(userRequest: UserRequest) {
        viewModelScope.launch {
            authRepository.registerUser(userRequest)
        }
    }

    fun loginUser(userRequest: UserRequest) {
        viewModelScope.launch {
            authRepository.loginUser(userRequest)
        }
    }

    fun validateCredentials(userRequest: UserRequest, isLogin: Boolean): Pair<Boolean, String> {
        var result = Pair(true, "")

        if((!isLogin && TextUtils.isEmpty(userRequest.username)) || TextUtils.isEmpty(userRequest.email) || TextUtils.isEmpty(userRequest.password)) {
            result = Pair(false, "Please enter all credentials")

        } else if(Patterns.EMAIL_ADDRESS.matcher(userRequest.email).matches() == false) {
            result = Pair(false, "Please provide valid email")

        } else if(userRequest.password.length <= 5) {
            result = Pair(false, "Password length must be greater than 5")

        }
        return result
    }
}