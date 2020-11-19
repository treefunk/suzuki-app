package com.myoptimind.suzuki_app.features.suzuki_diary.service_history.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.Nonnull

@Entity(tableName = "service_history_maintenance")
data class ServiceHistoryMaintenance(
        @PrimaryKey
        @Nonnull
        @ColumnInfo(name = "tab_type")
        val tabType: String = "",

        @ColumnInfo(name = "change_oil")
        var changeOil: String = "0",

        var tires: String = "0",

        var brakes: String = "0",

        @ColumnInfo(name = "chains_and_sprockets")
        var chainsAndSprockets: String = "0",

        @ColumnInfo(name = "air_filter")
        var airFilter: String = "0",

        @ColumnInfo(name = "spark_plugs")
        var sparkPlugs: String = "0",

        @ColumnInfo(name = "exhaust_muffler")
        var exhaust_muffler: String = "0",

        var suspension: String = "0",

        @ColumnInfo(name = "chassis_bolts_nuts")
        var chassisBoltsNuts: String = "0",

        var notes: String = ""

)