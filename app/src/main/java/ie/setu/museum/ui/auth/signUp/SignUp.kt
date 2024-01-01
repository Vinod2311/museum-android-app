package ie.setu.museum.ui.auth.signUp

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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ie.setu.museum.databinding.ActivitySignUpViewBinding
import ie.setu.museum.ui.auth.login.Login
import ie.setu.museum.ui.home.Home
import ie.setu.museum.utils.createLoader
import ie.setu.museum.utils.showLoader
import timber.log.Timber

class SignUp : AppCompatActivity() {

    lateinit var binding: ActivitySignUpViewBinding
    private lateinit var signUpViewModel: SignUpViewModel
    lateinit var loader : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loader = createLoader(this)
        binding = ActivitySignUpViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpTextLinkToLogin()

        //Setup sign up(register) button on-click listener
        binding.signUp.setOnClickListener{
            if (binding.emailText.text.toString().isNotEmpty() && binding.passwordText.text.toString().isNotEmpty()){
                showLoader(loader,"Registering User")
                createAccount(binding.firstNameText.text.toString(),binding.lastNameText.text.toString(),binding.emailText.text.toString(),binding.passwordText.text.toString())
            } else if (binding.emailText.text.toString().isEmpty()){
                binding.email.error = "Email is required"

            } else if (binding.passwordText.text.toString().isEmpty()){
                binding.password.error = "Password is required"
            }
            }

    }

    public override fun onStart() {
        super.onStart()
        signUpViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        signUpViewModel.liveFirebaseUser.observe(this, Observer {
                firebaseUser -> if (firebaseUser != null)
            startActivity(Intent(this, Home::class.java))
        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this, Login::class.java))
        finish()
        super.onBackPressed()
    }

    private fun setUpTextLinkToLogin(){
        val text = "Already have an account. Login now!"
        val ss  = SpannableString(text)
        val clickableText: ClickableSpan = object: ClickableSpan(){

            override fun onClick(widget: View) {
                startActivity(Intent(this@SignUp, Login::class.java))
                finish()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE
                ds.isUnderlineText=true
            }
        }
        ss.setSpan(clickableText, 24,30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val boldSpan = StyleSpan(Typeface.BOLD)
        ss.setSpan(boldSpan, 24, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.loginText.text = ss
        binding.loginText.movementMethod = LinkMovementMethod.getInstance()
        binding.loginText.highlightColor = Color.TRANSPARENT
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

    private fun createAccount(firstName: String, lastName: String, email: String, password: String) {
        Timber.d("createAccount:$email")
        if (!validateForm()) { return }

        signUpViewModel.register(firstName,lastName,email,password)
        //startActivity(Intent(this, Home::class.java))
    }
}


