package com.myoptimind.suzuki_app.features.home.api

import com.myoptimind.suzuki_app.features.home.data.HomeFeaturedProduct
import com.myoptimind.suzuki_app.features.shared.api.MetaResponse
import retrofit2.http.GET

interface HomeService {

    @GET("contents/slider_home")
    suspend fun getHomeFeatured(): HomeFeaturedResponse

    class HomeFeaturedResponse(
            val data: List<HomeFeaturedProduct>,
            val meta: MetaResponse
    )
}