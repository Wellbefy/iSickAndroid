package se.galvend.isick.firebase

import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import se.galvend.isick.classes.Event

/**
 * Created by dennisgalven on 2018-02-14.
 */
data class FbEvent(val date: Double? = 0.0,
                   val name: String? = "",
                   val reported : Boolean? = false,
                   val vab : Boolean? = false)

class EventDataBaseRepository {
    companion object {
        val TAG = "EventDataBaseRepository"
    }
    private val dataBase = FirebaseDatabase.getInstance()
    private var eventDataBaseReference: DatabaseReference? = null

    init {
        FirebaseAuth.getInstance().addAuthStateListener {
            if(it.currentUser != null) {
                val uid = it.currentUser!!.uid
                eventDataBaseReference = dataBase?.getReference("users")?.child(uid)?.child("history")
                startListeningToEvents()
            } else {
                eventDataBaseReference = null
            }
        }
    }

    val events : MediatorLiveData<List<Event>> = MediatorLiveData()

    private val eventListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError?) {
            Log.d(TAG, error?.message)
        }

        override fun onDataChange(snapshot: DataSnapshot?) {
            val data = snapshot?.children
                    ?.map {
                        val fbEvent = it.getValue(FbEvent::class.java)
                        return@map Event(it.key,
                                fbEvent?.name ?: "",
                                fbEvent?.date ?: 0.0,
                                fbEvent?.vab ?: false,
                                fbEvent?.reported ?: false)
                    } ?: emptyList()
            events.postValue(data)
        }
    }

    private fun startListeningToEvents() {
        if (eventDataBaseReference == null) return

        eventDataBaseReference?.addValueEventListener(eventListener)
    }

    fun stopListening() {
        eventDataBaseReference?.removeEventListener(eventListener)
    }
}