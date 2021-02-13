package com.myoptimind.suzuki_motors.di

import android.content.Context
import androidx.room.Room
import com.myoptimind.suzuki_motors.BuildConfig
import com.myoptimind.suzuki_motors.db.SuzukiDB
import com.myoptimind.suzuki_motors.features.shared.AppSharedPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().addHeader(
                        "x-api-key", BuildConfig.APIKEY
                ).build())
        }

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpClient.addInterceptor(loggingInterceptor)

        okHttpClient.apply {
            connectTimeout(60,TimeUnit.SECONDS)
            writeTimeout(60,TimeUnit.SECONDS)
            readTimeout(60,TimeUnit.SECONDS)
        }
//    https://suzukiapp.blitzworx.com/api/
//    http://suzuki.optimindsolutions.com/api/
//    http://suzuki.optimindsolutions.com/api/

        return Retrofit.Builder()
                .baseUrl("https://suzuki.optimindsolutions.com/api/")
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

    }


    @Singleton
    @Provides
    fun provideAppSharedPref(
            @ApplicationContext context: Context
    ): AppSharedPref {
        return AppSharedPref(context)
    }

    @Singleton
    @Provides
    fun provideDatabase(
            @ApplicationContext context: Context
    ): SuzukiDB {
        return Room.databaseBuilder(
                context,
                SuzukiDB::class.java,
                "suzuki-db"
        ).build()
    }

}