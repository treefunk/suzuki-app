package com.myoptimind.suzuki_app.di

import com.myoptimind.suzuki_app.features.home.HomeRepository
import com.myoptimind.suzuki_app.features.home.api.HomeService
import com.myoptimind.suzuki_app.features.login.LoginRepository
import com.myoptimind.suzuki_app.features.login.api.LoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
class HomeModule {

    @ActivityRetainedScoped
    @Provides
    fun provideHomeService(retrofit: Retrofit): HomeService {
        return retrofit.create(HomeService::class.java)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideHomeRepository(homeService: HomeService): HomeRepository {
        return HomeRepository(homeService)
    }
}