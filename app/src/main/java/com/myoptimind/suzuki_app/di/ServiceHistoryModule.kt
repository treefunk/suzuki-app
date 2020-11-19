package com.myoptimind.suzuki_app.di

import android.content.Context
import com.myoptimind.suzuki_app.db.SuzukiDB
import com.myoptimind.suzuki_app.features.shared.AppSharedPref
import com.myoptimind.suzuki_app.features.suzuki_diary.my_motorcycles.MyMotorcyclesRepository
import com.myoptimind.suzuki_app.features.suzuki_diary.my_motorcycles.api.MyMotorcyclesService
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.ServiceHistoryRepository
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.api.ServiceHistoryService
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.dao.ServiceHistoryDao
import com.myoptimind.suzuki_app.features.whats_new.WhatsNewRepository
import com.myoptimind.suzuki_app.features.whats_new.api.WhatsNewService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
class ServiceHistoryModule {

    @ActivityRetainedScoped
    @Provides
    fun serviceHistoryService(
            retrofit: Retrofit
    ): ServiceHistoryService {
        return retrofit.create(ServiceHistoryService::class.java)
    }

    @ActivityRetainedScoped
    @Provides
    fun serviceHistoryRepository(
            @ApplicationContext context: Context,
            serviceHistoryService: ServiceHistoryService,
            serviceHistoryDao: ServiceHistoryDao,
            appSharedPref: AppSharedPref,
            suzukiDB: SuzukiDB
    ): ServiceHistoryRepository {
        return ServiceHistoryRepository(context,serviceHistoryService,serviceHistoryDao,appSharedPref,suzukiDB)
    }


    @ActivityRetainedScoped
    @Provides
    fun serviceHistoryDao(
            suzukiDB: SuzukiDB
    ): ServiceHistoryDao {
        return suzukiDB.serviceHistoryDao()
    }

}