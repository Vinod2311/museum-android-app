package ie.setu.museum.models.museum

interface MuseumStore {
    fun findAll(): ArrayList<MuseumModel>
    fun create(museum: MuseumModel)
    fun update(museum: MuseumModel)
    fun delete(museum: MuseumModel)
    fun deleteAll()
    fun findById(id:Long): MuseumModel?
}