package ie.setu.museum.models.museum

import ie.setu.museum.models.user.UserModel

interface MuseumStore {
    fun findAll(): ArrayList<MuseumModel>
    fun create(museum: MuseumModel)
    fun update(museum: MuseumModel)
    fun delete(museum: MuseumModel)
    fun deleteAll()
    fun findById(id:Long): MuseumModel?
    fun findUserMuseums(user: UserModel): List<MuseumModel>
}