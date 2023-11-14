package ie.setu.museum.views.museumHome

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ie.setu.museum.R
import ie.setu.museum.databinding.ActivityMuseumHomeViewBinding
import ie.setu.museum.models.museum.MuseumModel
import timber.log.Timber


class MuseumHomeView : AppCompatActivity(), MuseumListener {

    lateinit var binding: ActivityMuseumHomeViewBinding
    lateinit var presenter: MuseumHomePresenter
    var position: Int = 0
    private lateinit var museumAdapter: MuseumAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMuseumHomeViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        Timber.i("Museum Home Activity started..")
        presenter = MuseumHomePresenter(this)
        val layoutManager = LinearLayoutManager(this)


        binding.recyclerView.layoutManager = layoutManager
        museumAdapter = MuseumAdapter(presenter.getMuseums(), this)
        binding.recyclerView.adapter = museumAdapter
        onRefresh()


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(query: String?): Boolean {
                museumAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                museumAdapter.filter.filter(query)
                return false
            }})

        binding.addButton.setOnClickListener{
            presenter.doAddMuseum()
            onRefresh()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.map -> {
                presenter.doShowMuseumsMap()
            }
            R.id.account -> {
                presenter.doShowAccount()
            }
        }
        return super.onOptionsItemSelected(item)
    }



    fun onDelete(position : Int) {
        binding.recyclerView.adapter?.notifyItemRemoved(position)
    }

/*
    private fun loadMuseums() {
        binding.recyclerView.adapter = MuseumAdapter(presenter.getMuseums(), this)
        onRefresh()
    }
*/
    fun onRefresh() {
        museumAdapter.
        notifyItemRangeChanged(0,presenter.getMuseums().size)
    }

    fun showFilteredList(museums:ArrayList<MuseumModel>){
        if (museums.isEmpty()){
            Snackbar.make(binding.root, "No results", Snackbar.LENGTH_LONG).show()
            binding.recyclerView.adapter = MuseumAdapter(museums, this)
        }else{
        binding.recyclerView. adapter = MuseumAdapter(museums, this)}
        binding.recyclerView.adapter?.
        notifyItemRangeChanged(0,museums.size)

    }


    override fun onEditMuseumClick(museum: MuseumModel, position: Int) {
        this.position = position
        presenter.doEditMuseum(museum,this.position)
    }

    override fun onMuseumClick(museum: MuseumModel) {
        presenter.doShowMuseum(museum)
    }


}