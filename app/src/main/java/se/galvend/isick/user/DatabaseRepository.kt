package se.galvend.isick.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by dennisgalven on 2018-02-12.
 */

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
            } else {
                uid = null
                userDatabaseRef = null
            }
        }
    }
}