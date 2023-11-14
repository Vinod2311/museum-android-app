package ie.setu.museum.views.signUp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ie.setu.museum.databinding.ActivitySignUpViewBinding
import ie.setu.museum.views.login.LoginView

class SignUpView : AppCompatActivity() {

    lateinit var binding: ActivitySignUpViewBinding
    lateinit var presenter : SignUpPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = SignUpPresenter(this)
        val text = "Already have an account. Login now!"
        val ss  = SpannableString(text)
        val clickableText: ClickableSpan = object: ClickableSpan(){

            override fun onClick(widget: View) {
                startActivity(Intent(this@SignUpView, LoginView::class.java))
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

        binding.signUp.setOnClickListener{
            if (binding.emailText.text.toString().isNotEmpty() && binding.passwordText.text.toString().isNotEmpty()){
                presenter.doAddUser(binding.firstNameText.text.toString(),binding.lastNameText.text.toString(),binding.emailText.text.toString(),binding.passwordText.text.toString())
            } else if (binding.emailText.text.toString().isEmpty()){
                binding.email.error = "Email is required"

            } else if (binding.passwordText.text.toString().isEmpty()){
                binding.password.error = "Password is required"
            }
            }

    }

    override fun onBackPressed() {
        startActivity(Intent(this, LoginView::class.java))
        finish()
        super.onBackPressed()
    }
}


