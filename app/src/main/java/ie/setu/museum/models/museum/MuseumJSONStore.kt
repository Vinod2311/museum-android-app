package ie.setu.museum.models.museum

/*

const val JSON_FILE = "museums.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<MuseumModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class MuseumJSONStore(private val context: Context) : MuseumStore {

    var museums = arrayListOf<MuseumModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): ArrayList<MuseumModel> {
        logAll()
        return museums
    }

    override fun create(museum: MuseumModel) {
        museum.id = generateRandomId()
        museums.add(museum)
        serialize()
    }



    override fun update(museum: MuseumModel) {
        var foundMuseum: MuseumModel? = museums.find { p -> p.id == museum.id }
        if (foundMuseum != null){
            foundMuseum.name = museum.name
            foundMuseum.shortDescription = museum.shortDescription
            foundMuseum.image = museum.image
            foundMuseum.category = museum.category
            foundMuseum.lat = museum.lat
            foundMuseum.lng = museum.lng
            foundMuseum.zoom = museum.zoom
            foundMuseum.rating = museum.rating
            foundMuseum.review = museum.review
            foundMuseum.user = museum.user
            logAll()
            serialize()
        }
    }

    override fun delete(museum: MuseumModel) {
        museums.remove(museum)
        serialize()
    }

    override fun deleteAll() {
        museums.clear()
        serialize()
    }

    override fun findById(id:Long): MuseumModel? {
        return museums.find { p -> p.id == id }
    }

    override fun findUserMuseums(userId:Long): ArrayList<MuseumModel> {
        var foundMuseums: List<MuseumModel> = museums.filter{ p -> p.user.id == userId}
        return foundMuseums as ArrayList
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(museums, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        museums = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        museums.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}
*/
