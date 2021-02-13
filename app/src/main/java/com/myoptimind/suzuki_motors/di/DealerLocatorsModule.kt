package com.myoptimind.suzuki_motors.di

import com.myoptimind.suzuki_motors.features.dealer_locator.DealerLocatorsRepository
import com.myoptimind.suzuki_motors.features.dealer_locator.api.DealerLocatorService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
class DealerLocatorsModule {

    @ActivityRetainedScoped
    @Provides
    fun provideDealerLocatorsService(
            retrofit: Retrofit
    ): DealerLocatorService {
        return retrofit.create(DealerLocatorService::class.java)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideDealerLocatorsRepository(
            dealerLocatorsService: DealerLocatorService
    ): DealerLocatorsRepository {
        return DealerLocatorsRepository(dealerLocatorsService)
    }

}