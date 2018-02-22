package se.galvend.isick.firebase

import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import se.galvend.isick.classes.Kid

/**
 * Created by dennisgalven on 2018-02-14.
 * Kid Fire Base class
 */

data class FbKid(val email: String = "",
                 val personnummer: String = "")

class KidDataBaseRepository {
    companion object {
        const val TAG = "KidDataBaseRepository"
    }
    private var kidDataBaseRef: DatabaseReference? = null
    private val dataBase = FirebaseDatabase.getInstance()

    init {
        FirebaseAuth.getInstance().addAuthStateListener {
            if(it.currentUser != null) {
                kidDataBaseRef = dataBase.getReference("users").child(it.currentUser?.uid).child("kids")
                startListeningToKids()
            } else {
                kidDataBaseRef = null
            }
        }
    }
    val kids : MediatorLiveData<List<Kid>> = MediatorLiveData()

    private val kidEventListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError?) {
            //something went wrong
            Log.d(UserDataBaseRepository.TAG, error?.message)
        }

        override fun onDataChange(snapshot: DataSnapshot?) {
            val data = snapshot?.children
                    ?.map {
                        val fbKid = it.getValue(FbKid::class.java)
                        return@map Kid(it.key ?: "",
                                fbKid?.personnummer ?: "",
                                fbKid?.email ?: "")
                    } ?: emptyList()
            Log.d(TAG, data.toString())
            kids.postValue(data)
        }
    }

    private fun startListeningToKids() {
        if(kidDataBaseRef == null) return

        kidDataBaseRef?.addValueEventListener(kidEventListener)
    }

    fun editAddKid(kid: Kid) {
        if(kidDataBaseRef == null) return
        val fbKid = FbKid(kid.email ?: "", kid.personNumber ?: "")
        kidDataBaseRef?.child(kid.name)?.setValue(fbKid)
    }

    fun removeKid(name: String) {
        if(kidDataBaseRef == null) return

        kidDataBaseRef?.child(name)?.removeValue()
    }

    fun stopListening() {
        kidDataBaseRef?.removeEventListener(kidEventListener)
    }
}