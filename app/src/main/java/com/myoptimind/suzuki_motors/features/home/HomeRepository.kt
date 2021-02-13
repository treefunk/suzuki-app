package com.myoptimind.suzuki_motors.features.home

import com.myoptimind.suzuki_motors.features.home.api.HomeService
import javax.inject.Inject

class HomeRepository
@Inject
constructor(
    private val homeService: HomeService
) {
    suspend fun getHomeFeatured(): HomeService.HomeFeaturedResponse = homeService.getHomeFeatured()
}