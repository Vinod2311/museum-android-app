package ie.setu.museum.views.addMuseum

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.museum.R
import ie.setu.museum.databinding.ActivityAddMuseumBinding
import ie.setu.museum.models.MuseumModel
import timber.log.Timber

class AddMuseumView : AppCompatActivity() {

    private lateinit var binding: ActivityAddMuseumBinding
    private lateinit var presenter: AddMuseumPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMuseumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        val dropdownItems = resources.getStringArray(R.array.simple_items)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, dropdownItems)
        //val dropdownMenu = findViewById<AutoCompleteTextView>(R.id.category)
        //dropdownMenu.setAdapter(arrayAdapter)
        binding.category.setAdapter(arrayAdapter)
        presenter = AddMuseumPresenter(this)

        binding.chooseImage.setOnClickListener{
            presenter.cacheMuseum(binding.nameText.text.toString(),binding.shortDescriptionText.text.toString(),binding.category.text.toString(), binding.ratingBar.rating)
            presenter.doSelectImage()
        }

        binding.location.setOnClickListener{
            presenter.cacheMuseum(binding.nameText.text.toString(),binding.shortDescriptionText.text.toString(),binding.category.text.toString(), binding.ratingBar.rating)
            presenter.doSetLocation()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_secondary, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        deleteMenu.isVisible = presenter.edit
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save -> {
                if (binding.nameText.toString().isEmpty()) {
                    Snackbar.make(binding.root, "Please enter museum name", Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    presenter.cacheMuseum(binding.nameText.text.toString(), binding.shortDescriptionText.text.toString(),binding.category.text.toString(), binding.ratingBar.rating)
                    presenter.doAddOrSave(binding.nameText.text.toString(), binding.shortDescriptionText.text.toString(),binding.category.text.toString(), binding.ratingBar.rating)
                }
            }
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showMuseum(museum: MuseumModel) {
        binding.nameText.setText(museum.name)
        binding.shortDescriptionText.setText(museum.shortDescription)
        binding.category.setText(museum.category,false)
        binding.ratingBar.rating = museum.rating

        Picasso.get()
            .load(museum.image[0])
            .into(binding.imageView)
        if (museum.image != Uri.EMPTY) {
            binding.chooseImage.setText("Change Image")
        }
    }

    fun updateImage(image: Uri) {
        Timber.i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.imageView)
        binding.chooseImage.setText("Change Image")
    }


}