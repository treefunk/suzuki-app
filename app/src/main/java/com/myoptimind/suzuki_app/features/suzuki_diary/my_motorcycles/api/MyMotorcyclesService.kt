package com.myoptimind.suzuki_app.features.suzuki_diary.my_motorcycles.api

import com.myoptimind.suzuki_app.features.shared.api.MetaResponse
import com.myoptimind.suzuki_app.features.shared.data.FilterName
import com.myoptimind.suzuki_app.features.suzuki_diary.my_motorcycles.data.MotorcycleModel
import com.myoptimind.suzuki_app.features.suzuki_diary.my_motorcycles.data.RegisteredMotorcycle
import retrofit2.http.*

interface MyMotorcyclesService{

    @POST("customers/register-my-motor")
    @FormUrlEncoded
    suspend fun registerMotorcycle(
            @Field("beast_nickname") beastNickName: String,
            @Field("customer_id") customerId: String,
            @Field("motorcycle_model_id") motorcycleModelId: String,
            @Field("engine_number") engineNumber: String,
            @Field("frame_number") frameNumber: String,
            @Field("date_purchased") datePurchased: String,
            @Field("purchased_in") purchasedIn: String
    ): RegisterMotorcycleResponse

    class RegisterMotorcycleResponse(
            val meta: MetaResponse
    )

    @POST("customers/registered-motor/{registered_motorcycle_id}")
    @FormUrlEncoded
    suspend fun updateRegisteredMotorcycle(
            @Path("registered_motorcycle_id") registeredMotorcycleId: String,
            @Field("beast_nickname") beastNickName: String,
            @Field("customer_id") customerId: String,
            @Field("motorcycle_model_id") motorcycleModelId: String,
            @Field("engine_number") engineNumber: String,
            @Field("frame_number") frameNumber: String,
            @Field("date_purchased") datePurchased: String,
            @Field("purchased_in") purchasedIn: String
    ): RegisterMotorcycleResponse


    @GET("motorcycles/all")
    suspend fun getMotorcycleModels(
            @Query("name") name: String? = null,
            @Query("cat") categoryName: String? = null,
            @Query("offset") offset: String? = null,
            @Query("per_rows") limit: String? = "9999999999"
    ): GetMotorcycleModelsListResponse

    class GetMotorcycleModelsListResponse(
            val data: Data,
            val meta: MetaResponse
    ){
        inner class Data(
                val result: List<MotorcycleModel>,
                val filters: List<FilterName>
        )
    }

    @GET("customers/motor_list/{customer_id}")
    suspend fun getRegisteredMotorcycles(
            @Path("customer_id") customerId: String
    ): RegisteredMotorcyclesResponse

    class RegisteredMotorcyclesResponse(
            val data: List<RegisteredMotorcycle>,
            val meta: MetaResponse
    )
}