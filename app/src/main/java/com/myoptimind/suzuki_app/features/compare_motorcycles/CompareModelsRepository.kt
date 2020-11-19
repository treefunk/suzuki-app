package com.myoptimind.suzuki_app.features.compare_motorcycles

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.myoptimind.suzuki_app.features.compare_motorcycles.api.CompareModelsService
import com.myoptimind.suzuki_app.features.compare_motorcycles.data.CompareModelDetail
import retrofit2.Call
import retrofit2.Callback
import javax.inject.Inject

class CompareModelsRepository @Inject constructor(
        private val compareModelsService: CompareModelsService
){

    fun getMotorcycleCompareDetails(
            motorcycleId: String
    ): Call<JsonObject> {
        return compareModelsService.getMotorcycleComparableDetails(motorcycleId)
    }

}