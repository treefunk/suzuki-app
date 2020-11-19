package com.myoptimind.suzuki_app.di

import com.myoptimind.suzuki_app.features.safety_tips.SafetyTipsRepository
import com.myoptimind.suzuki_app.features.safety_tips.api.SafetyTipsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
class SafetyTipsModule{

    @ActivityRetainedScoped
    @Provides
    fun provideSafetyTipsService(
            retrofit: Retrofit
    ): SafetyTipsService {
        return retrofit.create(SafetyTipsService::class.java)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideServiceCenterRepository(
            safetyTipsService: SafetyTipsService
    ): SafetyTipsRepository {
        return SafetyTipsRepository(safetyTipsService)
    }

}