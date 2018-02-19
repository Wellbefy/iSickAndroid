package se.galvend.isick.firebase

import com.google.firebase.auth.FirebaseAuth

/**
 * Created by dennisgalven on 2018-02-13.
 */

class Auth {
    private val instance = FirebaseAuth.getInstance()

    fun login(email: String, password: String, callback: (Exception?) -> Unit) {
        instance.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            callback(it.exception)
        }
    }

    fun signOut() {
        instance.signOut()
    }
}