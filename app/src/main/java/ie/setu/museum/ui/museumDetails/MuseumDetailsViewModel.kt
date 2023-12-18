package ie.setu.museum.ui.museumDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.denzcoskun.imageslider.models.SlideModel
import ie.setu.museum.firebase.FirebaseDBManager
import ie.setu.museum.models.museum.MuseumModel
import timber.log.Timber

class MuseumDetailsViewModel : ViewModel() {

    private val museum = MutableLiveData<MuseumModel>()
    val observableMuseum: LiveData<MuseumModel>
        get() = museum

    val imageList = ArrayList<SlideModel>()

    fun getMuseum(museumId:String){
        try {
            FirebaseDBManager.findById(museumId,museum)
            Timber.i("Detail getMuseum Success: ${museum.value.toString()}")
        } catch (e: Exception){
            Timber.i("Detail getDonation Error: ${e.message}")
        }
    }
}