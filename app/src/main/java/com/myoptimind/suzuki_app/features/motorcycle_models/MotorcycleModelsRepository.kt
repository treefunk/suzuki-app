package com.myoptimind.suzuki_app.features.motorcycle_models

import com.myoptimind.suzuki_app.features.motorcycle_models.api.MotorcycleModelsService
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
}