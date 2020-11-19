package com.myoptimind.suzuki_app.features.suzuki_diary.my_motorcycles.data

import com.google.gson.annotations.SerializedName
import com.myoptimind.suzuki_app.features.shared.BaseRemoteEntity

class RegisteredMotorcycle(
        val id: String,

        @SerializedName("beast_nickname")
        val beastNickname: String,

        @SerializedName("customer_id")
        val customerId: String,

        @SerializedName("customer_name")
        val customerName: String,

        @SerializedName("motorcycle_model_id")
        val motorcycleModelId: String,

        @SerializedName("motorcycle_model_name")
        val motorcycleModelName: String,

        @SerializedName("engine_number")
        val engineNumber: String,

        @SerializedName("frame_number")
        val frameNumber: String,

        @SerializedName("date_purchased")
        val datePurchased: String,

        @SerializedName("purchased_in")
        val purchasedIn: String
): BaseRemoteEntity()