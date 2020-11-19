package com.myoptimind.suzuki_app.features.suzuki_diary.service_history.data

import com.google.gson.annotations.SerializedName
import com.myoptimind.suzuki_app.features.shared.BaseRemoteEntity


// API / Remote version of service history
class ServiceHistory(

        @SerializedName("customer_id")
        var customerId: String? = null,

        @SerializedName("motorcycle_registration_id")
        var registeredMotorcycleId: String? = null,

        @SerializedName("current_odometer_reading")
        var currentOdometerReading: String? = null,

        @SerializedName("next_pms_date")
        var nextPmsDate: String? = null,

        @SerializedName("mileage_left")
        var mileageLeft: String? = null,

        @SerializedName("change_oil")
        var changeOil: String = "0",

        var tires: String = "0",

        var brakes: String = "0",

        @SerializedName("chains_and_sprockets")
        var chainsAndSprockets: String = "0",

        @SerializedName("air_filter")
        var airFilter: String = "0",

        @SerializedName("spark_plugs")
        var sparkPlugs: String = "0",

        @SerializedName("exhaust_muffler")
        var exhaust_muffler: String = "0",

        var suspension: String = "0",

        @SerializedName("chassis_bolts_nuts")
        var chassisBoltsNuts: String = "0",

        var notes: String = "",

        @SerializedName("change_oil_e")
        var changeOilE: String = "0",

        @SerializedName("tires_e")
        var tiresE: String = "0",

        @SerializedName("brakes_e")
        var brakesE: String = "0",

        @SerializedName("chains_and_sprockets_e")
        var chainsAndSprocketsE: String = "0",

        @SerializedName("air_filter_e")
        var airFilterE: String = "0",

        @SerializedName("spark_plugs_e")
        var sparkPlugsE: String = "0",

        @SerializedName("exhaust_muffler_e")
        var exhaust_mufflerE: String = "0",

        @SerializedName("suspension_e")
        var suspensionE: String = "0",

        @SerializedName("chassis_bolts_nuts_e")
        var chassisBoltsNutsE: String = "0",

        @SerializedName("notes_e")
        var notesE: String = "",

        @SerializedName("beast_nickname")
        var beastNickname: String? = null,

        @SerializedName("date_purchased")
        var purchasedDate: String? = null,

        var id: String? = null

): BaseRemoteEntity()