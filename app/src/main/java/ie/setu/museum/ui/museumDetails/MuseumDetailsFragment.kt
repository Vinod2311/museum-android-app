package ie.setu.museum.ui.museumDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import ie.setu.museum.R
import ie.setu.museum.databinding.FragmentMuseumDetailsBinding
import ie.setu.museum.ui.auth.login.LoggedInViewModel

class MuseumDetailsFragment : Fragment() {

    private lateinit var viewModel: MuseumDetailsViewModel
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private var _fragBinding: FragmentMuseumDetailsBinding? = null
    private val fragBinding get() = _fragBinding!!
    val imageList = ArrayList<SlideModel>()
    private val args by navArgs<MuseumDetailsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(MuseumDetailsViewModel::class.java)
        _fragBinding = FragmentMuseumDetailsBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        fragBinding.museumDetailsVM = viewModel
        //Toast.makeText(context,"Donation ID Selected : ${args.museumId}",Toast.LENGTH_LONG).show()
        viewModel.observableMuseum.observe(viewLifecycleOwner, Observer {
            showMuseum() })
        viewModel.getMuseum(args.museumId!!)



        return root
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {

            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_museum_details, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    fun showMuseum(){
        fragBinding.museumDetailsVM = viewModel
        //requireView().findNavController().currentDestination?.label = viewModel.observableMuseum.value?.name
        if (viewModel.observableMuseum.value?.images?.size != 0) {
            imageList.clear()

            //hideLoader(loader)
            for (x in viewModel.observableMuseum.value?.images!!) {
                imageList.add(SlideModel(x))
            }
            //imageList.add(SlideModel(FirebaseImageManager.imageUri.value?.get(0).toString()))
            //Timber.i("Images updated")
            fragBinding.imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
    }


}