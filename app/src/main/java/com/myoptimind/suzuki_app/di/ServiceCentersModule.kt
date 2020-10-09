package com.myoptimind.suzuki_app.di

import com.myoptimind.suzuki_app.features.service_centers.ServiceCentersRepository
import com.myoptimind.suzuki_app.features.service_centers.api.ServiceCentersService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
class ServiceCentersModule {

    @ActivityRetainedScoped
    @Provides
    fun provideServiceCentersService(
            retrofit: Retrofit
    ): ServiceCentersService {
        return retrofit.create(ServiceCentersService::class.java)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideServiceCenterRepository(
            serviceCentersService: ServiceCentersService
    ): ServiceCentersRepository {
        return ServiceCentersRepository(serviceCentersService)
    }

}