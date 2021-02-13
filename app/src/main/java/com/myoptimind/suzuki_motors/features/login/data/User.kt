package com.myoptimind.suzuki_motors.features.login.data

import com.google.gson.annotations.SerializedName
import com.myoptimind.suzuki_motors.features.shared.BaseRemoteEntity

class User(
        val id: String,

        val fullname: String,

        @SerializedName("email_address")
        val emailAddress: String,

        @SerializedName("mobile_number")
        val mobileNumber: String,

        @SerializedName("social_token")
        val socialToken: String,

        val birthday: String,

        val address: String,

        @SerializedName("is_notifies")
        val isNotifies: String,

        @SerializedName("profile_picture")
        val profilePicture: String
): BaseRemoteEntity()