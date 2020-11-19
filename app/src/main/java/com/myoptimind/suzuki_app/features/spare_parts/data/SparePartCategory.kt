package com.myoptimind.suzuki_app.features.spare_parts.data

enum class SparePartCategory(
        val id: String,
        val title: String
) {
    TIRES(
            "1",
            "Tires"
    ),
    COOLING_FAN_FILTER(
            "2",
            "Cooling Fan Filter"
    ),
    ENGINE_OIL(
            "3",
            "Engine Oil"
    ),
    DRIVE_BELT(
            "4",
            "Drive Belt"
    ),
    BATTERY(
            "5",
            "Battery"
    ),
    BRAKE_FLUID(
            "6",
            "Brake Fluid"
    ),
    BRAKE_PAD(
            "7",
            "Brake Pad"
    ),
    BRAKE_SHOE(
            "8",
            "Brake Shoe"
    ),
    SPARK_PLUG(
            "9",
            "Spark Plug"
    ),
    AIR_CLEANER_ELEMENT(
            "10",
            "Air Cleaner Element"
    ),
    OIL_FILTER(
            "11",
            "Oil Filter"
    )
}