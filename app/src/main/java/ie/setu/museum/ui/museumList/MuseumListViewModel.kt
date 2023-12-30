package ie.setu.museum.ui.museumList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import ie.setu.museum.firebase.FirebaseDBManager
import ie.setu.museum.models.museum.MuseumModel
import timber.log.Timber

class MuseumListViewModel(application: Application) : AndroidViewModel(application) {


    private val museumList = MutableLiveData<List<MuseumModel>>().apply {value = ArrayList<MuseumModel>()}
    val observableMuseumList: LiveData<List<MuseumModel>>
        get() = museumList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var readOnly = MutableLiveData(false)

    private val favouritesList = MutableLiveData<ArrayList<MuseumModel>>().apply { value = ArrayList<MuseumModel>() }
    val observableFavouriteList: LiveData<ArrayList<MuseumModel>>
        get() =favouritesList

    var filterCategory = MutableLiveData<ArrayList<String>>().apply {value = ArrayList<String>() }

    var filteredMuseumList = MutableLiveData<ArrayList<MuseumModel>>().apply {value = ArrayList<MuseumModel>()}


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


}