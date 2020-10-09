package com.myoptimind.suzuki_app

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.soloader.SoLoader
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin;
import com.myoptimind.suzuki_app.shared.AppSharedPref
import com.myoptimind.suzuki_app.shared.FILE_NAME


@HiltAndroidApp
class SuzukiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        SoLoader.init(this, false)

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            val client = AndroidFlipperClient.getInstance(this)
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            client.addPlugin(
                    SharedPreferencesFlipperPlugin(this,FILE_NAME))
            client.start()
        }
    }
}