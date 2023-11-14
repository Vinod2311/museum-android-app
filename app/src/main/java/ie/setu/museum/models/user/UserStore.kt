package ie.setu.museum.models.user

interface UserStore {
    fun findAll(): ArrayList<UserModel>
    fun create(user: UserModel)
    fun update(user: UserModel)
    fun delete(user: UserModel)
    fun deleteAll()
    fun findById(id:Long): UserModel?
    fun findByEmail(email:String): UserModel?
}