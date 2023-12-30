package ie.setu.museum.ui.museumList

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.museum.R
import ie.setu.museum.databinding.FragmentMuseumListBinding
import ie.setu.museum.models.museum.MuseumModel
import ie.setu.museum.ui.auth.login.LoggedInViewModel
import ie.setu.museum.utils.SwipeToDeleteCallback
import ie.setu.museum.utils.SwipeToEditCallback
import ie.setu.museum.utils.createLoader
import ie.setu.museum.utils.hideLoader
import ie.setu.museum.utils.showLoader

class MuseumListFragment : Fragment(), MuseumListener {

    private lateinit var viewModel: MuseumListViewModel
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    var position: Int = 0
    private var _fragBinding: FragmentMuseumListBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var museumAdapter: MuseumAdapter
    lateinit var loader : AlertDialog
    private lateinit var toggleMuseums: SwitchCompat


    override fun onCreate(savedInstanceState: Bundle?) {
        loader = createLoader(requireActivity())
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentMuseumListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        viewModel = ViewModelProvider(requireActivity()).get(MuseumListViewModel::class.java)
        setupMenu()
        setupChips()
        //showLoader(loader,"Downloading Museums")

        viewModel.observableMuseumList.observe(viewLifecycleOwner, Observer {
            viewModel.filterMuseumByCategory()
        })

        viewModel.filteredMuseumList.observe(viewLifecycleOwner, Observer {
            if (viewModel.filterCategory.value?.size != 0)
                render(viewModel.filteredMuseumList.value!!)
            else{
                render(viewModel.observableMuseumList.value as ArrayList<MuseumModel>)
            }

            checkSwipeRefresh()
        })

        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                viewModel.liveFirebaseUser.value = firebaseUser
                viewModel.load()
            }
        })


        val layoutManager = LinearLayoutManager(requireContext())
        fragBinding.recyclerView.layoutManager = layoutManager
        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = fragBinding.recyclerView.adapter as MuseumAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                viewModel.delete(viewModel.liveFirebaseUser.value?.uid!!,
                    (viewHolder.itemView.tag as MuseumModel).uid!!)

            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onEditMuseumClick(viewHolder.itemView.tag as MuseumModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)



        fragBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(query: String?): Boolean {
                 if(::museumAdapter.isInitialized)
                     museumAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                museumAdapter.filter.filter(query)
                return false
            }})

        fragBinding.addButton.setOnClickListener{
            val action = MuseumListFragmentDirections.actionMuseumListFragmentToAddMuseumFragment("empty")
            findNavController().navigate(action)
        }


        return root
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_museum_list, menu)
                val item = menu.findItem(R.id.toggleMuseums) as MenuItem
                item.setActionView(R.layout.togglebutton_layout)
                toggleMuseums = item.actionView!!.findViewById(R.id.toggleButton)
                toggleMuseums.isChecked = false
                (activity as? AppCompatActivity)?.supportActionBar?.title = "My Museums"

                toggleMuseums.setOnCheckedChangeListener { _, isChecked ->
                    showLoader(loader,"Getting Museums")
                    if (isChecked) {
                        viewModel.loadAll()
                        fragBinding.favourites.isChecked = false
                        (activity as? AppCompatActivity)?.supportActionBar?.title = "All Museums"
                    }
                    else {
                        viewModel.load()
                        fragBinding.favourites.isChecked = false
                        (activity as? AppCompatActivity)?.supportActionBar?.title = "My Museums"
                    }

                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupChips(){
        fragBinding.favourites.setOnClickListener{
            if(fragBinding.favourites.isChecked) {
                viewModel.filterMuseumByFavourites()
            } else{
                if (toggleMuseums.isChecked)
                    viewModel.loadAll()
                else
                    viewModel.load()
            }
        }
        fragBinding.naturalHistoryChip.setOnClickListener{
            if(fragBinding.naturalHistoryChip.isChecked) {
                viewModel.filterCategory.value?.add("Natural History")
            } else{
                viewModel.filterCategory.value?.remove("Natural History")
            }
            viewModel.filterMuseumByCategory()
        }
        fragBinding.historyChip.setOnClickListener{
            if(fragBinding.historyChip.isChecked) {
                viewModel.filterCategory.value?.add("History")
            } else{
                viewModel.filterCategory.value?.remove("History")
            }
            viewModel.filterMuseumByCategory()
        }
        fragBinding.scienceChip.setOnClickListener{
            if(fragBinding.scienceChip.isChecked) {
                viewModel.filterCategory.value?.add("Science")
            } else{
                viewModel.filterCategory.value?.remove("Science")
            }
            viewModel.filterMuseumByCategory()
        }
        fragBinding.artChip.setOnClickListener{
            if(fragBinding.artChip.isChecked) {
                viewModel.filterCategory.value?.add("Art")
            } else{
                viewModel.filterCategory.value?.remove("Art")
            }
            viewModel.filterMuseumByCategory()
        }
    }

    private fun render(museumList: ArrayList<MuseumModel>) {
        hideLoader(loader)
        museumAdapter = MuseumAdapter(museumList,viewModel.observableFavouriteList.value as ArrayList,this,viewModel.readOnly.value!!)
        fragBinding.recyclerView.adapter = museumAdapter
        //onRefresh()
        if (museumList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.museumsNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.museumsNotFound.visibility = View.GONE
        }
    }

    fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            showLoader(loader,"Downloading Museums")
            if(viewModel.readOnly.value!!){
                viewModel.loadAll()
            } else {
                viewModel.load()
            }

        }
    }

    fun checkSwipeRefresh() {
        if (fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
        showLoader(loader,"Downloading Museums")
        if (viewModel.observableMuseumList.value?.size != 0)
        {
            render(viewModel.observableMuseumList.value as ArrayList<MuseumModel>)
        }


    }

    fun onRefresh() {
        museumAdapter.
        notifyItemRangeChanged(0,viewModel.observableMuseumList.value!!.size)
    }

    fun onDelete(position : Int) {
        fragBinding.recyclerView.adapter?.notifyItemRemoved(position)
    }

    override fun onMuseumClick(museum: MuseumModel) {
        //if(!viewModel.readOnly.value!!){
            val action = MuseumListFragmentDirections.actionMuseumListFragmentToMuseumDetailsFragment(museum.uid)
            findNavController().navigate(action)
       // }

    }


    override fun onEditMuseumClick(museum: MuseumModel) {
        val action = MuseumListFragmentDirections.actionMuseumListFragmentToAddMuseumFragment(museum.uid)
        findNavController().navigate(action)
    }


}