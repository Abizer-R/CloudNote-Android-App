package com.example.cloudnote.data.respositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cloudnote.data.api.UserAPI
import com.example.cloudnote.data.models.userModels.UserRequest
import com.example.cloudnote.data.models.userModels.UserResponse
import com.example.cloudnote.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class AuthRepository
@Inject constructor(private val userAPI: UserAPI){

    private val TAG = AuthRepository::class.java.simpleName + "_TESTING"

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())

        try {
            val response = userAPI.signUp(userRequest)
            handleResponse(response)

        } catch (e : Exception) {
            Log.i(TAG, "registerUser: EXCEPTION = ${e.message}")
            e.printStackTrace()
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    suspend fun loginUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())

        try {
            val response = userAPI.signIn(userRequest)
            handleResponse(response)

        } catch (e : Exception) {
            Log.i(TAG, "loginUser: EXCEPTION = ${e.message}")
            e.printStackTrace()
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            Log.i(TAG, "response = ${response.body().toString()}")
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))

        } else if (response.errorBody() != null) {
            // read whole json object
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            // extract the error message
            val errorMsg = errorObj.getString("message")

            Log.i(TAG, "response = $errorMsg")
            _userResponseLiveData.postValue((NetworkResult.Error(errorMsg)))

        } else {
            Log.i(TAG, "registerUser: SOMETHING WENT WRONG")
            _userResponseLiveData.postValue((NetworkResult.Error("Something went wrong")))

        }
    }

}