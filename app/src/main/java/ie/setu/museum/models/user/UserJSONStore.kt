package ie.setu.museum.models.museum

/*
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

 */
