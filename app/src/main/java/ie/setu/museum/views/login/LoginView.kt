package ie.setu.museum.views.login

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
import ie.setu.museum.databinding.ActivityLoginViewBinding
import ie.setu.museum.views.signUp.SignUpView

class LoginView : AppCompatActivity() {

    lateinit var binding: ActivityLoginViewBinding
    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = LoginPresenter(this)
        val text = "Don't have an account? Sign up now!"
        val ss  = SpannableString(text)
        val clickableText: ClickableSpan = object: ClickableSpan(){

            override fun onClick(widget: View) {
                startActivity(Intent(this@LoginView, SignUpView::class.java))
                finish()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.setColor(Color.BLUE)
                ds.isUnderlineText=true
            }
        }
        ss.setSpan(clickableText, 23,30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val boldSpan = StyleSpan(Typeface.BOLD)
        ss.setSpan(boldSpan, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signupText.text = ss
        binding.signupText.movementMethod = LinkMovementMethod.getInstance()
        binding.signupText.highlightColor = Color.TRANSPARENT
        binding.login.setOnClickListener {
            presenter.doLogin(binding.emailText.text.toString(),binding.passwordText.text.toString())
        }

    }

    override fun onBackPressed() {
        startActivity(Intent(this@LoginView, SignUpView::class.java))
        finish()
        super.onBackPressed()
    }
}