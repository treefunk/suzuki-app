package com.myoptimind.suzuki_app.features.login

import android.provider.Settings
import com.google.firebase.iid.FirebaseInstanceId
import com.myoptimind.suzuki_app.features.login.api.LoginService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.Multipart
import retrofit2.http.Path
import javax.inject.Inject

class LoginRepository
@Inject
constructor(
        private val loginService: LoginService
) {
    suspend fun getFeaturedMotorcycles(): LoginService.FeaturedMotorcycleResponse = loginService.getFeaturedMotorcycles()

    suspend fun authenticateUser(emailAddress: String, password: String): LoginService.LoginResponse {
        return loginService.loginUser(emailAddress, password, getDeviceId(), getFirebaseId())
    }

    private fun getDeviceId() = Settings.Secure.ANDROID_ID

    //    private fun getDeviceId() = "123"
    private fun getFirebaseId() = FirebaseInstanceId.getInstance().id

    suspend fun authenticateSocialLoginUser(
            emailAddress: String,
            socialToken: String,
            fullname: String

    ): LoginService.LoginResponse {
        return loginService.loginSocialAccount(emailAddress, socialToken, fullname, getDeviceId(), getFirebaseId())
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

    suspend fun changePassword(
            userId: String,
            oldPassword: String,
            newPassword: String,
            confirmPassword: String
    ): LoginService.ChangePasswordResponse {
        return loginService.changePassword(
                userId,
                oldPassword,
                newPassword,
                confirmPassword
        )
    }


    suspend fun updateProfilePicture(
            userId: String,
            profilePicture: MultipartBody.Part
    ): LoginService.EditProfilePictureResponse {
        return loginService.editProfilePicture(
                userId,
                profilePicture
        )
    }
}