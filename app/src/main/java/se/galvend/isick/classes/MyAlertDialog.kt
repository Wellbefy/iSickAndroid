package se.galvend.isick.classes

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.view.Gravity
import android.widget.TextView
import se.galvend.isick.R

/**
 * Created by dennisgalven on 2018-02-21.
 * Alert Dialog class
 */
class MyAlertDialog {

    fun twoAction(context: Context, titleText: String, message: String, callback: (Boolean) -> Unit) {

        val background = ColorDrawable()
        background.color = Color.WHITE

        val title = TextView(context)
        title.setPadding(8, 8, 8, 0)
        title.text = titleText
        title.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        title.typeface = ResourcesCompat.getFont(context, R.font.raleway)
        title.background = background
        title.textSize = 24f
        title.gravity = Gravity.CENTER_HORIZONTAL

        val customMessage = TextView(context)
        title.setPadding(8, 16, 8, 0)
        customMessage.text = message
        customMessage.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        customMessage.typeface = ResourcesCompat.getFont(context, R.font.raleway)
        customMessage.background = background
        customMessage.textSize = 18f
        customMessage.gravity = Gravity.CENTER

        val alertDialog = AlertDialog.Builder(context)
                .setCustomTitle(title)
                .setView(customMessage)
                .setPositiveButton("JA", { _, _ ->
                    //kod nedanför så knapp ej stänger alertdialog
                })
                .setNegativeButton("NEJ", { _, _ ->
                    // dismiss alertdialog
                })
                .create()

        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            callback(true)
            alertDialog.dismiss()
        }

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
            callback(false)
            alertDialog.dismiss()
        }

    }
}