package se.galvend.isick.classes

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.content.Context
import se.galvend.isick.firebase.*
import java.util.*

/**
 * Created by dennisgalven on 2018-02-12.
 * User View Model
 */

data class User(val name: String?, val email: String?)

class UserViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG = "UserViewModel"
    }
    private val userDataBaseRepository = UserDataBaseRepository()
    private val kidDataBaseRepository = KidDataBaseRepository()
    private val eventDataBaseRepository = EventDataBaseRepository()
    private val auth = Auth()

    val sharedPrefs = SharedPrefs()

    val fkFireBase = FKFireBase()

    val user: MediatorLiveData<User> = userDataBaseRepository.user
    val kids: MediatorLiveData<List<Kid>> = kidDataBaseRepository.kids
    val events: MediatorLiveData<List<Event>> = eventDataBaseRepository.events

    fun signOut(context: Context) {
        userDataBaseRepository.stopListening()
        eventDataBaseRepository.stopListening()
        kidDataBaseRepository.stopListening()
        sharedPrefs.deleteSharedPrefs(context)
        auth.signOut()
    }

    fun changeUserName(name: String) {
        userDataBaseRepository.changeUserName(name)
    }

    fun changeUserEmail(email: String) {
        userDataBaseRepository.changeUserEmail(email)
    }

    fun uploadEvent(name: String, vab: Boolean = false, reported: Boolean = false) {
        eventDataBaseRepository.uploadEvent(name, vab, reported)
    }

    fun removeEvent(id: String?) {
        if(id == null) return

        eventDataBaseRepository.removeEvent(id)
    }

    fun sortDates(): List<Event> {
        val sortedEvents = events.value

        return sortedEvents?.sortedBy { it.date }?.reversed() ?: emptyList()
    }

    fun getCounts(callback: (Float, Float, Float) -> Unit) {
        val calendar = Calendar.getInstance()
        val dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH).toFloat()
        val month =  calendar.get(Calendar.MONTH)

        var vabCount = 0f
        var vabDate = ""
        var sickCount = 0f
        var sickDate = ""

        events.value?.forEach {
            if(it.getMonth() == month) {
                if (it.vab!! && it.displayDate() != vabDate) {
                    vabCount += 1
                    vabDate = it.displayDate()
                }

                if (!it.vab && it.displayDate() != sickDate) {
                    sickCount += 1
                    sickDate = it.displayDate()
                }
            }
        }

        val workPercent = ((dayCount - (sickCount+vabCount))/dayCount) * 100
        val sickPercent = (sickCount/dayCount) * 100
        val vabPercent = (vabCount/dayCount) * 100

        callback(workPercent, sickPercent, vabPercent)
    }

    fun editAddKid(kid: Kid) {
        kidDataBaseRepository.editAddKid(kid)
    }

    fun removeKid(name: String) {
        kidDataBaseRepository.removeKid(name)
    }

    override fun onCleared() {
        super.onCleared()
        userDataBaseRepository.stopListening()
        eventDataBaseRepository.stopListening()
        kidDataBaseRepository.stopListening()
    }
}