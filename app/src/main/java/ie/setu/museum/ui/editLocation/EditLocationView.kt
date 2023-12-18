package ie.setu.museum.ui.editLocation


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import ie.setu.museum.R
import ie.setu.museum.databinding.ActivityEditLocationPageBinding
import ie.setu.placemark.museum.editLocation.EditLocationPresenter

class EditLocationView : AppCompatActivity(),
    GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    lateinit var presenter: EditLocationPresenter
    lateinit var binding: ActivityEditLocationPageBinding
    //var location = Location()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditLocationPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = EditLocationPresenter(this)
        setSupportActionBar(binding.toolbar)
        binding.mapView.onCreate(savedInstanceState)

       binding.mapView.getMapAsync{
           map = it
           presenter.initMap(map)
       }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit_location, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_confirm -> {
                presenter.doConfirm()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMarkerDragStart(marker: Marker) {
    }

    override fun onMarkerDrag(marker: Marker) {
    }

    override fun onMarkerDragEnd(marker: Marker) {
        presenter.doUpdateLocation(marker.position.latitude,marker.position.longitude, map.cameraPosition.zoom)
        presenter.doUpdateMarker(marker)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doUpdateMarker(marker)
        return false
    }

    override fun onBackPressed() {
        presenter.doOnBackPressed()
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}