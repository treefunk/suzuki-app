package com.myoptimind.suzuki_app.di

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
class LoginModule {

    @ActivityRetainedScoped
    @Provides
    fun provideLoginService(retrofit: Retrofit): LoginService {
        return retrofit.create(LoginService::class.java)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideLoginRepository(loginService: LoginService): LoginRepository {
        return LoginRepository(loginService)
    }
}