package ie.setu.museum.views.addMuseum

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ie.setu.museum.helpers.showImagePicker
import ie.setu.museum.main.MainApp
import ie.setu.museum.models.MuseumModel
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
         // registerMapCallback()
    }

    fun doAddOrSave(name: String,description: String){
        museum.name = name
        museum.shortDescription = description
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

    fun cacheMuseum(name: String,description: String){
        museum.name = name
        museum.shortDescription = description
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
}