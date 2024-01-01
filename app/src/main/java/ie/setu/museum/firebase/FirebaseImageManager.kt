package ie.setu.museum.firebase

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.component1
import com.google.firebase.storage.component2
import ie.setu.museum.models.museum.MuseumModel
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.UUID


object FirebaseImageManager {
    var storage = FirebaseStorage.getInstance().reference
    var imageUri = MutableLiveData<List<Uri>>()
    /*
    private val imageUri = MutableLiveData<ArrayList<Uri>>()
    val observableImageUri: LiveData<ArrayList<Uri>>
        get() = imageUri
*/

    var progress: Long = 0

    fun updateUserImage(uri: Uri, currentUser: MutableLiveData<FirebaseUser>) {
        val imageRef = storage.child("user-profile-pic").child("${currentUser.value!!.uid}.jpg")
        lateinit var uploadTask: UploadTask

        uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener { ut ->
            ut.metadata!!.reference!!.downloadUrl.addOnCompleteListener { task ->

                val profileUpadates = userProfileChangeRequest {
                    photoUri = task.result
                }
                currentUser.value!!.updateProfile(profileUpadates).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Timber.i("The profile image downloadURL ${currentUser.value!!.photoUrl}")
                        currentUser.value = currentUser.value

                    }
                    //AddMuseumFragment.newInstance().refreshImageSlider()
                }
            }
        }
    }

        suspend fun deleteAllMuseumImages(museumId: String) {
            try {
                val folderRef = storage.child("museum-images").child("${museumId}")
                //val folderRef = storage.child("museum")
                val items = folderRef.listAll().await().items

                Timber.i("delete: bucket is ${folderRef.bucket}")
                for (item in items) {
                    Timber.i("delete: item bucket : ${item.bucket}")
                    folderRef.child("${item.name}").delete().await()

                }
                Timber.i("delete: after")
            } catch (e: Exception) {
                Timber.i("Error: $e")
            }
        }


        fun uploadMuseumImageToFirebase(
            uri: Uri,
            museumImages: MutableLiveData<List<Uri>>,
            museum: MutableLiveData<MuseumModel>
        ) {
            val key: String = UUID.randomUUID().toString()
            val imageRef = storage.child("museum-images").child("$key")
            museum.value?.images?.clear()
            lateinit var uploadTask: UploadTask
            uploadTask = imageRef.putFile(uri)
            uploadTask.addOnSuccessListener { ut ->
                ut.metadata!!.reference!!.downloadUrl.addOnCompleteListener { task ->
                    Timber.i("The museumList before = ${imageUri.value}")
                    Timber.i("downloadUrl = ${task.result!!}")

                    museumImages.value =
                        museumImages.value?.plus(task.result!!) ?: listOf(task.result!!)
                    museum.value?.images?.add(task.result!!.toString())
                    museum.value = museum.value

                    Timber.i("The imageUriList after = ${imageUri.value}")
                }
            }.addOnProgressListener { (bytesTransferred, totalByteCount) ->
                progress = (100 * bytesTransferred) / totalByteCount
                Timber.i("Upload is $progress% done")
            }.addOnPausedListener {
                Timber.i("Upload is paused")
            }
        }

        fun downloadImageURLs(
            museumId: String,
            downloadUriList: MutableLiveData<List<Uri>>
        ) {

            try {
                var localList = arrayListOf<Uri>()
                val imageRef = storage.child("museum-images").child("${museumId}")
                imageRef.listAll().addOnCompleteListener { task ->
                    val items = task.result!!.items
                    for (item in items) {
                        item.downloadUrl.addOnCompleteListener { task ->

                            localList.add(task.result)
                            downloadUriList.value = localList.toList()
                            Timber.i("localList is $localList")
                            Timber.i("uriList is ${downloadUriList.value}")
                        }

                    }
                }

            } catch (e: Exception) {
                Timber.i("Error: $e")
            }
        }
    }


