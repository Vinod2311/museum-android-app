package ie.setu.museum.ui.addMuseum

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import ie.setu.museum.firebase.FirebaseDBManager
import ie.setu.museum.firebase.FirebaseImageManager
import ie.setu.museum.models.museum.Location
import ie.setu.museum.models.museum.MuseumModel
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class AddMuseumViewModel(application: Application) : AndroidViewModel(application) {


    private val addMuseumStatus = MutableLiveData<Boolean>()
    val observableStatus: LiveData<Boolean>
        get() = addMuseumStatus

    private val museum = MutableLiveData<MuseumModel>()
    val observableMuseum: LiveData<MuseumModel>
        get() = museum


    private val museumImages = MutableLiveData<List<Uri>>()
    val observableMuseumImages: LiveData<List<Uri>>
        get() = museumImages


    var edit = false;


    init {
        museum.value = MuseumModel()

    }

    //Get a museum using userId and museumId
    fun getMuseum(userId:String, museumId: String) {
        FirebaseDBManager.findById(userId,museumId, museum)
    }

    /*
    fun getMuseumImages(userId:String, museumId: String) = runBlocking{
        FirebaseImageManager.downloadImageURLs(userId,museumId, museumImages)
    }

     */

    fun deleteMuseumImages() = runBlocking {
        FirebaseImageManager.deleteAllMuseumImages(museum.value!!.uid!!)
        museumImages.value = arrayListOf<Uri>()
    }

    fun uploadMuseumImage(userId: String, uri: Uri) {
        FirebaseImageManager.uploadMuseumImageToFirebase(uri, museumImages, museum)
        //FirebaseDBManager.update(userId,museum.value!!)
    }

    //Upload or update museum to firebase
    fun doAddOrSave(firebaseUser: MutableLiveData<FirebaseUser>){

        if (edit) {

            addMuseumStatus.value = try {
                FirebaseDBManager.update(firebaseUser.value!!.uid, museum.value!!)
                //FirebaseImageManager.uploadImageToFirebase(museum.value?.uid,)
                true
            } catch (e: IllegalArgumentException) {
                false
            }
        } else {
            addMuseumStatus.value = try {
                Timber.i("museum is ${museum.value}")
                museum.value?.email = firebaseUser.value?.email
                FirebaseDBManager.create(firebaseUser,museum.value!!)
                true
            } catch (e: IllegalArgumentException) {
                false
            }
        }
    }

    fun updateLocation(location: Location){
        museum.value!!.lat = location.lat
        museum.value!!.lng = location.lng
        museum.value!!.zoom = location.zoom
    }

/*
    fun cacheMuseum(name: String,description: String,category: String, review: String, rating: Float){
        museum.name = name
        museum.shortDescription = description
        museum.category = category
        museum.rating = rating
        museum.review = review
    }

    fun doSetLocation() {
        val location = Location(52.245696, -7.139102, 15f)
        if (museum.zoom != 0f) {
            location.lat =  museum.lat
            location.lng = museum.lng
            location.zoom = museum.zoom
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
            .putExtra("location", location)
            .putExtra("museum",museum)
        mapIntentLauncher.launch(launcherIntent)
    }

 */
}