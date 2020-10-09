package com.myoptimind.suzuki_app.shared

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext

const val NAME = "SuzukiSharedPreference"
const val FILE_NAME = "shared_pref"
private const val MODE = Context.MODE_PRIVATE

private const val PREF_USER_ID   = "pref_user_id"

class AppSharedPref(val context: Context) {
    private var preferences: EncryptedSharedPreferences? = null


    init {
        setupSharedPref()
    }

    private fun setupSharedPref() {
        val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        preferences = EncryptedSharedPreferences.create(context, FILE_NAME,masterKey,EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM) as EncryptedSharedPreferences
    }

    fun storeUserLogin(userId: String){
        val editor = preferences?.edit()
        editor?.putString(PREF_USER_ID, userId)
        editor?.apply()
    }

    fun getUserId(): String? = preferences?.getString(PREF_USER_ID,"")

}