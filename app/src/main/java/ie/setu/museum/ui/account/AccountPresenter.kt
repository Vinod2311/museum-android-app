package ie.setu.museum.ui.account

import ie.setu.museum.main.MainApp
import ie.setu.museum.models.user.UserModel

class AccountPresenter(private val view:AccountView) {

    var app: MainApp = view.application as MainApp
    var user = UserModel()

    init {
        user = view.intent.extras?.getParcelable("user")!!
    }
/*
    fun doUpdateAccount(){
        var userMuseums = app.museums.findUserMuseums(user.id)
        view.showUserDetails(user,userMuseums)
    }

    fun doCancel() {
        view.finish()
    }

    fun doLogout() {
        val intent = Intent(view, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        view.startActivity(intent)
    }
    */

}