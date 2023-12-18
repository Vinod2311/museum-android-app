package ie.setu.museum.ui.allMuseumLocations
/*
class AllMuseumLocationsPresenter (val view: AllMuseumLocationsView) {


    lateinit var app: MainApp

    fun initMap(map: GoogleMap) {
        map.uiSettings.isZoomControlsEnabled = true
        app = view.application as MainApp
        map.setOnMarkerClickListener(view)
        app.museums.findAll().forEach{
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.name).position(loc)
            map.addMarker(options)?.tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,8f))
            view.updateCard(it!!)
        }

    }

    fun doUpdateCard(marker: Marker) {
        val tag = marker.tag as Long
        val museum = app.museums.findById(tag)
        view.updateCard(museum!!)
    }

    fun doOnBackPressed() {
        view.finish()
    }

    fun doCancel() {
        view.finish()
    }



}

 */