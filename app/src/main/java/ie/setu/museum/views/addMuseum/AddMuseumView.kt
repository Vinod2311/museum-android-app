package ie.setu.museum.views.addMuseum

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
        presenter = AddMuseumPresenter(this)

        binding.chooseImage.setOnClickListener{
            presenter.cacheMuseum(binding.name.text.toString(),binding.shortDescription.text.toString())
            presenter.doSelectImage()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_museum, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        deleteMenu.isVisible = presenter.edit
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save -> {
                if (binding.name.text.toString().isEmpty()) {
                    Snackbar.make(binding.root, "Please enter museum name", Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    presenter.cacheMuseum(binding.name.text.toString(), binding.shortDescription.text.toString())
                    presenter.doAddOrSave(binding.name.text.toString(), binding.shortDescription.text.toString())
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
        binding.name.setText(museum.name)
        binding.shortDescription.setText(museum.shortDescription)
        Picasso.get()
            .load(museum.image)
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