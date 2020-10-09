package com.myoptimind.suzuki_app.features.home

import com.myoptimind.suzuki_app.features.home.api.HomeService
import javax.inject.Inject

class HomeRepository
@Inject
constructor(
    private val homeService: HomeService
) {
    suspend fun getHomeFeatured(): HomeService.HomeFeaturedResponse = homeService.getHomeFeatured()
}