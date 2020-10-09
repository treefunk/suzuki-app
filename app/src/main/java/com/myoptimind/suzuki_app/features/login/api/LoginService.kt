package com.myoptimind.suzuki_app.features.login.api

import com.google.gson.annotations.SerializedName
import com.myoptimind.suzuki_app.features.login.data.LoginFeaturedMotorcycle
import com.myoptimind.suzuki_app.features.login.data.User
import com.myoptimind.suzuki_app.shared.api.MetaResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


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

}