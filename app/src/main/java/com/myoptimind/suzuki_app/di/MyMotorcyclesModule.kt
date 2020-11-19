package com.myoptimind.suzuki_app.di

import com.myoptimind.suzuki_app.features.suzuki_diary.my_motorcycles.MyMotorcyclesRepository
import com.myoptimind.suzuki_app.features.suzuki_diary.my_motorcycles.api.MyMotorcyclesService
import com.myoptimind.suzuki_app.features.whats_new.WhatsNewRepository
import com.myoptimind.suzuki_app.features.whats_new.api.WhatsNewService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
class MyMotorcyclesModule {

    @ActivityRetainedScoped
    @Provides
    fun provideMyMotorcyclesService(
            retrofit: Retrofit
    ): MyMotorcyclesService {
        return retrofit.create(MyMotorcyclesService::class.java)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideMyMotorcyclesRepository(
            myMotorcycleService: MyMotorcyclesService
    ): MyMotorcyclesRepository {
        return MyMotorcyclesRepository(myMotorcycleService)
    }

}