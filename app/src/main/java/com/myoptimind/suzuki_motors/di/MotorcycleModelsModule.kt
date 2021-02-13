package com.myoptimind.suzuki_motors.di

import com.myoptimind.suzuki_motors.features.motorcycle_models.MotorcycleModelsRepository
import com.myoptimind.suzuki_motors.features.motorcycle_models.api.MotorcycleModelsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
class MotorcycleModelsModule {

    @ActivityRetainedScoped
    @Provides
    fun provideMotorcycleModelsService(retrofit: Retrofit): MotorcycleModelsService {
        return retrofit.create(MotorcycleModelsService::class.java)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideMotorcycleModelsRepository(motorcycleModelsService: MotorcycleModelsService): MotorcycleModelsRepository {
        return MotorcycleModelsRepository(motorcycleModelsService)
    }
}