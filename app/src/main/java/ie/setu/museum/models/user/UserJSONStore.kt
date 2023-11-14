package ie.setu.museum.models.museum

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ie.setu.museum.helpers.exists
import ie.setu.museum.helpers.read
import ie.setu.museum.helpers.write
import ie.setu.museum.models.user.UserModel
import ie.setu.museum.models.user.UserStore
import timber.log.Timber
import java.lang.reflect.Type
import java.util.Random

const val JSON_USER_FILE = "users.json"
val gsonUserBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .create()
val listUserType: Type = object : TypeToken<ArrayList<UserModel>>() {}.type

fun generateRandomUserId(): Long {
    return Random().nextLong()
}

class UserJSONStore(private val context: Context) : UserStore {

    var users = arrayListOf<UserModel>()

    init {
        if (exists(context, JSON_USER_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): ArrayList<UserModel> {
        logAll()
        return users
    }

    override fun create(user: UserModel) {
        user.id = generateRandomUserId()
        users.add(user)
        serialize()
    }


    override fun update(user: UserModel) {
        var foundUser: UserModel? = users.find { p -> p.id == user.id }
        if (foundUser != null){
            foundUser.firstName = user.firstName
            foundUser.lastName = user.lastName
            foundUser.email = user.email
            foundUser.password = user.password
            logAll()
            serialize()
        }
    }

    override fun delete(user: UserModel) {
        users.remove(user)
        serialize()
    }

    override fun deleteAll() {
        users.clear()
        serialize()
    }

    override fun findById(id: Long): UserModel? {
        return users.find { p -> p.id == id }
    }

    override fun findByEmail(email: String): UserModel? {
        return users.find {p -> p.email == email }
    }

    private fun serialize() {
        val jsonString = gsonUserBuilder.toJson(users, listUserType)
        write(context, JSON_USER_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_USER_FILE)
        users = gsonUserBuilder.fromJson(jsonString, listUserType)
    }

    private fun logAll() {
        users.forEach { Timber.i("$it") }
    }
}
