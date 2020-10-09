package com.myoptimind.suzuki_app.features.login.data

import com.google.gson.annotations.SerializedName
import com.myoptimind.suzuki_app.shared.BaseRemoteEntity

class User(
        val id: String,

        val fullname: String,

        @SerializedName("email_address")
        val emailAddress: String,

        @SerializedName("mobile_number")
        val mobileNumber: String,

        val birthday: String,

        val address: String,

        @SerializedName("is_notifies")
        val isNotifies: String
): BaseRemoteEntity()