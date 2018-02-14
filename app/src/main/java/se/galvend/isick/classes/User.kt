package se.galvend.isick.classes

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import se.galvend.isick.firebase.DatabaseRepository

/**
 * Created by dennisgalven on 2018-02-12.
 */
data class User(val name: String?, val email: String?)

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val databaseRepository = DatabaseRepository()

    val user: MediatorLiveData<User> = databaseRepository.user
    val kids: MediatorLiveData<List<Kid>> = databaseRepository.kids

    fun signOut() {
        databaseRepository.stopListening()
    }
}