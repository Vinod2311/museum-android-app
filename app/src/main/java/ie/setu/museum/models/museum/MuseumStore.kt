package ie.setu.museum.models.museum

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface MuseumStore {
    fun findAll(museumList: MutableLiveData<List<MuseumModel>>)
    fun findUserMuseums(userId: String, museumList: MutableLiveData<List<MuseumModel>>)
    fun findById(userId: String, museumId: String, museum: MutableLiveData<MuseumModel>)
    fun findById(museumId: String, museum: MutableLiveData<MuseumModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, museum: MuseumModel)
    fun update(userId: String, museum: MuseumModel)
    fun delete(userId: String, museumId: String)
    fun deleteAll(userId: String)


}