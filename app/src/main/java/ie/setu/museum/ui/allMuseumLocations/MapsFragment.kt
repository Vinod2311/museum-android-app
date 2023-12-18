package ie.setu.museum.ui.allMuseumLocations

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.core.net.toUri
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import ie.setu.museum.R
import ie.setu.museum.databinding.FragmentMapsBinding
import ie.setu.museum.models.museum.MuseumModel
import ie.setu.museum.ui.auth.login.LoggedInViewModel
import ie.setu.museum.ui.museumList.MuseumListViewModel
import ie.setu.museum.utils.createLoader
import ie.setu.museum.utils.hideLoader
import ie.setu.museum.utils.showLoader

class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    private lateinit var mapsViewModel: MapsViewModel
    private val museumListViewModel: MuseumListViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private var _fragBinding: FragmentMapsBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var loader : AlertDialog
    private lateinit var toggleMuseums: SwitchCompat

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mapsViewModel.map = googleMap
        //val loc = LatLng(52.245696, -7.139102)
        mapsViewModel.map.isMyLocationEnabled = true
        mapsViewModel.map.uiSettings.isZoomControlsEnabled = true
        mapsViewModel.map.uiSettings.isMyLocationButtonEnabled = true

        mapsViewModel.currentLocation.observe(viewLifecycleOwner) {
            val currentLoc = LatLng(
                mapsViewModel.currentLocation.value!!.latitude,
                mapsViewModel.currentLocation.value!!.longitude
            )
            //mapsViewModel.map.addMarker(MarkerOptions().position(currentLoc).title("You are Here!"))
            //mapsViewModel.map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 14f))


        }

        museumListViewModel.observableMuseumList.observe(viewLifecycleOwner, Observer { museums ->
            museums?.let {
                render(museums as ArrayList<MuseumModel>)
                hideLoader(loader)
            }
        })


        mapsViewModel.map.setOnMarkerClickListener(this)

        //mapsViewModel.map.addMarker(MarkerOptions().position(loc).title("SETU"))

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentMapsBinding.inflate(inflater, container, false)

        val root = fragBinding.root

        loader = createLoader(requireActivity())
        mapsViewModel = ViewModelProvider(this)[MapsViewModel::class.java]
        setupMenu()
        /*
        museumListViewModel.observableMuseumList.observe(viewLifecycleOwner, Observer { museums ->
            museums?.let {
                render(museums as ArrayList<MuseumModel>)
                hideLoader(loader)
            }
        })

         */
        fragBinding.checkBoxFavourites.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                museumListViewModel.filterMuseumByFavourites()
            } else {
                if(toggleMuseums.isChecked){
                    museumListViewModel.loadAll()
                } else {
                    museumListViewModel.load()
                }
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_map, menu)

                val item = menu.findItem(R.id.toggleMuseums) as MenuItem
                item.setActionView(R.layout.togglebutton_layout)
                toggleMuseums = item.actionView!!.findViewById(R.id.toggleButton)
                toggleMuseums.isChecked = false

                toggleMuseums.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) museumListViewModel.loadAll()
                    else museumListViewModel.load()
                    if (museumListViewModel.observableFavouriteList.value?.size != 0) {
                        /*
                        for (favouriteMuseum in museumListViewModel.observableFavouriteList.value!!) {
                            for (museum in museumListViewModel.observableMuseumList.value!!)
                                if (museum.uid == favouriteMuseum?.uid) {
                                    binding.favouriteButton.setBackgroundResource(R.drawable.ic_favorite_red)
                                    museum.isFavourite = true
                                    break
                                } else {
                                    binding.favouriteButton.setBackgroundResource(R.drawable.ic_favorite_grey)
                                    museum.isFavourite = false
                                }
                        }

                         */
                    }
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }     }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(museumList: ArrayList<MuseumModel>){
        var markerColour : Float
        mapsViewModel.map.clear()
        if (!museumList.isEmpty()){
            museumList.forEach{
                for (favouritedMuseum in museumListViewModel.observableFavouriteList.value!!) {
                    if (favouritedMuseum.uid == it.uid){
                        it.isFavourite = true
                        break
                    } else {
                        it.isFavourite = false
                    }

                }
            markerColour = if(it.email.equals(this.museumListViewModel.liveFirebaseUser.value!!.email))
                BitmapDescriptorFactory.HUE_RED + 5
            else
                BitmapDescriptorFactory.HUE_AZURE
            mapsViewModel.map.addMarker(

                MarkerOptions().position(LatLng(it.lat, it.lng))
                    .title("${it.name}")
                    .snippet(it.category)
                    .icon(
                        BitmapDescriptorFactory.defaultMarker(markerColour))

            )?.tag = it
            }
        }
    }

    override fun onResume(){
        super.onResume()
        fragBinding.cardView.name.text = "Click a marker for more details!"
        showLoader(loader,"Downloading Museums")
        val currentLoc = LatLng(
            mapsViewModel.currentLocation.value!!.latitude,
            mapsViewModel.currentLocation.value!!.longitude
        )
        mapsViewModel.map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 7f))
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner) {
                firebaseUser -> if (firebaseUser != null) {
                museumListViewModel.liveFirebaseUser.value = firebaseUser
                museumListViewModel.load()
                }
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        //mapsViewModel.updateCard(marker)
        val museum = marker.tag as MuseumModel
        fragBinding.cardView.name.text = museum!!.name
        fragBinding.cardView.category.text = museum!!.category
        Picasso.get().load(museum.images!![0].toUri()).into(fragBinding.cardView.imageIcon)
        return false
    }

}
