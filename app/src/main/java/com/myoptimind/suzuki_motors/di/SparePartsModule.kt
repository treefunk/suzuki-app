package com.myoptimind.suzuki_motors.di

import com.myoptimind.suzuki_motors.features.spare_parts.SparePartsRepository
import com.myoptimind.suzuki_motors.features.spare_parts.api.SparePartsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
class SparePartsModule{

    @ActivityRetainedScoped
    @Provides
    fun provideSparePartsService(
            retrofit: Retrofit
    ): SparePartsService {
        return retrofit.create(SparePartsService::class.java)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideServiceCenterRepository(
            sparePartsService: SparePartsService
    ): SparePartsRepository {
        return SparePartsRepository(sparePartsService)
    }

}