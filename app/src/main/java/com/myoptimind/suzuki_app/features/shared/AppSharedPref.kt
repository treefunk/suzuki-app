package com.myoptimind.suzuki_app.features.shared

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.myoptimind.suzuki_app.features.login.data.User
import timber.log.Timber
import javax.inject.Singleton

const val NAME = "SuzukiSharedPreference"
const val FILE_NAME = "shared_pref"
private const val MODE = Context.MODE_PRIVATE

private const val PREF_USER_ID   = "pref_user_id"
private const val PREF_FULL_NAME = "pref_full_name"
private const val PREF_EMAIL_ADDRESS = "pref_email_address"
private const val PREF_PROFILE_PICTURE = "pref_profile_picture"

class AppSharedPref(val context: Context) {
    private var preferences: SharedPreferences? = null


    init {
        setupSharedPref()
    }

    private fun setupSharedPref() {
        Timber.v("initializing shared pref")
        val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
//        preferences = EncryptedSharedPreferences.create(context, FILE_NAME,masterKey,EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM) as EncryptedSharedPreferences
        preferences = try{
            EncryptedSharedPreferences.create(
                    context,
                    FILE_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }catch (exception: Exception){
            context.getSharedPreferences(NAME, MODE)
        }


    }

    fun storeUserLogin(userId: String){
        val editor = preferences?.edit()
        editor?.putString(PREF_USER_ID, userId)
        editor?.apply()
    }

    fun storeLoginCredentials(
            userId: String,
            fullname: String,
            emailAddress: String,
            profilePicture: String
    ){
        val editor = preferences?.edit()
        editor?.putString(PREF_USER_ID, userId)
        editor?.putString(PREF_FULL_NAME, fullname)
        editor?.putString(PREF_EMAIL_ADDRESS, emailAddress)
        editor?.putString(PREF_PROFILE_PICTURE, profilePicture)
        editor?.apply()
    }

    fun getUserFromPrefs(): User {
        return User(
                getUserId(),
                preferences?.getString(PREF_FULL_NAME,"")!!,
                preferences?.getString(PREF_EMAIL_ADDRESS,"")!!,
                "",
                "",
                "",
                "",
                "",
                preferences?.getString(PREF_PROFILE_PICTURE,"")!!
        )
    }

    fun getUserId(): String = preferences?.getString(PREF_USER_ID,"")!!

    fun removeUserId(){
        val editor = preferences?.edit()
        editor?.remove(PREF_USER_ID)
        editor?.remove(PREF_PROFILE_PICTURE)
        editor?.remove(PREF_EMAIL_ADDRESS)
        editor?.remove(PREF_FULL_NAME)

        editor?.apply()
    }

}