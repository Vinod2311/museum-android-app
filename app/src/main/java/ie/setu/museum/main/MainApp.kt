package ie.setu.museum.main

import android.app.Application
import ie.setu.museum.models.museum.MuseumStore
import ie.setu.museum.models.user.UserStore
import timber.log.Timber
import timber.log.Timber.Forest.i

class MainApp : Application() {

    lateinit var museums: MuseumStore
    lateinit var users: UserStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        //museums = MuseumJSONStore(applicationContext)
        //users = UserJSONStore(applicationContext)
        i("Museum app starting")
        //fillJSONStore()
    }
/*
    private fun fillJSONStore(){
        users.deleteAll()
        val user1 = UserModel(firstName = "Homer", lastName = "Simpson", email = "homer@simpson.com", password = "secret")
        val user2 = UserModel(firstName = "Marge", lastName = "Simpson", email = "marge@simpson.com", password = "secret")
        val user3 = UserModel(firstName = "Bart", lastName = "Simpson", email = "bart@simpson.com", password = "secret")
        val userList = listOf(user1,user2,user3)
        userList.forEach{users.create(it)}

        museums.deleteAll()
        val museum1 = MuseumModel(user = user1, name = "National Museum of Ireland - Natural History", category = "Natural History", rating = 2.5f,
            shortDescription = "Stuffed and mounted animals are displayed in their 19th-century grandeur in this throwback museum.",
            review = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
                    "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum " +
                    "dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum." ,
            lat =  53.34066, lng= -6.25301, zoom = 15f,
            image = arrayListOf<Uri>(
                Uri.parse("content://com.android.providers.media.documents/document/image%3A1000040647"),
                Uri.parse("content://com.android.providers.media.documents/document/image%3A1000040646"),
                Uri.parse("content://com.android.providers.media.documents/document/image%3A1000040645"),
                Uri.parse("content://com.android.providers.media.documents/document/image%3A1000040369")))
        val museum2 = MuseumModel(user = user1, name = "National Museum of Ireland - Archaeology", category = "History", rating = 3.5f,
            shortDescription = "Local archaeological finds from the Bronze Age, Vikings and medieval times with some Egyptian items.",
            review = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
                    "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum " +
                    "dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            lat = 53.34181, lng = -6.25464, zoom = 15f, image = arrayListOf(
                Uri.parse("content://com.android.providers.media.documents/document/image%3A1000040371"),
                Uri.parse("content://com.android.providers.media.documents/document/image%3A1000040650"),
                Uri.parse("content://com.android.providers.media.documents/document/image%3A1000040651")))
        val museum3 = MuseumModel(user=user1, name = "Irish Museum of Modern Art", category = "Art", rating = 4.5f,
            shortDescription = "Imposing building from 1684 housing permanent collection and temporary exhibitions of varied art.",
            review = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
                    "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum " +
                    "dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            lat = 53.34434, lng = -6.29982, zoom = 15f, image = arrayListOf(
                Uri.parse("content://com.android.providers.media.documents/document/image%3A1000040373"),
                Uri.parse("content://com.android.providers.media.documents/document/image%3A1000040653")))
        val museum4 = MuseumModel(user=user2, name = "National Maritime Museum of Ireland", category = "History", rating = 2.0f,
            shortDescription = "An array of exhibits on maritime history showcased in a church built for seafarers in 1837.",
            review = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
                    "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum " +
                    "dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            lat = 53.29720, lng = -6.13263, zoom = 15f, image = arrayListOf(
                Uri.parse("content://com.android.providers.media.documents/document/image%3A1000040657"),
                Uri.parse("content://com.android.providers.media.documents/document/image%3A1000040376"),
                Uri.parse("content://com.android.providers.media.documents/document/image%3A1000040656")))
        val museum5 = MuseumModel(user=user3, name = "The National 1798 Rebellion Centre", category = "History", rating = 3.0f,
            shortDescription = "Hands-on, multi-media exhibits & reenactments tracing the events of the 1798 Irish Rebellion.",
            review = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
                    "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum " +
                    "dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            lat = 52.55506, lng = -6.56233, zoom = 15f, image = arrayListOf(
                Uri.parse("content://com.android.providers.media.documents/document/image%3A1000040377"),
                Uri.parse("content://com.android.providers.media.documents/document/image%3A1000040659")))
        val museumList = listOf(museum1,museum2,museum3,museum4,museum5)
        museumList.forEach{museums.create(it)}



    }

 */
}