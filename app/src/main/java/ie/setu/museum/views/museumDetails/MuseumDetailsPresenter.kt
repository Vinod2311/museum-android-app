package ie.setu.museum.views.museumDetails

import android.net.Uri
import com.denzcoskun.imageslider.models.SlideModel
import ie.setu.museum.main.MainApp
import ie.setu.museum.models.museum.MuseumModel

class MuseumDetailsPresenter(private val view: MuseumDetailsView) {

    var museum = MuseumModel()
    var app: MainApp
    val imageList = ArrayList<SlideModel>()

    init{
        app = view.application as MainApp
        museum = view.intent.extras?.getParcelable("museum")!!

        if (museum.image[0] != Uri.EMPTY) {
            for (x in museum.image) {
                imageList.add(SlideModel(x.toString()))
            }
        }

        view.showMuseum(imageList,museum)
    }

    fun doCancel() {
        view.finish()
    }

}