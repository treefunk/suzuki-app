package com.myoptimind.suzuki_motors.di

import android.content.Context
import com.myoptimind.suzuki_motors.features.login.LoginRepository
import com.myoptimind.suzuki_motors.features.login.api.LoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideLoginRepository(loginService: LoginService, @ApplicationContext context: Context): LoginRepository {
        return LoginRepository(loginService,context)
    }
}