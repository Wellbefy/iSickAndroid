package se.galvend.isick.sendactivity

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.telephony.SmsManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_send.*
import se.galvend.isick.R
import se.galvend.isick.classes.MyAlertDialog
import se.galvend.isick.classes.StaticUser
import se.galvend.isick.classes.UserViewModel
import se.galvend.isick.permissions.PermissionManager

class SendActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SendActivity"
        const val VAB = "VAB"
        const val SMS_PERMISSION_CODE = 0
    }

    private var viewModel: ViewModel? = null
    private val permissionManager = PermissionManager()
    private val alert = MyAlertDialog()

    private var vab = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)

        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        Log.d(TAG, StaticUser.mailAndMessages.toString())
        Log.d(TAG, StaticUser.staticUser.toString())
        vab = intent.getBooleanExtra(VAB, false)

        sendRecycler.adapter = SendMailAdapter()
        sendRecycler.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val pager = PagerSnapHelper()
        pager.attachToRecyclerView(sendRecycler)

        (sendRecycler.adapter as SendMailAdapter).messages = StaticUser.mailAndMessages


        StaticUser.mailAndMessages.forEach {
            if (sendToLabel.text.contains("Till:")) {
                sendToLabel.text = "${sendToLabel.text}\n\t\t\t\t${it.mail}"
            } else {
                sendToLabel.text = "Till:\t\t${it.mail}"
            }
        }

        sendButton.setOnClickListener {
            send(vab)
        }

        sendBackButton.setOnClickListener {
            finish()
        }
    }

    private fun send(vab: Boolean) {
//        if(vab) {
//            alert.twoAction(this, titleText = "Anmäla till försäkringskassan?", message = "", callback = { ok ->
//                if(ok) {
//                    checkSMS()
//                }
//            })
//        }
        uploadEvent()
        //skicka mail
        //ladda upp event
    }

    private fun checkSMS() {
        if(!permissionManager.isPermissionGranted(this, android.Manifest.permission.SEND_SMS)) {
            permissionManager.requestPermission(this, android.Manifest.permission.SEND_SMS, SMS_PERMISSION_CODE)
        } else {
            sendSMS()
        }
    }

    private fun sendMail() {

    }

    private fun sendSMS() {
        (viewModel as UserViewModel).fkFireBase.getFKNumber { number ->
            if (number == null) {
                alert.twoAction(this, "Ojdå! Något gick fel.", "Vill du försöka igen?", { ok ->
                    if(ok) sendSMS()
                    else sendMail()
                })
            } else {
                SmsManager.getDefault().sendTextMessage(number, null, "", null, null)
            }
        }
    }

    private fun uploadEvent(reported: Boolean = false) {
        if(vab) {
            StaticUser.mailAndMessages.forEach {
                (viewModel as UserViewModel).uploadEvent(it.name ?: "", vab, reported)
            }
        } else {
            (viewModel as UserViewModel).uploadEvent(StaticUser.staticUser?.name ?: "")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            SMS_PERMISSION_CODE -> {
                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    sendSMS()
                } else {
                    //not granted
                }
                return
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel = null
        StaticUser.mailAndMessages = emptyList()
    }
}
