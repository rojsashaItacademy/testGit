package ru.trinitydigital.pagingcashe

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.trinitydigital.pagingcashe.di.appModules

class PagingCasheApp : Application() {

    lateinit var s: String

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PagingCasheApp)
            modules(appModules)

        }
    }
}