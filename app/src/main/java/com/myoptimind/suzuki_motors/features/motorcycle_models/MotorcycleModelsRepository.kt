package com.myoptimind.suzuki_motors.features.motorcycle_models

import com.myoptimind.suzuki_motors.features.motorcycle_models.api.MotorcycleModelsService
import javax.inject.Inject

class MotorcycleModelsRepository @Inject constructor(
        private val motorcycleModelsService: MotorcycleModelsService
) {
    suspend fun getMotorcycleModelList(
        name: String?,
        category: String?,
        offset: Int?,
        limit: Int?
    ): MotorcycleModelsService.MotorcycleModelsResponse {
        return motorcycleModelsService.getMotorcycleModelsList(
                name,
                category,
                offset?.toString(),
                limit?.toString()
        )
    }

    suspend fun getSingleMotorcycleModel(
            id: String
    ): MotorcycleModelsService.GetSingleMotorcycleModelResponse {
        return motorcycleModelsService.getSingleMotorcycleModel(id)
    }
}