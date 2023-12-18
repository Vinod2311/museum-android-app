package ie.setu.museum.ui.account

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseUser
import ie.setu.museum.R
import ie.setu.museum.databinding.FragmentAccountBinding
import ie.setu.museum.models.museum.MuseumModel
import ie.setu.museum.ui.auth.login.LoggedInViewModel
import ie.setu.museum.ui.auth.login.Login
import ie.setu.museum.utils.createLoader
import ie.setu.museum.utils.hideLoader
import ie.setu.museum.utils.showLoader
import timber.log.Timber

class AccountFragment : Fragment() {

    private lateinit var viewModel: AccountViewModel
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    //private val museumListViewModel: MuseumListViewModel by activityViewModels()
    private var _fragBinding: FragmentAccountBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var loader : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        loader = createLoader(requireActivity())
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentAccountBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        setupMenu()
        Timber.i("loggedinViewmodel: ${loggedInViewModel.liveFirebaseUser.value}")
        //Timber.i("museumListViewModel: ${museumListViewModel.observableMuseumList.value}")
        //fragBinding.userVM = viewModel
        /*
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null){
                viewModel.liveFirebaseUser.value = firebaseUser
            }
        })*/

        viewModel.observableStatus.observe(viewLifecycleOwner, Observer {
            updateLoader(it)
        })
        viewModel.observableMuseumList.observe(viewLifecycleOwner, Observer {
            render(loggedInViewModel.liveFirebaseUser.value!!,viewModel.observableMuseumList.value!!) })

        viewModel.loadMuseum(loggedInViewModel.liveFirebaseUser.value!!)


        fragBinding.editButton.setOnClickListener {
            showLoader(loader,"Updating User Details")
            viewModel.updateUser(loggedInViewModel.liveFirebaseUser, fragBinding.userFirstName.text.toString(),fragBinding.userLastName.text.toString())
        }
        return root
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_account, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                // Validate and handle the selected menu item
/*
                when (item.itemId) {

                    R.id.item_logout -> {
                        signOut()
                    }
                }
                //NavigationUI.onNavDestinationSelected(item,requireView().findNavController())

 */
                return NavigationUI.onNavDestinationSelected(item,requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    fun render(user: FirebaseUser, userMuseums: List<MuseumModel>){

            fragBinding.userFirstName.setText(user.displayName?.split(" ")!![0] ?: "N/A")
            fragBinding.userLastName.setText(user.displayName?.split(" ")!![1] ?:"N/A")
            fragBinding.userEmail.setText(user.email)
            fragBinding.totalMuseums.setText(userMuseums.size.toString())

    }

    fun updateLoader(status: Boolean){
        when (status) {
            true -> {
                hideLoader(loader)
                }
            false -> Toast.makeText(context,getString(R.string.addMuseumError), Toast.LENGTH_LONG).show()
        }
    }

    fun signOut() {
        loggedInViewModel.logOut()
        val intent = Intent(requireActivity(), Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


}