package ie.setu.museum.views.allMuseumLocations


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso
import ie.setu.museum.R
import ie.setu.museum.databinding.ActivityAllMuseumLocationsViewBinding
import ie.setu.museum.models.museum.MuseumModel


class AllMuseumLocationsView : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityAllMuseumLocationsViewBinding
    //private lateinit var cardBinding: CardMuseumBinding
    lateinit var map: GoogleMap
    lateinit var presenter: AllMuseumLocationsPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AllMuseumLocationsPresenter(this)
        binding = ActivityAllMuseumLocationsViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        //cardBinding = CardMuseumBinding.inflate(layoutInflater)
        //cardBinding = CardMuseumBinding.bind(binding.root)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync {
            map = it
            presenter.initMap(map)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_secondary, menu)
        binding.cardView.editButton.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                presenter.doCancel()
            }

        }
        return super.onOptionsItemSelected(item)
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

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doUpdateCard(marker)
        return false
    }

    override fun onBackPressed() {
        presenter.doOnBackPressed()
        super.onBackPressed()
    }

    fun updateCard(museum: MuseumModel) {
        binding.cardView.name.text = museum!!.name
        binding.cardView.category.text = museum!!.category
        Picasso.get().load(museum.image[0]).into(binding.cardView.imageIcon)
    }
}