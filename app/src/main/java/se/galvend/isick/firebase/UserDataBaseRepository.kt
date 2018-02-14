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

class UserDataBaseRepository {
    companion object {
        val TAG = "UserDataBaseRepository"
    }

    private var uid: String? = null
    private var userDatabaseRef: DatabaseReference? = null
    private val databaseRef = FirebaseDatabase.getInstance().reference

    init {
        FirebaseAuth.getInstance().addAuthStateListener {
            if(it.currentUser != null) {
                uid = it.currentUser!!.uid
                userDatabaseRef = databaseRef.child("users").child(uid)

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

    fun stopListening() {
        stopListeningToUserInfor()
    }

    private fun stopListeningToUserInfor() {

    }
}