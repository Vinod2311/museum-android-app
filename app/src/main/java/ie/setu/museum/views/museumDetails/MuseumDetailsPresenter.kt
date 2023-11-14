package ie.setu.museum.views.museumDetails

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
        //val url = URL(museum.image.toString())
        for (x in museum.image) {
            imageList.add(SlideModel(x.toString(), "some text"))
        }
        view.showMuseum(imageList,museum)
    }

    fun doCancel() {
        view.finish()
    }

}