package uz.gita.onlinecontacts.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import uz.gita.onlinecontacts.data.local.SharedPref

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SharedPref.init(this)
        instance = this

    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: Context
            private set
    }

}