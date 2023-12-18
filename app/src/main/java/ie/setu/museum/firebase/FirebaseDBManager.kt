package ie.setu.museum.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ie.setu.museum.models.museum.MuseumModel
import ie.setu.museum.models.museum.MuseumStore
import timber.log.Timber

object FirebaseDBManager: MuseumStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun findAll(museumList: MutableLiveData<List<MuseumModel>>) {
        database.child("museums")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Museum error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<MuseumModel>()
                    val children = snapshot.children
                    children.forEach {
                        val museum = it.getValue(MuseumModel::class.java)
                        localList.add(museum!!)
                    }
                    database.child("museums")
                        .removeEventListener(this)

                    museumList.value = localList
                }
            })

    }

    override fun findUserMuseums(userId: String, museumList: MutableLiveData<List<MuseumModel>>) {
        database.child("user-museums").child(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Museum DB error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<MuseumModel>()
                    val children = snapshot.children
                    children.forEach {
                        val museum = it.getValue(MuseumModel::class.java)

                        localList.add(museum!!)
                    }
                    database.child("user-donations").child(userId)
                        .removeEventListener(this)

                    museumList.value = localList
                }
            })
    }

    override fun findById(userId: String, museumId: String, museum: MutableLiveData<MuseumModel>) {
        database.child("user-museums")
            .child(userId)
            .child(museumId)
            .get()
            .addOnSuccessListener {
                museum.value = it.getValue(MuseumModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun findById(museumId: String, museum: MutableLiveData<MuseumModel>) {
        database.child("museums")
            .child(museumId)
            .get()
            .addOnSuccessListener {
                museum.value = it.getValue(MuseumModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, museum: MuseumModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("museums").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        museum.uid = key
        val museumValues = museum.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/museums/$key"] = museumValues
        childAdd["/user-museums/$uid/$key"] = museumValues

        database.updateChildren(childAdd)
    }

    override fun update(userId: String, museum: MuseumModel) {
        //museum.uid = museumId
        val museumValues = museum.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["museums/${museum.uid}"] = museumValues
        childUpdate["user-museums/$userId/${museum.uid}"] = museumValues
        //database.child("museums").child("${museum.uid}").child("images").setValue(null)
        //database.child("user-museums").child("$userId").child("${museum.uid}")
        //    .child("images").setValue("")
        database.updateChildren(childUpdate)
    }

    override fun delete(userId: String, museumId: String) {
        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/museums/$museumId"]=null
        childDelete["/user-museums/$userId/$museumId"]=null
        database.updateChildren(childDelete)
    }

    override fun deleteAll(userId: String) {
        TODO("Not yet implemented")
    }
}