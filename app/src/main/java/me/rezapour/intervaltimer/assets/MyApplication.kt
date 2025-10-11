package me.rezapour.intervaltimer.assets

import android.app.Application
import me.rezapour.di.CoreDi
import me.rezapour.di.FeatureDi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                CoreDi.modules + FeatureDi.modules
            )
        }
    }
}