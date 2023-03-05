package com.example.cloudnote.data.api

import com.example.cloudnote.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

    @Inject
    lateinit var tokenManager: TokenManager

    override fun intercept(chain: Interceptor.Chain): Response {
        // (we will intercept this request and add header into it)
        val request = chain.request().newBuilder()

        val token = tokenManager.getToken()
        // We add "BEARER " (just like postman)
        request.addHeader("Authorization", "BEARER $token")

        // now we proceed with the new request (containing our header)
        val newRequest = request.build()
        return chain.proceed(newRequest)
    }
}