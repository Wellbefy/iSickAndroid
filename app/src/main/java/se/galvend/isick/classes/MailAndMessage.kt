package se.galvend.isick.classes

import java.io.Serializable

/**
 * Created by dennisgalven on 2018-02-20.
 */
data class MailAndMessage(val mail: String? = "", val message: String? = "") : Serializable