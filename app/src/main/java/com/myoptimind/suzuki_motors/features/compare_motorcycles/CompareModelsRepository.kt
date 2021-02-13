package com.myoptimind.suzuki_motors.features.compare_motorcycles

import com.google.gson.JsonObject
import com.myoptimind.suzuki_motors.features.compare_motorcycles.api.CompareModelsService
import retrofit2.Call
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