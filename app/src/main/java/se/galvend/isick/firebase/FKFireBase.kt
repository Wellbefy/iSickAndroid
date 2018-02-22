package se.galvend.isick.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by dennisgalven on 2018-02-22.
 * FK Firebase Class
 */

class FKFireBase {
    fun getFKNumber(callback: (String?) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("fk")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                callback(null)
            }

            override fun onDataChange(snapshot: DataSnapshot?) {
                val number: String? = snapshot?.child("nummer")?.value as? String

                callback(number)
            }
        })
    }
}
