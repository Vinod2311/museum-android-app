package ie.setu.museum.ui.auth.login

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import ie.setu.museum.R
import ie.setu.museum.databinding.ActivityLoginViewBinding
import ie.setu.museum.ui.auth.signUp.SignUp
import ie.setu.museum.ui.home.Home
import ie.setu.museum.utils.createLoader
import ie.setu.museum.utils.showLoader
import timber.log.Timber

class Login : AppCompatActivity() {

    lateinit var binding: ActivityLoginViewBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var startForResult : ActivityResultLauncher<Intent>
    lateinit var callbackManager : CallbackManager
    lateinit var loader : AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loader = createLoader(this)
        binding = ActivityLoginViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpTextLinkToSignUp()

        //Setup facebook callback manager
        callbackManager = CallbackManager.Factory.create();

        //Setup Google SignIn button style
        binding.googleSignInButton.setSize(SignInButton.SIZE_WIDE)
        binding.googleSignInButton.setColorScheme(0)

        //Setup up sign in button on click listener
        binding.login.setOnClickListener {
            //showLoader(loader,"Logging In User.")
            signIn(binding.emailText.text.toString(), binding.passwordText.text.toString())
        }
    }

    public override fun onStart() {
        super.onStart()

        //Setup viewModel
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        //Setup Observers
        loginViewModel.liveFirebaseUser.observe(this, Observer {
            firebaseUser -> if (firebaseUser != null)
                startActivity(Intent(this, Home::class.java))
        })
        loginViewModel.firebaseAuthManager.errorStatus.observe(this, Observer {
                status -> checkStatus(status)
        })

        //Setup Facebook sign in permissions and callback
        binding.buttonFacebookLogin.setReadPermissions("email", "public_profile")
        binding.buttonFacebookLogin.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    showLoader(loader,"Logging In User.")
                    Timber.i( "facebook:onSuccess:$loginResult")
                    loginViewModel.authWithFacebook(loginResult.accessToken)
                }
                override fun onCancel() {
                    Timber.i( "facebook:onCancel")
                }
                override fun onError(error: FacebookException) {
                    Timber.i( "facebook:onError:$error")
                }
            }
        )


        //Setup Google sign-in callback and on-click listener
        setupGoogleSignInCallback()
        binding.googleSignInButton.setOnClickListener {
            showLoader(loader,"Logging In User.")
            googleSignIn() }
    }

    private fun setUpTextLinkToSignUp(){
        val text = "Don't have an account? Sign up now!"
        val ss  = SpannableString(text)
        val clickableText: ClickableSpan = object: ClickableSpan(){

            override fun onClick(widget: View) {
                startActivity(Intent(this@Login, SignUp::class.java))
                finish()
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE
                ds.isUnderlineText=true
            }
        }
        ss.setSpan(clickableText, 23,30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val boldSpan = StyleSpan(Typeface.BOLD)
        ss.setSpan(boldSpan, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signupText.text = ss
        binding.signupText.movementMethod = LinkMovementMethod.getInstance()
        binding.signupText.highlightColor = Color.TRANSPARENT
    }


    private fun signIn(email: String, password: String) {
        Timber.d("signIn:$email")
        if (!validateForm()) {

            return }
        showLoader(loader,"Logging In User.")
        loginViewModel.login(email,password)
    }

    private fun validateForm(): Boolean {
        var valid = true
        val email = binding.emailText.text.toString()
        if (TextUtils.isEmpty(email)) {
            binding.email.error = "Email is required"
            valid = false
        } else {
            binding.email.error = null
        }

        val password = binding.passwordText.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.password.error = "Password is Required."
            valid = false
        } else {
            binding.password.error = null
        }
        return valid
    }

    //Display error to user if authorisation has failed
    private fun checkStatus(error:Boolean) {
        if (error)
            Toast.makeText(this,
                getString(R.string.auth_failed),
                Toast.LENGTH_LONG).show()
    }



    override fun onBackPressed() {
        startActivity(Intent(this@Login, SignUp::class.java))
        finish()
        super.onBackPressed()
    }

    private fun googleSignIn() {
        val signInIntent = loginViewModel.firebaseAuthManager
            .googleSignInClient.value!!.signInIntent

        startForResult.launch(signInIntent)
    }

    private fun setupGoogleSignInCallback() {
        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            val account = task.getResult(ApiException::class.java)
                            loginViewModel.authWithGoogle(account!!)
                        } catch (e: ApiException) {
                            // Google Sign In failed
                            Timber.i( "Google sign in failed $e")
                            Snackbar.make(binding.root, "Authentication Failed.",
                                Snackbar.LENGTH_SHORT).show()
                        }
                        Timber.i("DonationX Google Result $result.data")
                    }
                    RESULT_CANCELED -> {

                    } else -> { }
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }


}