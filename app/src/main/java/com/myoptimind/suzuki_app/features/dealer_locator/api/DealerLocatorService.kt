package com.myoptimind.suzuki_app.features.dealer_locator.api

import com.google.gson.annotations.SerializedName
import com.myoptimind.suzuki_app.features.dealer_locator.data.DealerLocatorsListItem
import com.myoptimind.suzuki_app.features.shared.api.MetaResponse
import com.myoptimind.suzuki_app.features.shared.data.City
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DealerLocatorService {

    @GET("dealer-locators/all")
    suspend fun getDealerLocators(
            @Query("name") name: String? = null,
            @Query("location") location: String? = null,
            @Query("offset") offset: String? = null,
            @Query("per_rows") limit: String? = null
    ): DealerLocatorsResponse

    class DealerLocatorsResponse(
            val data: Data,
            val meta: MetaResponse
    ){

        inner class Data(
                val result: List<DealerLocatorsListItem>,
                @SerializedName("filter_by")
                val cities: List<City>
        )
    }

    @GET("contents/dealer/{dealerId}")
    suspend fun getSingleDealerLocator(
            @Path("dealerId") dealerId: String
    ): SingleDealerLocatorResponse

    class SingleDealerLocatorResponse(
            val data: DealerLocatorsListItem,
            val meta: MetaResponse
    )


}

