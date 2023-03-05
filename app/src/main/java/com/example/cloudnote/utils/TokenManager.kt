package com.example.cloudnote.utils

import android.content.Context
import com.example.cloudnote.utils.Constants.PREFS_TOKEN_FILE
import com.example.cloudnote.utils.Constants.PREFS_TOKEN_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor (@ApplicationContext context: Context) {

    private var prefs = context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    fun saveToken(token : String) {
        val prefEditor = prefs.edit()
        prefEditor.putString(PREFS_TOKEN_KEY, token)
        prefEditor.apply()
    }

    fun getToken() : String? {
        // 'null' is default value here
        return prefs.getString(PREFS_TOKEN_KEY, null)
    }
}