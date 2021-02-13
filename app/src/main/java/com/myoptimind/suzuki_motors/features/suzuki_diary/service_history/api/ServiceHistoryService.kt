package com.myoptimind.suzuki_motors.features.suzuki_diary.service_history.api

import com.myoptimind.suzuki_motors.features.shared.api.MetaResponse
import com.myoptimind.suzuki_motors.features.suzuki_diary.service_history.data.ServiceHistory
import retrofit2.http.*

interface ServiceHistoryService {

    @GET("customers/service-history-list/{customerId}")
    suspend fun getServiceHistoryList(
            @Path("customerId") customerId: String,
            @Query("f") maxDayLimitFromNow: String? = null
    ): ServiceHistoryListResponse

    class ServiceHistoryListResponse(
            val data: List<ServiceHistory>,
            val meta: MetaResponse
    )

    @POST("customers/add-service-history")
    @FormUrlEncoded
    suspend fun addServiceHistory(
        @Field("customer_id") customerId: String,
        @Field("motorcycle_registration_id") registeredMotorcycleId: String,
        @Field("current_odometer_reading") currentOdometerReading: String,
//        @Field("purchased_date") purchasedDate: String,
        @Field("next_pms_date") nextPmsDate: String,
        @Field("mileage_left") mileageLeft: String,
        @Field("change_oil") changeOil: String,
        @Field("tires") tires: String,
        @Field("brakes") brakes: String,
        @Field("chains_and_sprockets") chainsAndSprockets: String,
        @Field("air_filter") airFilter: String,
        @Field("spark_plugs") sparkPlugs: String,
        @Field("exhaust_muffler") exhaustMuffler: String,
        @Field("suspension") suspension: String,
        @Field("chassis_bolts_nuts") chassisBoltsNuts: String,
        @Field("notes") notes: String,
        @Field("change_oil_e") changeOilE: String,
        @Field("tires_e") tiresE: String,
        @Field("brakes_e") brakesE: String,
        @Field("chains_and_sprockets_e") chainsAndSprocketsE: String,
        @Field("air_filter_e") airFilterE: String,
        @Field("spark_plugs_e") sparkPlugsE: String,
        @Field("exhaust_muffler_e") exhaustMufflerE: String,
        @Field("suspension_e") suspensionE: String,
        @Field("chassis_bolts_nuts_e") chassisBoltsNutsE: String,
        @Field("notes_e") notesE: String
    ): AddServiceHistoryResponse
/*@POST("customers/add-service-history")
@Headers("Content-Type: application/json")
@FormUrlEncoded
suspend fun addServiceHistory(
        @Body serviceHistory: ServiceHistory
): AddServiceHistoryResponse*/

    class AddServiceHistoryResponse(
            val data: IdData,
            val meta: MetaResponse // we only need the meta data
    )

    class IdData (
            val id: Int
    )

    @POST("customers/update-service-history/{service_history_id}")
    @FormUrlEncoded
    suspend fun updateServiceHistory(
            @Path("service_history_id") serviceHistoryId: String,
            @Field("customer_id") customerId: String,
            @Field("motorcycle_registration_id") registeredMotorcycleId: String,
            @Field("current_odometer_reading") currentOdometerReading: String,
            @Field("next_pms_date") nextPmsDate: String,
            @Field("mileage_left") mileageLeft: String,
            @Field("change_oil") changeOil: String,
            @Field("tires") tires: String,
            @Field("brakes") brakes: String,
            @Field("chains_and_sprockets") chainsAndSprockets: String,
            @Field("air_filter") airFilter: String,
            @Field("spark_plugs") sparkPlugs: String,
            @Field("exhaust_muffler") exhaustMuffler: String,
            @Field("suspension") suspension: String,
            @Field("chassis_bolts_nuts") chassisBoltsNuts: String,
            @Field("notes") notes: String,
            @Field("change_oil_e") changeOilE: String,
            @Field("tires_e") tiresE: String,
            @Field("brakes_e") brakesE: String,
            @Field("chains_and_sprockets_e") chainsAndSprocketsE: String,
            @Field("air_filter_e") airFilterE: String,
            @Field("spark_plugs_e") sparkPlugsE: String,
            @Field("exhaust_muffler_e") exhaustMufflerE: String,
            @Field("suspension_e") suspensionE: String,
            @Field("chassis_bolts_nuts_e") chassisBoltsNutsE: String,
            @Field("notes_e") notesE: String
    ): UpdateServiceHistoryResponse

    class UpdateServiceHistoryResponse(
            val data: ServiceHistory,
            val meta: MetaResponse
    )
}