package com.myoptimind.suzuki_app.features.compare_motorcycles.api

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.myoptimind.suzuki_app.features.compare_motorcycles.data.CompareModelDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Path

interface CompareModelsService {

    @GET("compare-motorcycles/{motorcycleId}")
    fun getMotorcycleComparableDetails(
            @Path("motorcycleId") motorcycleId: String
    ): Call<JsonObject>

}