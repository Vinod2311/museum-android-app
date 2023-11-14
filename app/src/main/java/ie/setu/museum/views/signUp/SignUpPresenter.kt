package ie.setu.museum.views.signUp

import android.content.Intent
import ie.setu.museum.main.MainApp
import ie.setu.museum.models.user.UserModel
import ie.setu.museum.views.login.LoginView

class SignUpPresenter(val view: SignUpView) {

    var user = UserModel()
    var app: MainApp = view.application as MainApp


    fun doAddUser(firstName:String, lastName:String, email:String, password:String) {
        val user = UserModel(firstName=firstName, lastName = lastName, email = email, password = password)
        if (user.email.isNotEmpty() && user.password.isNotEmpty()){
            app.users.create(user)
        }
        val intent = Intent(view, LoginView::class.java)
        view.startActivity(intent)
        view.finish()
    }
}