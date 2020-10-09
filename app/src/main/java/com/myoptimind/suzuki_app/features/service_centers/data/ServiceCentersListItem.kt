package com.myoptimind.suzuki_app.features.service_centers.data

import com.google.gson.annotations.SerializedName

class ServiceCentersListItem (
        val name: String,

        @SerializedName("full_address")
        val fullAddress: String,

        val city: String,

        @SerializedName("contact_number")
        val contactNumber: String,

        @SerializedName("mobile_number")
        val mobileNumber: String,

        @SerializedName("info_link")
        val infoLink: String
)
