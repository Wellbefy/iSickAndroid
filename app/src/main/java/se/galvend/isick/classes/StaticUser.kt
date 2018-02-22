package se.galvend.isick.classes

/**
 * Created by dennisgalven on 2018-02-22.
 * Static User Class
 */

class StaticUser {
    companion object {
        var staticUser: User? = null
        var mailAndMessages: List<MailAndMessage> = emptyList()
    }
}