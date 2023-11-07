package ie.setu.museum.main

import android.app.Application
import android.net.Uri

import ie.setu.museum.models.MuseumJSONStore
import ie.setu.museum.models.MuseumModel
import ie.setu.museum.models.MuseumStore
import timber.log.Timber
import timber.log.Timber.Forest.i

class MainApp : Application() {

    lateinit var museums: MuseumStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        museums = MuseumJSONStore(applicationContext)
        i("Museum app starting")
        fillJSONStore()
    }

    private fun fillJSONStore(){
        museums.deleteAll()
        var museum1 = MuseumModel("name 1","History","This is a short description", Uri.EMPTY,0)
        var museum2 = MuseumModel("name 2","Science","This is a short description", Uri.EMPTY,1)
        var museum3 = MuseumModel("name 3","Art","This is a short description", Uri.EMPTY,2)
        museums.create(museum1)
        museums.create(museum2)
        museums.create(museum3)
    }
}