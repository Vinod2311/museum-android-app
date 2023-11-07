package ie.setu.museum.models

interface MuseumStore {
    fun findAll(): List<MuseumModel>
    fun create(museum: MuseumModel)
    fun update(museum: MuseumModel)
    fun delete(museum: MuseumModel)
    fun deleteAll()
    fun findById(id:Long): MuseumModel?
}