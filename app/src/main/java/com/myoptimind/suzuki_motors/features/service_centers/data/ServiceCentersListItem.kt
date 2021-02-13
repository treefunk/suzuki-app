package com.myoptimind.suzuki_motors.features.service_centers.data

import com.google.gson.annotations.SerializedName

class ServiceCentersListItem (

        val id: String,

        val name: String,

        @SerializedName("full_address")
        val fullAddress: String,

        @SerializedName("email_address")
        val emailAddress: String,

        val city: String,

        val latitude: String? = null,

        val longitude: String? = null,

        @SerializedName("contact_number")
        val contactNumber: String,

        @SerializedName("mobile_number")
        val mobileNumber: String,

        @SerializedName("info_link")
        val infoLink: String
)
