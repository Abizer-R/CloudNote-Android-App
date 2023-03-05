package com.example.cloudnote.data.api

import com.example.cloudnote.data.models.userModels.UserRequest
import com.example.cloudnote.data.models.userModels.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {

    @POST("/users/signup")
    suspend fun signUp (@Body userRequest : UserRequest) : Response<UserResponse>

    @POST("/users/signin")
    suspend fun signIn (@Body userRequest : UserRequest) : Response<UserResponse>
}