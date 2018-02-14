package se.galvend.isick.classes

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import se.galvend.isick.firebase.EventDataBaseRepository
import se.galvend.isick.firebase.KidDataBaseRepository
import se.galvend.isick.firebase.UserDataBaseRepository

/**
 * Created by dennisgalven on 2018-02-12.
 */
data class User(val name: String?, val email: String?)

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userDataBaseRepository = UserDataBaseRepository()
    private val kidDataBaseRepositroy = KidDataBaseRepository()
    private val eventDataBaseRepository = EventDataBaseRepository()

    val user: MediatorLiveData<User> = userDataBaseRepository.user
    val kids: MediatorLiveData<List<Kid>> = kidDataBaseRepositroy.kids
    val events: MediatorLiveData<List<Event>> = eventDataBaseRepository.events

    fun signOut() {
        userDataBaseRepository.stopListening()
        eventDataBaseRepository.stopListening()
        kidDataBaseRepositroy.stopListening()
    }
}