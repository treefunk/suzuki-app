package com.myoptimind.suzuki_motors.features.service_centers.api

import com.myoptimind.suzuki_motors.features.service_centers.data.ServiceCentersListItem
import com.myoptimind.suzuki_motors.features.shared.api.MetaResponse
import com.myoptimind.suzuki_motors.features.shared.data.City
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceCentersService {

    @GET("service-centers/all")
    suspend fun getServiceCenters(
            @Query("name") name: String? = null,
            @Query("location") location: String? = null,
            @Query("offset") offset: String? = null,
            @Query("per_rows") limit: String? = null
    ): ServiceCentersResponse

    class ServiceCentersResponse(
            val data: Data,
            val meta: MetaResponse
    ){

        inner class Data(
                val result: List<ServiceCentersListItem>,
                val cities: List<City>,
                val services: List<Service>
        )

        inner class Service(
                val name: String,
                val thumbnail: String
        )
    }

    @GET("service-center/single/{serviceCenterId}")
    suspend fun getSingleServiceCenter(
            @Path("serviceCenterId") serviceCenterId: String
    ): SingleServiceCenterResponse

    class SingleServiceCenterResponse(
            val data: ServiceCentersListItem,
            val meta: MetaResponse
    )
}