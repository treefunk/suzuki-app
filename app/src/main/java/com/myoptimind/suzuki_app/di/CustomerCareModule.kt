package com.myoptimind.suzuki_app.di

import com.myoptimind.suzuki_app.features.customer_care.CustomerCareRepository
import com.myoptimind.suzuki_app.features.customer_care.api.CustomerCareService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
class CustomerCareModule {

    @ActivityRetainedScoped
    @Provides
    fun provideCustomerCareService(
            retrofit: Retrofit
    ): CustomerCareService {
        return retrofit.create(CustomerCareService::class.java)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideCustomerCareRepository(
            whatsNewService: CustomerCareService
    ): CustomerCareRepository {
        return CustomerCareRepository(whatsNewService)
    }

}