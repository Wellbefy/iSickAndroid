package se.galvend.isick.classes

import java.sql.Timestamp
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by dennisgalven on 2018-02-14.
 */

data class Event(val id: String?,
                 val name: String?,
                 val date: Long?,
                 val vab: Boolean?,
                 val reported: Boolean?) {
    fun displayDate() : String {
        val calendar = Calendar.getInstance()
        val date = Date(this.date!!)
        return ""
    }
}