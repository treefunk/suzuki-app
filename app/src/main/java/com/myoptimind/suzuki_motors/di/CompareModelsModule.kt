package com.myoptimind.suzuki_motors.di


import com.myoptimind.suzuki_motors.features.compare_motorcycles.CompareModelsRepository
import com.myoptimind.suzuki_motors.features.compare_motorcycles.api.CompareModelsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
class CompareModelsModule {

    @ActivityRetainedScoped
    @Provides
    fun provideCompareModelsService(
            retrofit: Retrofit
    ): CompareModelsService {
        return retrofit.create(CompareModelsService::class.java)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideCompareModelsRepository(
            compareModelsService: CompareModelsService
    ): CompareModelsRepository {
        return CompareModelsRepository(compareModelsService)
    }

}