package ie.setu.museum.ui.museumList

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import ie.setu.museum.firebase.FirebaseDBManager
import ie.setu.museum.models.museum.MuseumModel
import timber.log.Timber

class MuseumListViewModel(application: Application) : AndroidViewModel(application) {

    //private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    //private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    private val museumList = MutableLiveData<List<MuseumModel>>().apply {value = ArrayList<MuseumModel>()}
    val observableMuseumList: LiveData<List<MuseumModel>>
        get() = museumList

    private val museumImages = MutableLiveData<List<Uri>>()
    val observableMuseumImages: LiveData<List<Uri>>
        get() = museumImages

    //private var position: Int = 0
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var readOnly = MutableLiveData(false)

    private val favouritesList = MutableLiveData<ArrayList<MuseumModel>>().apply { value = ArrayList<MuseumModel>() }
    val observableFavouriteList: LiveData<ArrayList<MuseumModel>>
        get() =favouritesList

    var filterCategory = MutableLiveData<ArrayList<String>>().apply {value = ArrayList<String>() }

    var filteredMuseumList = MutableLiveData<ArrayList<MuseumModel>>().apply {value = ArrayList<MuseumModel>()}


    init {

        //load()
        //registerMapCallback()
        //registerRefreshCallback()

    }

    fun load() {
        try {
            readOnly.value = false
            FirebaseDBManager.findUserMuseums(liveFirebaseUser.value?.uid!!, museumList)
            //FirebaseImageManager.downloadImageURLs(userId,museumId, museumImages)
            Timber.i("Report Load Success : ${museumList.value.toString()}")
        } catch (e: Exception){
            Timber.i("Report Load Error : $e.message")
        }

    }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAll(museumList)
            Timber.i("Report LoadAll Success : ${museumList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report LoadAll Error : $e.message")
        }
    }

    fun filterMuseumByCategory() {
        val localList = ArrayList<MuseumModel>()
        filterCategory.value?.forEach{category ->
            museumList.value?.forEach { museum ->
                        if(museum.category == category)
                        localList.add(museum)
            }
        }
        filteredMuseumList.value = localList
    }

    fun delete(userid: String, id: String) {
        try {
            FirebaseDBManager.delete(userid,id)
            Timber.i("Report Delete Success")
        }
        catch (e: java.lang.Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }

    fun filterMuseumByFavourites() {
        //if (favouritesList.value?.size != 0) {
            val localSet = museumList.value?.intersect(favouritesList.value!!)
            val localList = ArrayList(localSet)
            museumList.value = localList as ArrayList<MuseumModel>?
        //}
    }

    //fun getMuseums() = app.museums.findAll()
    /*
        fun doFilterList(query:String?): ArrayList<MuseumModel>{
            val filteredList = ArrayList<MuseumModel>()
            if (query != null){
                val searchText = query.lowercase(Locale.ROOT)
                for (i in app.museums.findAll()){
                    if(i.name.lowercase(Locale.ROOT).contains(searchText)){
                        filteredList.add(i)
                    }
                }
            }
            return filteredList
        }
    */


/*
    fun doEditMuseum(museum: MuseumModel, pos: Int) {
        val launchIntent = Intent(view, AddMuseumView::class.java)
        launchIntent.putExtra("museum_edit", museum)
        loggedInUser= view.intent.extras?.getParcelable("user")!!
        launchIntent.putExtra("user",loggedInUser)
        position = pos
        refreshIntentLauncher.launch(launchIntent)
    }

    fun doShowMuseum(museum: MuseumModel) {
        val launchIntent = Intent(view, MuseumDetailsView::class.java)
        launchIntent.putExtra("museum", museum)
        refreshIntentLauncher.launch(launchIntent)
    }

    fun doShowAccount(){
        val launchIntent = Intent(view, AccountView::class.java)
        loggedInUser= view.intent.extras?.getParcelable("user")!!
        launchIntent.putExtra("user",loggedInUser)
        refreshIntentLauncher.launch(launchIntent)
    }

    fun doShowMuseumsMap() {

        val launcherIntent = Intent(view, AllMuseumLocationsView::class.java)
        mapIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK) view.onRefresh()
                else // Deleting
                    if (it.resultCode == 99) view.onDelete(position)
            }
    }
    private fun registerMapCallback() {
        mapIntentLauncher = view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {  }
    }

 */
}