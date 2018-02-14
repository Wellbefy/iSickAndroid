package se.galvend.isick.firebase

import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import se.galvend.isick.classes.Kid
import se.galvend.isick.classes.User

/**
 * Created by dennisgalven on 2018-02-12.
 */
data class FbKid(val email: String = "",
                 val personnummer: String = "")

class DatabaseRepository {
    companion object {
        val TAG = "DatabaseRepository"
    }

    private var uid: String? = null
    private var userDatabaseRef: DatabaseReference? = null
    private val databaseRef = FirebaseDatabase.getInstance().reference

    init {
        FirebaseAuth.getInstance().addAuthStateListener {
            if(it.currentUser != null) {
                uid = it.currentUser!!.uid
                userDatabaseRef = databaseRef.child("users").child(uid)
                startListeningToKids()
                getUserInfo()
            } else {
                uid = null
                userDatabaseRef = null
            }
        }
    }

    val user : MediatorLiveData<User> = MediatorLiveData()

    private val userInfoEventListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError?) {
            Log.d(TAG, error?.message)
        }

        override fun onDataChange(snapshot: DataSnapshot?) {
            val name = snapshot?.child("name")?.value as? String ?: ""
            val email = snapshot?.child("email")?.value as? String ?: ""
            val fbUser = User(name, email)

            user.postValue(fbUser)
        }

    }

    private fun getUserInfo() {
        if(userDatabaseRef == null) {
            return
        }

        userDatabaseRef?.addValueEventListener(userInfoEventListener)
    }

    val kids : MediatorLiveData<List<Kid>> = MediatorLiveData()

    private val kidEventListener = object :ValueEventListener {
        override fun onCancelled(error: DatabaseError?) {
            //something went wrong
            Log.d(TAG, error?.message)
        }

        override fun onDataChange(snapshot: DataSnapshot?) {
            val data = snapshot?.children
                    ?.map {
                        val fbKid = it.getValue(FbKid::class.java)
                        return@map Kid(it.key ?: "",
                                fbKid?.personnummer ?: "",
                                fbKid?.email ?: "")
                    } ?: emptyList()
            kids.postValue(data)
        }
    }

    private fun startListeningToKids() {
        if(userDatabaseRef == null) {
            return
        }
        userDatabaseRef?.child("kids")?.addValueEventListener(kidEventListener)
    }

    fun stopListening() {
        stopListeningToKids()
        stopListeningToUserInfor()
    }

    private fun stopListeningToKids() {
        userDatabaseRef?.child("kids")?.removeEventListener(kidEventListener)
    }

    private fun stopListeningToUserInfor() {

    }
}