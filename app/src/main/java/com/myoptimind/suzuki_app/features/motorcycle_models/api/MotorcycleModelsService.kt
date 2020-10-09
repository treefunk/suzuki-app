package com.myoptimind.suzuki_app.features.motorcycle_models.api

import com.google.gson.annotations.SerializedName
import com.myoptimind.suzuki_app.features.motorcycle_models.data.FilterName
import com.myoptimind.suzuki_app.features.motorcycle_models.data.MotorcycleModelListItem
import com.myoptimind.suzuki_app.shared.api.MetaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MotorcycleModelsService {


    @GET("motorcycles/all")
    suspend fun getMotorcycleModelsList(
            @Query("name") name: String? = null,
            @Query("cat") category: String? = null,
            @Query("offset") offset: String? = null,
            @Query("per_rows") limit: String? = null
    ): MotorcycleModelsResponse

    class MotorcycleModelsResponse(
            val data: Data,
            val meta: MetaResponse
    ){
        class Data (
            val result: List<MotorcycleModelListItem>,

            @SerializedName("filter_by")
            val filters: List<FilterName>
        )
    }
}