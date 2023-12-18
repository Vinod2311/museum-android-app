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

    /*
    fun checkStorageForExistingProfilePic(userid: String) {
        val imageRef = storage.child("photos").child("${userid}.jpg")
        val defaultImageRef = storage.child("homer.jpg")

        imageRef.metadata.addOnSuccessListener { //File Exists
            imageRef.downloadUrl.addOnCompleteListener { task ->
                imageUri.value = task.result!!
            }
            //File Doesn't Exist
        }.addOnFailureListener {
            imageUri.value = Uri.EMPTY
        }
    }
    */
    fun updateUserImage(uri: Uri, currentUser: MutableLiveData<FirebaseUser>) {
        val imageRef = storage.child("user-profile-pic").child("${currentUser.value!!.uid}.jpg")
        lateinit var uploadTask: UploadTask

        uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener { ut ->
            ut.metadata!!.reference!!.downloadUrl.addOnCompleteListener { task ->
                //Timber.i("The museumList before = ${imageUri.value}")
                //Timber.i("downloadUrl = ${task.result!!}")
                //museumImages.value?.add(task.result!!)
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
            //museum.value?.images?.add(key)
            val imageRef = storage.child("museum-images").child("$key")
            //val bitmap = (imageView as BitmapDrawable).bitmap
            //val baos = ByteArrayOutputStream()
            museum.value?.images?.clear()
            lateinit var uploadTask: UploadTask
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            //val data = baos.toByteArray()
            //Timber.i("The imageUriList = ${imageUri.value!!}")
            uploadTask = imageRef.putFile(uri)
            uploadTask.addOnSuccessListener { ut ->
                ut.metadata!!.reference!!.downloadUrl.addOnCompleteListener { task ->
                    Timber.i("The museumList before = ${imageUri.value}")
                    Timber.i("downloadUrl = ${task.result!!}")
                    //museumImages.value?.add(task.result!!)
                    museumImages.value =
                        museumImages.value?.plus(task.result!!) ?: listOf(task.result!!)
                    museum.value?.images?.add(task.result!!.toString())
                    museum.value = museum.value
                    //AddMuseumFragment.newInstance().refreshImageSlider()
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
            userId: String,
            museumId: String,
            downloadUriList: MutableLiveData<List<Uri>>
        ) {

            try {
                var localList = arrayListOf<Uri>()
                val imageRef = storage.child("museum-images").child("${museumId}")
                /*
             val images = FirebaseDBManager.database
                 .child("user-museums")
                 .child("$userId")
                 .child("$museumId")
                 .child("images")
                 .get()
                 .addOnSuccessListener {
                     Timber.i("images are ${it.value}")
                 }.addOnFailureListener{
                     Timber.i("Error getting image name")
                 }

                  */

                //for (item in images){
                //    localList.add(images.value!!)
                //}

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
            }
        }
    }


