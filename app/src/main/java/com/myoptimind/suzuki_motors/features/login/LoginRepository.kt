package com.myoptimind.suzuki_motors.features.login

import android.content.Context
import android.provider.Settings
import com.google.android.gms.tasks.Tasks
import com.google.firebase.installations.FirebaseInstallations
import com.myoptimind.suzuki_motors.features.login.api.LoginService
import okhttp3.MultipartBody
import timber.log.Timber
import javax.inject.Inject

class LoginRepository
@Inject
constructor(
        private val loginService: LoginService,
        private val context: Context
) {
    suspend fun getFeaturedMotorcycles(): LoginService.FeaturedMotorcycleResponse = loginService.getFeaturedMotorcycles()

    suspend fun authenticateUser(emailAddress: String, password: String, token: String): LoginService.LoginResponse {
        val deviceId = getDeviceId()
        Timber.d("token: $token")
        return loginService.loginUser(emailAddress, password, deviceId, token)
    }

    private fun getDeviceId() = Settings.Secure.getString(context.getContentResolver(),
            Settings.Secure.ANDROID_ID);


    private suspend fun getFirebaseId(): String {
        val idTask = FirebaseInstallations.getInstance().id
        val tokenTask = FirebaseInstallations.getInstance().getToken(false)
//        Timber.d("token " + Tasks.await(tokenTask).token)
        return Tasks.await(tokenTask).token
//        return Tasks.await(idTask)


    }

    suspend fun authenticateSocialLoginUser(
            emailAddress: String,
            socialToken: String,
            fullname: String,
            token: String

    ): LoginService.LoginResponse {
        return loginService.loginSocialAccount(emailAddress, socialToken, fullname, getDeviceId(), token)
    }

    suspend fun registerUser(fullname: String, emailAddress: String, password: String): LoginService.RegisterUserResponse {
        return loginService.registerUser(
                fullname,
                emailAddress,
                password
        )
    }

    suspend fun logoutUser(): LoginService.LogoutResponse = loginService.logoutUser(getDeviceId())

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