package ie.setu.museum.ui.addMuseum

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import ie.setu.museum.R
import ie.setu.museum.databinding.FragmentAddMuseumBinding
import ie.setu.museum.firebase.FirebaseImageManager
import ie.setu.museum.models.museum.Location
import ie.setu.museum.ui.auth.login.LoggedInViewModel
import ie.setu.museum.ui.editLocation.EditLocationView
import ie.setu.museum.utils.createLoader
import ie.setu.museum.utils.hideLoader
import ie.setu.museum.utils.showImagePicker
import ie.setu.museum.utils.showLoader
import timber.log.Timber

class AddMuseumFragment : Fragment() {

    private lateinit var viewModel: AddMuseumViewModel
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private var _fragBinding: FragmentAddMuseumBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val args by navArgs<AddMuseumFragmentArgs>()
    val imageList = ArrayList<SlideModel>()
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    lateinit var loader : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        loader = createLoader(requireActivity())
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentAddMuseumBinding.inflate(inflater, container, false)
        //_fragBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_museum,container,false)
        //fragBinding.lifecycleOwner = viewLifecycleOwner
        val root = fragBinding.root
        viewModel = ViewModelProvider(this).get(AddMuseumViewModel::class.java)

        fragBinding.museumVM = viewModel
        viewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                status -> status?.let { render(status) }})

        viewModel.observableMuseum.observe(viewLifecycleOwner, Observer {
            showMuseum()
        })
        //showLoader(loader,"Fetching Images")
        /*
        viewModel.observableMuseumImages.observe(viewLifecycleOwner, Observer {
            refreshImageSlider()
            //hideLoader(loader)
        })

         */

        val dropdownItems = resources.getStringArray(R.array.simple_items)
        val arrayAdapter = ArrayAdapter(requireActivity(), R.layout.dropdown_item, dropdownItems)
        fragBinding.category.setAdapter(arrayAdapter)

        registerImagePickerCallback()
        registerMapCallback()


        fragBinding.chooseImage.setOnClickListener{
            //viewModel.cacheMuseum(fragBinding.nameText.text.toString(),fragBinding.shortDescriptionText.text.toString(),fragBinding.category.text.toString(), fragBinding.reviewText.text.toString(), fragBinding.ratingBar.rating)
            doSelectImage()

        }

        fragBinding.location.setOnClickListener{
            //viewModel.cacheMuseum(fragBinding.nameText.text.toString(),fragBinding.shortDescriptionText.text.toString(),fragBinding.category.text.toString(), fragBinding.reviewText.text.toString(), fragBinding.ratingBar.rating)
            doSetLocation()
        }

        fragBinding.createButton.setOnClickListener{
            if (fragBinding.nameText.text.toString().isNotEmpty() && fragBinding.category.text.toString().isNotEmpty()){
                //viewModel.cacheMuseum(museum)
                viewModel.doAddOrSave(loggedInViewModel.liveFirebaseUser)
            } else if (fragBinding.nameText.text.toString().isEmpty()){
                fragBinding.name.error = "Museum name is required"

            } else if (fragBinding.category.text.toString().isEmpty()){
                fragBinding.categoryLayout.error = "Category is required"
            }

        }

        setupMenu()
        checkIfEdit()

        return root
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_add_museum, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun checkIfEdit() {
        if (args.museumId != "empty" ){
            viewModel.getMuseum(loggedInViewModel.liveFirebaseUser.value!!.uid, args.museumId!!)
            //viewModel.getMuseumImages(loggedInViewModel.liveFirebaseUser.value!!.uid, args.museumId!!)
            viewModel.edit = true
            fragBinding.createButton.setText(R.string.save_changes)
            fragBinding.name.isHelperTextEnabled = false
            fragBinding.categoryLayout.isHelperTextEnabled = false
        }
    }

    fun doSelectImage() {
        context?.let { showImagePicker(imageIntentLauncher, it) }
    }

    fun doSetLocation() {
        val location = Location(52.245696, -7.139102, 15f)
        if (viewModel.observableMuseum.value!!.zoom != 0f) {
            location.lat =  viewModel.observableMuseum.value!!.lat
            location.lng = viewModel.observableMuseum.value!!.lng
            location.zoom = viewModel.observableMuseum.value!!.zoom
        }
        val launcherIntent = Intent(requireActivity(), EditLocationView::class.java)
            .putExtra("location", location)
            .putExtra("museum",viewModel.observableMuseum.value!!)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun refreshImageSlider(){
        if (viewModel.observableMuseum.value?.images?.size != 0) {
            imageList.clear()
            hideLoader(loader)
            for (x in viewModel.observableMuseum.value?.images!!) {
                imageList.add(SlideModel(x))
            }
            //imageList.add(SlideModel(FirebaseImageManager.imageUri.value?.get(0).toString()))
            Timber.i("Images updated")
            fragBinding.imageView.setImageList(imageList, ScaleTypes.CENTER_CROP)
            fragBinding.chooseImage.setText(R.string.change_image)
            }

    }

    fun updateImage(imageList: ArrayList<SlideModel>) {
        Timber.i("Image updated")
        fragBinding.imageView.setImageList(imageList, ScaleTypes.CENTER_CROP)
        fragBinding.chooseImage.setText(R.string.change_image)
    }

    fun showMuseum() {
        fragBinding.museumVM = viewModel
        refreshImageSlider()
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    //findNavController().popBackStack()
                    val action = AddMuseumFragmentDirections.actionAddMuseumFragmentToMuseumListFragment()
                    findNavController().navigate(action)
                }
            }
            false -> Toast.makeText(context,getString(R.string.addMuseumError),Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    private fun registerImagePickerCallback(){
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (viewModel.edit) {
                            viewModel.deleteMuseumImages()
                        }
                        showLoader(loader,"Changing Image")
                        if (result.data!!.clipData != null) {
                            Timber.i("Got Result ${result.data!!.clipData}")
                            val count:Int = result.data!!.clipData!!.itemCount
                            var currentItem = 0
                            //viewModel.museum.value!!.image.clear()
                            //FirebaseImageManager.observableImageUri.value!!.clear()
                            while (currentItem < count) {
                                val uri:Uri = result.data!!.clipData!!.getItemAt(currentItem).uri!!
                                //val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(),uri)
                                //viewModel.museum.value!!.image.add(result.data!!.clipData!!.getItemAt(currentItem).uri!!.toString())
                                viewModel.uploadMuseumImage(loggedInViewModel.liveFirebaseUser.value!!.uid, uri)
                                //requireActivity().contentResolver.takePersistableUriPermission(
                                //    viewModel.museum.value!!.image[currentItem].toUri(),
                                //    Intent.FLAG_GRANT_READ_URI_PERMISSION
                                //)
                                currentItem += 1
                            }
                            //updateImage(imageList)
                        } else if(result.data != null) {
                            val uri:Uri = result!!.data!!.data!!
                            //val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(),uri)
                            Timber.i("Got Result ${result!!.data!!.data}")
                            //viewModel.museum.value!!.image.clear()
                            //viewModel.museum.value!!.image.add(uri.toString())
                            FirebaseImageManager.imageUri.value = listOf<Uri>()

                            Timber.i("imagelist: $imageList")
                            //requireActivity().contentResolver.takePersistableUriPermission(
                            //    viewModel.observableMuseum.value!!.image[0].toUri(),
                            //    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            //)
                            viewModel.uploadMuseumImage(loggedInViewModel.liveFirebaseUser.value!!.uid, uri)
                            //refreshImageSlider()
                            //imageList.add(SlideModel(viewModel.observableMuseum.value!!.image[0].toString()))
                            //updateImage(imageList)
                        }
                        //viewModel.getMuseumImages(loggedInViewModel.liveFirebaseUser.value!!.uid, args.museumId!!)
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            viewModel.updateLocation(location)

                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }



    override fun onResume() {
        super.onResume()


    }
    companion object {
        @JvmStatic
        fun newInstance() = AddMuseumFragment().apply{
            arguments = Bundle().apply {  }
        }
    }

}