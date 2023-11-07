package ie.setu.museum.views.museumHome

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ie.setu.museum.main.MainApp
import ie.setu.museum.models.MuseumModel
import ie.setu.museum.views.addMuseum.AddMuseumView

class MuseumHomePresenter(private val view: MuseumHomeView) {

    var app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private var position: Int = 0

    init {
        app = view.application as MainApp
        registerMapCallback()
        registerRefreshCallback()
    }

    fun getMuseums() = app.museums.findAll()

    fun doAddMuseum() {
        val launchIntent = Intent(view, AddMuseumView::class.java)
        refreshIntentLauncher.launch(launchIntent)
    }

    fun doEditMuseum(museum: MuseumModel, pos: Int) {
        val launchIntent = Intent(view, AddMuseumView::class.java)
        launchIntent.putExtra("museum_edit", museum)
        position = pos
        refreshIntentLauncher.launch(launchIntent)
    }

    fun doShowMuseumsMap() {
        /*
        val launcherIntent = Intent(view, ::class.java)
        mapIntentLauncher.launch(launcherIntent)*/
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
}