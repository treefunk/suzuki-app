package com.myoptimind.suzuki_motors.features.spare_parts.api

import com.google.gson.annotations.SerializedName
import com.myoptimind.suzuki_motors.features.shared.api.MetaResponse
import com.myoptimind.suzuki_motors.features.shared.data.FilterName
import com.myoptimind.suzuki_motors.features.spare_parts.data.SparePart
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SparePartsService {

//    @GET("spare-parts/all?")
    @GET("spare-parts/category/{sparePartId}")
    suspend fun getSparePart(
        @Path("sparePartId") id: String,
        @Query("motorcycle") motorcycleName: String? = null,
        @Query("name") name: String? = null,
        @Query("offset") offset: String? = null,
        @Query("per_rows") limit: String? = null
    ): SparePartResponse

    class SparePartResponse(
        val data: Data,
        val meta: MetaResponse
    ){
        inner class Data (
            val result: List<SparePart>,
            @SerializedName("filter_by")
            val filters: List<FilterName>
        )

    }
}