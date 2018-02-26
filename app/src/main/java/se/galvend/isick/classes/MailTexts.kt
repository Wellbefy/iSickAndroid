package se.galvend.isick.classes

import android.content.Context
import se.galvend.isick.R
import java.text.DateFormat
import java.util.*

/**
 * Created by dennisgalven on 2018-02-26.
 */
class MailTexts {
    private val dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT)

    companion object {
        val sharedInstance = MailTexts()
    }

    fun sickMail(context: Context, name: String, personNumber: String): String {
        return context.getString(R.string.sickmail, dateFormat.format(Date()), name, personNumber)
    }

    fun vabMail(context: Context, name: String, personNumber: String): String {
        return context.getString(R.string.vabmail, dateFormat.format(Date()), name, personNumber)
    }
}