package ie.setu.museum.ui.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import ie.setu.museum.R
import ie.setu.museum.databinding.HomeBinding
import ie.setu.museum.databinding.NavHeaderBinding
import ie.setu.museum.firebase.FirebaseImageManager
import ie.setu.museum.ui.allMuseumLocations.MapsViewModel
import ie.setu.museum.ui.auth.login.LoggedInViewModel
import ie.setu.museum.ui.auth.login.Login
import ie.setu.museum.utils.checkLocationPermissions
import ie.setu.museum.utils.createLoader
import ie.setu.museum.utils.customTransformation
import ie.setu.museum.utils.hideLoader
import ie.setu.museum.utils.isPermissionGranted
import ie.setu.museum.utils.showLoader
import ie.setu.museum.utils.showProfileImagePicker
import timber.log.Timber

class Home : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding : HomeBinding
    private lateinit var navHeaderBinding : NavHeaderBinding
    private lateinit var headerView : View
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var loggedInViewModel : LoggedInViewModel
    private lateinit var intentLauncher : ActivityResultLauncher<Intent>
    private val mapsViewModel : MapsViewModel by viewModels()
    lateinit var loader : AlertDialog
    private lateinit var toggleDarkMode: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        loader = createLoader(this)
        drawerLayout = homeBinding.drawerLayout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.addMuseumFragment, R.id.museumListFragment), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val navView = homeBinding.navView
        navView.setupWithNavController(navController)
        initNavHeader()

        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val nightMode = sharedPreferences.getBoolean("night",false)

        val item = navView.menu.findItem(R.id.changeTheme) as MenuItem
        item.setActionView(R.layout.togglebutton_layout)
        toggleDarkMode = item.actionView!!.findViewById(R.id.toggleButton)

        if(nightMode) {
            toggleDarkMode.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        //else {
        //    toggleDarkMode.isChecked = false
        //    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        //}

        toggleDarkMode.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked) {
                editor.putBoolean("night",true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                //editor.clear()


            } else {
                editor.putBoolean("night",false)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                //editor.clear()


            }
            editor.commit()
        }

        if(checkLocationPermissions(this)) {
            mapsViewModel.updateCurrentLocation()
        }

    }

    public override fun onStart() {
        super.onStart()
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
            if (firebaseUser != null)
                updateNavHeader(loggedInViewModel.liveFirebaseUser.value!!)
        })

        loggedInViewModel.loggedOut.observe(this, Observer { loggedout ->
            if (loggedout) {
                startActivity(Intent(this, Login::class.java))
            }
        })
    registerProfileImagePickerCallback()
    }

    public override fun onResume() {
        super.onResume()
        loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
            if (firebaseUser != null)
                updateNavHeader(loggedInViewModel.liveFirebaseUser.value!!)
        })
    }

    private fun initNavHeader() {
        Timber.i("DX Init Nav Header")
        headerView = homeBinding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderBinding.bind(headerView)
        navHeaderBinding.navHeaderImage.setOnClickListener{
            //showLoader(loader, "Changing Profile Pic")
            showProfileImagePicker(intentLauncher)
        }
    }

    private fun updateNavHeader(currentUser: FirebaseUser) {
        navHeaderBinding.navHeaderName.text = currentUser.displayName
        navHeaderBinding.navHeaderEmail.text = currentUser.email
        if(currentUser.photoUrl != null) {
            Timber.i("photoURl: ${currentUser.photoUrl}")
            Picasso.get().load(currentUser.photoUrl)
                .resize(200, 200)
                .transform(customTransformation())
                .centerCrop()
                .into(navHeaderBinding.navHeaderImage)
        }
        hideLoader(loader)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun registerProfileImagePickerCallback() {
        intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        showLoader(loader, "Updating profile pic")
                        if (result.data != null) {
                            Timber.i("DX registerPickerCallback() ")
                            val uri: Uri = result!!.data!!.data!!
                            FirebaseImageManager
                                .updateUserImage(uri, loggedInViewModel.liveFirebaseUser)

                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (isPermissionGranted(requestCode, grantResults))
            mapsViewModel.updateCurrentLocation()
        else {
            // permissions denied, so use a default location
            mapsViewModel.currentLocation.value = Location("Default").apply {
                latitude = 52.245696
                longitude = -7.139102
            }
        }
        Timber.i("LOC : %s", mapsViewModel.currentLocation.value)
    }

    fun signOut(item: MenuItem) {
        loggedInViewModel.logOut()
        //Launch Login activity and clear the back stack to stop navigating back to the Home activity
        val intent = Intent(this, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


/*
    fun switchDarkMode(toggleDarkMode: MenuItem) {
        if(toggleDarkMode.isCheckedvbb ){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

 */
}