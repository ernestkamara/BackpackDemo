package io.kamara.backpackdemo

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class AppContext: Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}