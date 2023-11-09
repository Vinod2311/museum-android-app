package ie.setu.museum.views.addMuseum

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ie.setu.museum.helpers.showImagePicker
import ie.setu.museum.main.MainApp
import ie.setu.museum.models.Location
import ie.setu.museum.models.MuseumModel
import ie.setu.museum.views.editLocation.EditLocationView
import timber.log.Timber

class AddMuseumPresenter(private val view: AddMuseumView) {

    var museum = MuseumModel()
    var app: MainApp = view.application as MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false;

    init {
        if (view.intent.hasExtra("museum_edit")) {
            edit = true
            museum = view.intent.extras?.getParcelable("museum_edit")!!
            view.showMuseum(museum)
        }
         registerImagePickerCallback()
         registerMapCallback()
    }

    fun doAddOrSave(name: String,description: String,category: String, rating: Float ){
        museum.name = name
        museum.shortDescription = description
        museum.rating = rating
        museum.category = category
        if (edit) {
            app.museums.update(museum)
        } else {
            app.museums.create(museum)
        }
        view.setResult(RESULT_OK)
        view.finish()
    }

    fun doSelectImage() {
        showImagePicker(imageIntentLauncher,view)
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
        mapIntentLauncher.launch(launcherIntent)
    }

    fun cacheMuseum(name: String,description: String,category: String, rating: Float){
        museum.name = name
        museum.shortDescription = description
        museum.category = category
        museum.rating = rating
    }

    fun doCancel() {
        view.finish()
    }
    fun doDelete() {
        view.setResult(99)
        app.museums.delete(museum)
        view.finish()
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            museum.image = result.data!!.data!!
                            view.contentResolver.takePersistableUriPermission(museum.image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            view.updateImage(museum.image)
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
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
}