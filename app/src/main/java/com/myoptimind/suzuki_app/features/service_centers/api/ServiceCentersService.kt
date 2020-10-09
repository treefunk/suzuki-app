package com.myoptimind.suzuki_app.features.service_centers.api

import com.myoptimind.suzuki_app.features.service_centers.data.ServiceCentersListItem
import com.myoptimind.suzuki_app.shared.api.MetaResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceCentersService {

    @GET("service-centers/all")
    suspend fun getServiceCenters(
            @Query("name") name: String? = null,
            @Query("location") location: String? = null,
            @Query("per_rows") limit: String? = null,
            @Query("offset") offset: String? = null
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

        inner class City(
                val city: String
        )

        inner class Service(
                val name: String,
                val thumbnail: String
        )
    }
}