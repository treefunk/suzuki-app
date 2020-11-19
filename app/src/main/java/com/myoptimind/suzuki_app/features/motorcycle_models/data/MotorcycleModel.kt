package com.myoptimind.suzuki_app.features.motorcycle_models.data

import com.google.gson.annotations.SerializedName
import com.myoptimind.suzuki_app.features.dealer_locator.api.DealerLocatorService
import com.myoptimind.suzuki_app.features.dealer_locator.data.DealerLocatorsListItem
import com.myoptimind.suzuki_app.features.shared.data.City

data class MotorcycleModel (
    val id: String,

    val name: String,

    val tagline: String,

    val category: String,

    @SerializedName("variant_name")
    val variantName: String,

    val price: String,

    val thumbnail: String,

    val logo: String,

    @SerializedName("m_360_url")
    val url360 : String,

    @SerializedName("product_video_url")
    val productVideoUrl: String,

    val brochure: String,

    @SerializedName("list_of_dealers")
    val listOfDealers: List<DealerLocatorsListItem>,

    val cities: List<City>,

    @SerializedName("color_variants")
    val colorVariants: List<ColorVariant>,

    val features: List<Feature>,

    @SerializedName("engine_specs")
    val engineSpecs: List<Spec>,

    @SerializedName("chassis_specs")
    val chassisSpecs: List<Spec>,

    val dimensions: List<Spec>,

    @SerializedName("f_created_at")
    val formattedCreatedAt: String
)

data class ColorVariant (
        val color: String,
        val image: String,
        @SerializedName("variant_name")
        val variantName: String
)

data class Feature (
        val image: String
)

data class Spec(
        val field: String,
        val value: String
)