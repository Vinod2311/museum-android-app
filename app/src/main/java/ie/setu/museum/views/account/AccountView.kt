package ie.setu.museum.views.account

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ie.setu.museum.R
import ie.setu.museum.databinding.ActivityAccountViewBinding
import ie.setu.museum.models.user.UserModel

class AccountView : AppCompatActivity() {

    lateinit var binding: ActivityAccountViewBinding
    lateinit var presenter: AccountPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        presenter = AccountPresenter(this)
        presenter.doUpdateAccount()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_account, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                presenter.doCancel()
            }
            R.id.item_logout -> {
                presenter.doLogout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showUserDetails(user: UserModel){
        binding.userFirstName.text = user.firstName
        binding.userLastName.text = user.lastName
        binding.userEmail.text = user.email
    }
}