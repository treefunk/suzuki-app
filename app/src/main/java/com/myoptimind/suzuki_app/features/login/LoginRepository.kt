package com.myoptimind.suzuki_app.features.login

import android.provider.Settings
import com.google.firebase.iid.FirebaseInstanceId
import com.myoptimind.suzuki_app.features.login.api.LoginService
import javax.inject.Inject

class LoginRepository
@Inject
constructor(
        private val loginService: LoginService
)
{
    suspend fun getFeaturedMotorcycles(): LoginService.FeaturedMotorcycleResponse = loginService.getFeaturedMotorcycles()

    suspend fun authenticateUser(emailAddress: String, password: String): LoginService.LoginResponse {
        val deviceId = Settings.Secure.ANDROID_ID
        val firebaseId = FirebaseInstanceId.getInstance().id
        return loginService.loginUser(emailAddress,password,deviceId,firebaseId)
    }

    suspend fun registerUser(fullname: String, emailAddress: String, password: String): LoginService.RegisterUserResponse {
        return loginService.registerUser(
                fullname,
                emailAddress,
                password
        )
    }

    suspend fun logoutUser(deviceId: String): LoginService.LogoutResponse = loginService.logoutUser(deviceId)

    suspend fun requestForgotPassword(emailAddress: String): LoginService.ForgotPasswordResponse = loginService.requestForgotPassword(emailAddress)
}