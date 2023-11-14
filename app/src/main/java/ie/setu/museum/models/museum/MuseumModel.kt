package ie.setu.museum.models.museum

import android.net.Uri
import android.os.Parcelable
import ie.setu.museum.models.user.UserModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class MuseumModel(var name: String = "",
                       var category: String = "",
                       var shortDescription: String = "",
                       var review: String = "",
                       var rating: Float = 0f,
                       var image: ArrayList<Uri> = arrayListOf<Uri>(Uri.EMPTY),
                       var id:Long = 0,
                       var lat: Double = 0.0,
                       var lng: Double = 0.0,
                       var zoom: Float = 0f,
                       var user: UserModel = UserModel() ): Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable


