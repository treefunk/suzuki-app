package com.myoptimind.suzuki_motors.features.spare_parts.data

import com.google.gson.annotations.SerializedName
import com.myoptimind.suzuki_motors.features.dealer_locator.data.DealerLocatorsListItem

class SparePart (
        val id: String,
        val name: String,
        val number: String,
        val thumbnail: String,
        @SerializedName("model_number")
        val modelNumber: String,
        val price: String,
        @SerializedName("info_link")
        val infoLink: String,

        @SerializedName("list_of_dealers")
        val listOfDealers: List<DealerLocatorsListItem>
)