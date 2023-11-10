package ie.setu.museum.views.museumDetails

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import ie.setu.museum.R
import ie.setu.museum.databinding.ActivityMuseumDetailsBinding
import ie.setu.museum.models.MuseumModel

class MuseumDetailsView : AppCompatActivity() {

    private lateinit var binding: ActivityMuseumDetailsBinding
    private lateinit var presenter: MuseumDetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMuseumDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar )
        presenter = MuseumDetailsPresenter(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_secondary, menu)
        val saveMenu: MenuItem = menu.findItem(R.id.item_save)
        saveMenu.isVisible = false
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

    fun showMuseum(imageList: ArrayList<SlideModel>, museum : MuseumModel){
        binding.titlebar.text = museum.name
        binding.imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
        binding.descriptionText.text = museum.shortDescription
        binding.categoryText.text = museum.category
        binding.ratingBar.rating = museum.rating

    }
}