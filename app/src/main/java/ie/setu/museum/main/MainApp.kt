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
        var museum1 = MuseumModel("National Museum of Ireland - Natural History","Natural History",
            "Stuffed and mounted animals are displayed in their 19th-century grandeur in this throwback museum.",
            lat =  53.34066, lng= -6.25301, zoom = 15f, image = arrayListOf<Uri>(Uri.parse("content://com.android.providers.media.documents/document/image%3A1000040369")))
        var museum2 = MuseumModel("National Museum of Ireland - Archaeology","History",
            "Local archaeological finds from the Bronze Age, Vikings and medieval times with some Egyptian items.",
            lat = 53.34181, lng = -6.25464, zoom = 15f, image = arrayListOf( Uri.parse("content://com.android.providers.media.documents/document/image%3A1000040371")))
        var museum3 = MuseumModel("Irish Museum of Modern Art","Art",
            "Imposing building from 1684 housing permanent collection and temporary exhibitions of varied art.",
            lat = 53.34434, lng = -6.29982, zoom = 15f, image = arrayListOf( Uri.parse("content://com.android.providers.media.documents/document/image%3A1000040373")))
        var museum4 = MuseumModel("National Maritime Museum of Ireland","History",
            "An array of exhibits on maritime history showcased in a church built for seafarers in 1837.",
            lat = 53.29720, lng = -6.13263, zoom = 15f, image = arrayListOf(Uri.parse("content://com.android.providers.media.documents/document/image%3A1000040376")))
        var museum5 = MuseumModel("The National 1798 Rebellion Centre","History",
            "Hands-on, multi-media exhibits & reenactments tracing the events of the 1798 Irish Rebellion.",
            lat = 52.55506, lng = -6.56233, zoom = 15f, image = arrayListOf(Uri.parse("content://com.android.providers.media.documents/document/image%3A1000040377")))
        val museumList = listOf(museum1,museum2,museum3,museum4,museum5)
        museumList.forEach{museums.create(it)}
    }
}