package ie.setu.museum.main

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import timber.log.Timber
import timber.log.Timber.Forest.i

class MainApp : Application() {


    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        checkDarkMode()
        i("Museum app starting")

    }
    private fun checkDarkMode() {
        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        val nightMode = sharedPreferences.getBoolean("night",false)

        if(nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}