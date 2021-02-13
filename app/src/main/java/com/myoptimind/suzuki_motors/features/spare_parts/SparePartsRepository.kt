package com.myoptimind.suzuki_motors.features.spare_parts

import com.myoptimind.suzuki_motors.features.spare_parts.api.SparePartsService
import javax.inject.Inject

class SparePartsRepository @Inject constructor(
        private val sparePartsService: SparePartsService
){

    suspend fun getSpareParts(
            sparePartId: String,
            motorcycleName: String?,
            name: String?,
            offset: Int?,
            limit: Int?
    ): SparePartsService.SparePartResponse {
        return sparePartsService.getSparePart(
                sparePartId,
                motorcycleName,
                name,
                offset?.toString(),
                limit?.toString()
        )
    }

}