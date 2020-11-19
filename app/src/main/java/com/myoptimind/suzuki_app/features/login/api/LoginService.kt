package com.myoptimind.suzuki_app.features.login.api

import com.google.gson.annotations.SerializedName
import com.myoptimind.suzuki_app.features.login.data.LoginFeaturedMotorcycle
import com.myoptimind.suzuki_app.features.login.data.User
import com.myoptimind.suzuki_app.features.shared.api.MetaResponse
import okhttp3.MultipartBody
import retrofit2.http.*


interface LoginService {

    @POST("customers/register")
    @FormUrlEncoded
    suspend fun registerUser(
            @Field("fullname") fullname: String,
            @Field("email_address") emailAddress: String,
            @Field("password") password: String
    ): RegisterUserResponse

    class RegisterUserResponse(
            val data: Data,
            val meta: MetaResponse
    ) {
        class Data(
                @SerializedName("fullname")
                val fullname: String,
                @SerializedName("email_address")
                val emailAddress: String,
                @SerializedName("mobile_number")
                val mobileNumber: String
        )
    }

    @POST("customers/login")
    @FormUrlEncoded
    suspend fun loginUser(
            @Field("email_address") emailAddress: String,
            @Field("password") password: String,
            @Field("device_id") deviceId: String,
            @Field("firebase_id") firebaseId: String
    ): LoginResponse

    class LoginResponse(
            val data: User,
            val meta: MetaResponse
    )

    @POST("customers/social_login")
    @FormUrlEncoded
    suspend fun loginSocialAccount(
            @Field("email_address") emailAddress: String,
            @Field("social_token") socialToken: String,
            @Field("fullname") fullname: String,
            @Field("device_id") deviceId: String,
            @Field("firebase_id") firebaseId: String
    ): LoginResponse

    @POST("customer/forgot-password")
    @FormUrlEncoded
    suspend fun requestForgotPassword(
            @Field("email_address") emailAddress: String
    ): ForgotPasswordResponse

    class ForgotPasswordResponse(
            val data: Boolean,
            val meta: MetaResponse
    )

    @POST("customers/logout")
    @FormUrlEncoded
    suspend fun logoutUser(
            @Field("device_id") deviceId: String
    ): LogoutResponse

    class LogoutResponse(
            val data: Nothing,
            val meta: MetaResponse
    )

    @GET("contents/home")
    suspend fun getFeaturedMotorcycles(): FeaturedMotorcycleResponse

    class FeaturedMotorcycleResponse(
            val data: List<LoginFeaturedMotorcycle>,
            val meta: MetaResponse
    )

    @POST("customer/edit-password/{customer_id}")
    @FormUrlEncoded
    suspend fun changePassword(
            @Path("customer_id") userId: String,
            @Field("old_password") oldPassword: String,
            @Field("new_password") newPassword: String,
            @Field("confirm_password") confirmPassword: String
    ): ChangePasswordResponse

    class ChangePasswordResponse(
            val meta: MetaResponse
    )

    @Multipart
    @POST("customers/edit_pfp/{customer_id}")
    suspend fun editProfilePicture(
            @Path("customer_id") userId: String,
            @Part profilePicture: MultipartBody.Part
    ): EditProfilePictureResponse

    class EditProfilePictureResponse(
            val data: User,
            val meta: MetaResponse
    )


}