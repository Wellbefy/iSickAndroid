package se.galvend.isick.classes

import android.util.Log
import java.text.DateFormat
import java.util.*

/**
 * Created by dennisgalven on 2018-02-14.
 */

data class Event(val id: String?,
                 val name: String?,
                 val date: Long?,
                 val vab: Boolean?,
                 val reported: Boolean?,
                 var removed: Boolean = false) {
    companion object {
        val TAG = "Event"
    }

    fun displayDate() : String {
        val date = Date(this.date!! * 1000)
        val formatter = DateFormat.getDateInstance(DateFormat.DEFAULT)

        return formatter.format(date)
    }

    fun getMonth() : Int {
        val date = Date(date!! * 1000)
        val calendar = Calendar.getInstance()
        calendar.time = date
        Log.d(TAG, calendar.get(Calendar.MONTH).toString())
        return calendar.get(Calendar.MONTH)
    }
}