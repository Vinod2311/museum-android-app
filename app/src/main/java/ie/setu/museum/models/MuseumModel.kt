package ie.setu.museum.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MuseumModel(var name: String = "",
    var category: String = "",
    var shortDescription: String = "",
    var image: Uri = Uri.EMPTY,
    var id:Long = 0
    ): Parcelable
