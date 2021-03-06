package se.galvend.isick.permissions

import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat

/**
 * Created by dennisgalven on 2018-02-22.
 * Sms manager class
 */

class PermissionManager {

    fun isPermissionGranted(activity: FragmentActivity, code: String): Boolean =
            (ContextCompat.checkSelfPermission(activity, code) == PackageManager.PERMISSION_GRANTED)

    fun requestPermission(activity: FragmentActivity, code: String, permission: Int) {
        ActivityCompat.requestPermissions(activity, arrayOf(code), permission)
    }
}
