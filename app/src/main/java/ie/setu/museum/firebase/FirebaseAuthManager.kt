package ie.setu.museum.firebase

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import ie.setu.museum.R
import timber.log.Timber

class FirebaseAuthManager(application: Application) {

    private var application: Application? = null

    var firebaseAuth: FirebaseAuth? = null
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var loggedOut = MutableLiveData<Boolean>()
    var errorStatus = MutableLiveData<Boolean>()
    var googleSignInClient = MutableLiveData<GoogleSignInClient>()

    init {
        this.application = application
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth!!.currentUser != null) {
            liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
            loggedOut.postValue(false)
            errorStatus.postValue(false)
        }
        configureGoogleSignIn()
    }

    fun login(email: String?, password: String?) {
        firebaseAuth!!.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(application!!.mainExecutor) { task ->
                if (task.isSuccessful) {
                    //liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
                    liveFirebaseUser.value = firebaseAuth!!.currentUser
                    errorStatus.postValue(false)
                } else {
                    Timber.i("Login Failure: $task.exception!!.message")
                    errorStatus.postValue(true)
                }
            }
    }

    fun register(firstName:String?, lastName: String?, email: String?, password: String?) {
        val profileUpadates = userProfileChangeRequest{
            displayName = "$firstName $lastName"
        }
        firebaseAuth!!.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(application!!.mainExecutor) { task ->
                if (task.isSuccessful) {

                    errorStatus.postValue(false)
                    Timber.i("liveFirebaseUser is ${liveFirebaseUser.value}")
                    Timber.i("firebaseauth.currentuser is ${firebaseAuth!!.currentUser}")
                    firebaseAuth!!.currentUser!!.updateProfile(profileUpadates)
                        .addOnCompleteListener{task ->
                            liveFirebaseUser.value = firebaseAuth!!.currentUser
                            Timber.i("firebase.currentuser displayname is ${firebaseAuth!!.currentUser!!.displayName}")
                            Timber.i("liveFirebaseUser displayname is ${liveFirebaseUser.value?.displayName}")
                            Timber.i("User profile updated")}
                } else {
                    Timber.i("Registration Failure: $task.exception!!.message")
                    errorStatus.postValue(true)
                }
            }
    }

    fun logOut() {
        firebaseAuth!!.signOut()
        googleSignInClient.value!!.signOut()
        loggedOut.postValue(true)
        errorStatus.postValue(false)
    }

    private fun configureGoogleSignIn() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(application!!.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient.value = GoogleSignIn.getClient(application!!.applicationContext,gso)
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Timber.i( "DonationX firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(application!!.mainExecutor) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update with the signed-in user's information
                    Timber.i( "signInWithCredential:success")
                    liveFirebaseUser.postValue(firebaseAuth!!.currentUser)

                } else {
                    // If sign in fails, display a message to the user.
                    Timber.i( "signInWithCredential:failure $task.exception")
                    errorStatus.postValue(true)
                }
            }
    }
}