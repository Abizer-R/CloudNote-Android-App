package com.example.cloudnote.di

import com.example.cloudnote.data.api.AuthInterceptor
import com.example.cloudnote.data.api.NoteAPI
import com.example.cloudnote.data.api.UserAPI
import com.example.cloudnote.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofitBuilder() : Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
//            .build()
    }

    @Provides
    @Singleton
    fun providesUserApi(retrofitBuilder: Retrofit.Builder) : UserAPI {
        return retrofitBuilder
            .build().create(UserAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor) : OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Provides
    @Singleton
    fun providesNoteApi(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient) : NoteAPI {
        return retrofitBuilder
            .client(okHttpClient)
            .build().create(NoteAPI::class.java)
    }
}