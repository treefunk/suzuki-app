package com.myoptimind.suzuki_app.features.login.data

import com.google.gson.annotations.SerializedName
import com.myoptimind.suzuki_app.shared.BaseRemoteEntity

/**
 *  Items that displays on login slider
 */
data class LoginFeaturedMotorcycle(
        val id: String,

        val heading: String,

        @SerializedName("sub_heading")
        val subHeading: String,

        val redirection: String,

        val thumbnail: String,

        val background: String,

        val logo: String,

        @SerializedName("is_active")
        val isActive: String,

        @SerializedName("f_created_at")
        val formattedCreatedAt: String
): BaseRemoteEntity()