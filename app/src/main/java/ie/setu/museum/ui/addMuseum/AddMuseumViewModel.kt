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
        //set(value) {museum.value = value.value}

    private val museumImages = MutableLiveData<List<Uri>>()
    val observableMuseumImages: LiveData<List<Uri>>
        get() = museumImages


    //private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    //private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false;


    init {
        museum.value = MuseumModel()

    }

    fun getMuseum(userId:String, museumId: String) {
        FirebaseDBManager.findById(userId,museumId, museum)
    }

    fun getMuseumImages(userId:String, museumId: String) = runBlocking{
        FirebaseImageManager.downloadImageURLs(userId,museumId, museumImages)
    }

    fun deleteMuseumImages() = runBlocking {
        FirebaseImageManager.deleteAllMuseumImages(museum.value!!.uid!!)
        museumImages.value = arrayListOf<Uri>()
    }

    fun uploadMuseumImage(userId: String, uri: Uri) {
        FirebaseImageManager.uploadMuseumImageToFirebase(uri, museumImages, museum)
        //FirebaseDBManager.update(userId,museum.value!!)
    }

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
                //museum.value?.email = "test"
                Timber.i("museum is ${museum.value}")
                museum.value?.email = firebaseUser.value?.email
                FirebaseDBManager.create(firebaseUser,museum.value!!)
                true
            } catch (e: IllegalArgumentException) {
                false
            }
        }
    }
/*
    fun doSetLocation() {
        val location = Location(52.245696, -7.139102, 15f)
        if (museum.value!!.zoom != 0f) {
            location.lat =  museum.value!!.lat
            location.lng = museum.value!!.lng
            location.zoom = museum.value!!.zoom
        }
        val launcherIntent = Intent(getApplication(), EditLocationView::class.java)
            .putExtra("location", location)
            .putExtra("museum",museum.value!!)
        mapIntentLauncher.launch(launcherIntent)
    }

 */

    fun updateLocation(location: Location){
        museum.value!!.lat = location.lat
        museum.value!!.lng = location.lng
        museum.value!!.zoom = location.zoom
    }
/*
    private fun registerMapCallback() {
        mapIntentLauncher =registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            museum.lat = location.lat
                            museum.lng = location.lng
                            museum.zoom = location.zoom
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }


 */
/*
    fun cacheMuseum(name: String,description: String,category: String, review: String, rating: Float){
        museum.name = name
        museum.shortDescription = description
        museum.category = category
        museum.rating = rating
        museum.review = review
    }
*/

/*
    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data!!.clipData != null) {
                            Timber.i("Got Result ${result.data!!.clipData}")
                            var count:Int = result.data!!.clipData!!.itemCount
                            var currentItem = 0
                            museum.image.clear()
                            imageList.clear()
                            while (currentItem < count) {
                                museum.image.add(result.data!!.clipData!!.getItemAt(currentItem).uri)
                                view.contentResolver.takePersistableUriPermission(
                                    museum.image[currentItem],
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                                )
                                currentItem += 1
                            }
                            for (x in museum.image) {
                                imageList.add(SlideModel(x.toString(), "some text"))
                            }
                            view.updateImage(imageList)
                        } else if(result.data != null) {
                            Timber.i("Got Result ${result!!.data!!.data}")
                            museum.image.clear()
                            museum.image.add(result.data!!.data!!)
                            imageList.clear()
                            view.contentResolver.takePersistableUriPermission(
                                museum.image[0],
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                            )
                            imageList.add(SlideModel(museum.image[0].toString()))
                            view.updateImage(imageList)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }

 */
/*


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



    fun doCancel() {
        view.finish()
    }
    fun doDelete() {
        view.setResult(99)
        app.museums.delete(museum)
        view.finish()
    }





 */
}