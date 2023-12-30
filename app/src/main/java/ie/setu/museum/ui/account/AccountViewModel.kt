package ie.setu.museum.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import ie.setu.museum.firebase.FirebaseDBManager
import ie.setu.museum.models.museum.MuseumModel
import timber.log.Timber

class AccountViewModel(application: Application) : AndroidViewModel(application) {

    //var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    private val updateUserStatus = MutableLiveData<Boolean>()
    val observableStatus: LiveData<Boolean>
        get() = updateUserStatus

    private val museumList = MutableLiveData<List<MuseumModel>>()
    val observableMuseumList: LiveData<List<MuseumModel>>
        get() = museumList

    init {

    }

    fun updateUser(currentUser: MutableLiveData<FirebaseUser>, firstName: String?, lastName: String?){
        val profileUpdates = userProfileChangeRequest {
            displayName = "$firstName $lastName"
        }
        Timber.i("firebaseuser is ${currentUser.value!!.displayName}")
        currentUser.value!!.updateProfile(profileUpdates)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    updateUserStatus.value = true
                    currentUser.value = currentUser.value
                    Timber.i("User updated")

                }
            }
    }

    fun loadMuseum(currentUser: FirebaseUser) {
        try {
            FirebaseDBManager.findUserMuseums(currentUser.uid!!, museumList)
            //FirebaseImageManager.downloadImageURLs(userId,museumId, museumImages)
            Timber.i("Report Load Success : ${museumList.value.toString()}")
        } catch (e: Exception){
            Timber.i("Report Load Error : $e.message")
        }

    }

}