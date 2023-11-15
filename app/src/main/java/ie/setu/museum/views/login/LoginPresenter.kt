package ie.setu.museum.views.login

import android.content.Intent
import com.google.android.material.snackbar.Snackbar
import ie.setu.museum.main.MainApp
import ie.setu.museum.views.museumHome.MuseumHomeView

class LoginPresenter(private val view: LoginView) {

    var app: MainApp = view.application as MainApp

    fun doLogin(email: String, password: String){
        var foundUser = app.users.findByEmail(email)
        if (foundUser == null){
            Snackbar.make(view.binding.root, "No User with given email found", Snackbar.LENGTH_LONG).show()
        } else {
            if (password == foundUser?.password) {
                val intent = Intent(view, MuseumHomeView::class.java)
                intent.putExtra("user",foundUser)
                view.startActivity(intent)
                view.finish()
            } else{
                Snackbar.make(view.binding.email, "Password is incorrect", Snackbar.LENGTH_LONG).show()
            }
        }
    }

}