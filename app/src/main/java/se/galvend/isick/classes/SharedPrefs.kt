package se.galvend.isick.classes

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by dennisgalven on 2018-02-21.
 * Shared preferences class
 */
class SharedPrefs {
    companion object {
        const val PREFS_NAME = "com.iSick.prefs"
        const val PERSON_NUMBER = "personNumber"
    }

    fun saveToSharedPrefs(context: Context, saveString: String) {
        val sharedPreferences = sharedPrefs(context)
        val editor = sharedPreferences.edit()
        editor.putString(PERSON_NUMBER, saveString)
        editor.apply()
    }

    fun fetchSharedPrefs(context: Context): String {
        val sharedPreferences = sharedPrefs(context)
        return sharedPreferences.getString(PERSON_NUMBER, "") ?: ""
    }

    fun deleteSharedPrefs(context: Context) {
        val sharedPreferences = sharedPrefs(context)
        val editor = sharedPreferences.edit()
        editor.putString(PERSON_NUMBER, "")
        editor.apply()
    }

    private fun sharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
}