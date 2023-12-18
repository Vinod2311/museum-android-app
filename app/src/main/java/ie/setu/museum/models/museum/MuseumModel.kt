package ie.setu.museum.models.museum

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class MuseumModel(var name: String = "",
                       var category: String = "",
                       var shortDescription: String = "",
                       var review: String = "",
                       var rating: Float = 0f,
                       var museumPreviewImage: String = "",
                       var profilePic: String = "",
                       var images: ArrayList<String>? = arrayListOf(),
                       var lat: Double = 0.0,
                       var lng: Double = 0.0,
                       var zoom: Float = 0f,
                       var email: String? = "",
                       var isFavourite: Boolean = false,
                       var uid: String? = ""): Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "category" to category,
            "shortDescription" to shortDescription,
            "review" to review,
            "rating" to rating,
            "museumPreviewImage" to museumPreviewImage,
            "profilePic" to profilePic,
            "images" to images,
            "lat" to lat,
            "lng" to lng,
            "zoom" to zoom,
            "uid" to uid,
            "isFavourite" to isFavourite,
            "email" to email

        )
    }
}
/*
@Parcelize
data class MuseumModelWithFavourites(var name: String = "",
                                     var category: String = "",
                                     var shortDescription: String = "",
                                     var review: String = "",
                                     var rating: Float = 0f,
                                     var museumPreviewImage: String = "",
                                     var profilePic: String = "",
                                     var images: ArrayList<String>? = arrayListOf(),
                                     var lat: Double = 0.0,
                                     var lng: Double = 0.0,
                                     var zoom: Float = 0f,
                                     var email: String? = "",
                                     var uid: String? = "",
                                     var isFavourite: Boolean = false): Parcelable
*/
@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable



