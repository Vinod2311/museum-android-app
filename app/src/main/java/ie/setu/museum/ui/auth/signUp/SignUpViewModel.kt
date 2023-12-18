package ie.setu.museum.ui.auth.signUp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import ie.setu.museum.firebase.FirebaseAuthManager


class SignUpViewModel(app: Application) : AndroidViewModel(app) {
    var firebaseAuthManager : FirebaseAuthManager = FirebaseAuthManager(app)
    var liveFirebaseUser : MutableLiveData<FirebaseUser> = firebaseAuthManager.liveFirebaseUser

    fun register(firstName:String?, lastName:String?, email: String?, password: String?) {
        firebaseAuthManager.register(firstName,lastName, email, password)

    }
}