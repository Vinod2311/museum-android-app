package ie.setu.museum.views.museumHome

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.museum.R
import ie.setu.museum.databinding.ActivityMuseumHomeViewBinding
import ie.setu.museum.models.MuseumModel
import timber.log.Timber


class MuseumHomeView : AppCompatActivity(), MuseumListener {

    private lateinit var binding: ActivityMuseumHomeViewBinding
    lateinit var presenter: MuseumHomePresenter
    var position: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMuseumHomeViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        presenter = MuseumHomePresenter(this)
        Timber.i("Museum Home Activity started..")

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadMuseums()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                presenter.doAddMuseum()
            }
            R.id.map -> { }
            R.id.account -> { }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onDelete(position : Int) {
        binding.recyclerView.adapter?.notifyItemRemoved(position)
    }


    private fun loadMuseums() {
        binding.recyclerView.adapter = MuseumAdapter(presenter.getMuseums(), this)
        onRefresh()
    }

    fun onRefresh() {
        binding.recyclerView.adapter?.
        notifyItemRangeChanged(0,presenter.getMuseums().size)
    }

    override fun onMuseumClick(museum: MuseumModel, position: Int) {
        this.position = position
        presenter.doEditMuseum(museum,this.position)
    }


}