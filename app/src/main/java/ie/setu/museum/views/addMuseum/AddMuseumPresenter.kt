package ie.setu.museum.views.addMuseum

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ie.setu.museum.helpers.showImagePicker
import ie.setu.museum.main.MainApp
import ie.setu.museum.models.museum.Location
import ie.setu.museum.models.museum.MuseumModel
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

    fun doAddOrSave(name: String,description: String,category: String, review: String, rating: Float ){
        museum.name = name
        museum.shortDescription = description
        museum.review = review
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

    fun cacheMuseum(name: String,description: String,category: String, review: String, rating: Float){
        museum.name = name
        museum.shortDescription = description
        museum.category = category
        museum.rating = rating
        museum.review = review
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
                        if (result.data!!.clipData != null) {
                            Timber.i("Got Result ${result.data!!.clipData}")
                            var count:Int = result.data!!.clipData!!.itemCount
                            var currentItem = 0
                            museum.image.clear()
                            while (currentItem < count) {
                                museum.image.add(result.data!!.clipData!!.getItemAt(currentItem).uri)
                                view.contentResolver.takePersistableUriPermission(
                                    museum.image[currentItem],
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                                )
                                currentItem += 1
                            }
                            view.updateImage(museum.image[0])
                        } else if(result.data != null) {
                            Timber.i("Got Result ${result!!.data!!.data}")
                            museum.image.clear()
                            museum.image.add(result.data!!.data!!)
                            val dummy = museum.image[0]
                            view.contentResolver.takePersistableUriPermission(
                                museum.image[0],
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                            )
                            view.updateImage(museum.image[0])
                        }
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