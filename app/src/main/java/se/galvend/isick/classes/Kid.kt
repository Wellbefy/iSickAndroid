package se.galvend.isick.classes

/**
 * Created by dennisgalven on 2018-02-13.
 */
data class Kid(val name: String?,
               val personNumber: String?,
               val email: String?,
               var isSick: Boolean = false)