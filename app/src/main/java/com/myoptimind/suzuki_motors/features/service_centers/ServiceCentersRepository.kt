package com.myoptimind.suzuki_motors.features.service_centers

import com.myoptimind.suzuki_motors.features.service_centers.api.ServiceCentersService
import javax.inject.Inject

class ServiceCentersRepository @Inject constructor(
        private val serviceCentersService: ServiceCentersService
) {
    suspend fun getServiceCenters(
            name: String?,
            location: String?,
            offset: Int?,
            limit: Int?
    ): ServiceCentersService.ServiceCentersResponse {
        return serviceCentersService.getServiceCenters(
                name,
                location,
                offset?.toString(),
                limit?.toString()
        )
    }

    suspend fun getSingleServiceCenter(
            id: String
    ): ServiceCentersService.SingleServiceCenterResponse {
        return serviceCentersService.getSingleServiceCenter(id)
    }


}