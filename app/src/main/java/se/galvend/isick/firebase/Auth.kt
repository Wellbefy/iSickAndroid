package se.galvend.isick.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by dennisgalven on 2018-02-13.
 * FireBase Auth class
 */
data class NewUser(val name: String? = "", val email: String? = "")
class Auth {
    private val instance = FirebaseAuth.getInstance()

    fun login(email: String, password: String, callback: (Exception?) -> Unit) {
        instance.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            callback(it.exception)
        }
    }

    fun register(email: String, password: String, callback: (Exception?, String?) -> Unit) {
        instance.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isComplete) {
                if(it.isSuccessful) {
                    callback(null, it.result.user.uid)
                } else {
                    callback(it.exception, null)
                }
            }
        }
    }

    fun createNewUser(uid: String, name: String, email: String) {
        val newUser = NewUser(name, email)
        val ref = FirebaseDatabase.getInstance().getReference("users").child(uid)
        ref.setValue(newUser)
    }

    fun signOut() {
        instance.signOut()
    }
}