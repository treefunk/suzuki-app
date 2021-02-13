package com.myoptimind.suzuki_motors.features.compare_motorcycles.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CompareModelsService {

    @GET("compare-motorcycles/{motorcycleId}")
    fun getMotorcycleComparableDetails(
            @Path("motorcycleId") motorcycleId: String
    ): Call<JsonObject>

}