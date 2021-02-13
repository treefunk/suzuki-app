package com.myoptimind.suzuki_motors.di

import com.myoptimind.suzuki_motors.features.whats_new.WhatsNewRepository
import com.myoptimind.suzuki_motors.features.whats_new.api.WhatsNewService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
class WhatsNewModule {

    @ActivityRetainedScoped
    @Provides
    fun provideWhatsNewService(
            retrofit: Retrofit
    ): WhatsNewService {
        return retrofit.create(WhatsNewService::class.java)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideWhatsNewRepository(
            whatsNewService: WhatsNewService
    ): WhatsNewRepository {
        return WhatsNewRepository(whatsNewService)
    }

}