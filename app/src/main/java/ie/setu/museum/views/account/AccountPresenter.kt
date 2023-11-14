package ie.setu.museum.views.account

import android.content.Intent
import ie.setu.museum.main.MainApp
import ie.setu.museum.models.user.UserModel
import ie.setu.museum.views.login.LoginView

class AccountPresenter(private val view:AccountView) {

    var app: MainApp = view.application as MainApp
    var user = UserModel()

    init {
        user = app.users.findByEmail("homer@simpson.com")!!
    }

    fun doUpdateAccount(){
        view.showUserDetails(user)
    }

    fun doCancel() {
        view.finish()
    }

    fun doLogout() {
        val intent = Intent(view,LoginView::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        view.startActivity(intent)
    }
}