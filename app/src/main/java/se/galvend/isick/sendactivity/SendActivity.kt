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
import android.view.View
import kotlinx.android.synthetic.main.activity_send.*
import org.jetbrains.anko.indeterminateProgressDialog
import se.galvend.isick.R
import se.galvend.isick.classes.*
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

    private var reported = false

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
        Log.d(TAG, "loading")
        if(vab) {
            alert.twoAction(this, titleText = "Anmäla till försäkringskassan?", message = "", callback = { ok ->
                if(ok) {
                    checkSMS()
                } else {
                    sendMail()
                }
            })
        } else {
            sendMail()
        }
    }

    private fun checkSMS() {
        if(!permissionManager.isPermissionGranted(this, android.Manifest.permission.SEND_SMS)) {
            permissionManager.requestPermission(this, android.Manifest.permission.SEND_SMS, SMS_PERMISSION_CODE)
        } else {
            sendSMS()
        }
    }

    private fun sendSMS() {
        var userPersonNumber: String? = ""
        var kidPersonNumber: String? = ""
        progress(true)
        StaticUser.mailAndMessages.forEach {
            if(it.name == StaticUser.staticUser?.name) userPersonNumber = it.personNumber?.replace("-", "")
            else kidPersonNumber = it.personNumber?.replace("-", "")
        }

        val smsBody = "VAB ${userPersonNumber ?: ""} ${kidPersonNumber ?: ""}"

        (viewModel as UserViewModel).fkFireBase.getFKNumber { number ->
            if (number == null) {
                progress(false)
                alert.twoAction(this, "Ojdå! Något gick fel.", "Vill du försöka igen?", { ok ->
                    if(ok) checkSMS()
                    else sendMail()
                })
            } else {
                SmsManager.getDefault().sendTextMessage(number, null, smsBody, null, null)
                reported = true
                sendMail()
            }
        }
    }

    private fun sendMail() {
        val mailManager = MailManager()
        var number = 1
        progress(true)
        StaticUser.mailAndMessages.forEach {
            mailManager.sendMail(it.name ?: "", it.message ?: "", it.mail ?: "", {fault ->
                if(fault != null) {
                    Log.d(TAG, fault.message)
                    progress(false)
                    alert.twoAction(this, "Ojdå! Något gick fel.", "Vill du försöka igen?", { ok ->
                        if(ok) sendMail()
                        else finish()
                    })
                } else {
                    if(number >= StaticUser.mailAndMessages.count()) {
                        Log.d(TAG, "stop loading")
                        uploadEvent()
                    }
                }
                Log.d(TAG, number.toString())
                number += 1
            })
        }
    }

    private fun uploadEvent() {
        if(vab) {
            StaticUser.mailAndMessages.forEach {
                Log.d(TAG, it.name)
                (viewModel as UserViewModel).uploadEvent(it.name ?: "", vab, reported)
            }
        } else {
            (viewModel as UserViewModel).uploadEvent(StaticUser.staticUser?.name ?: "")
        }
        progress(false)
        finish()
    }

    private fun progress(show: Boolean) {
        val thisProgress = indeterminateProgressDialog("Skickar")
        if(show) {
            thisProgress.show()
            sendButton.visibility = View.INVISIBLE
            sendBackButton.visibility = View.INVISIBLE
        } else {
            thisProgress.dismiss()
            sendButton.visibility = View.VISIBLE
            sendBackButton.visibility = View.VISIBLE
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            SMS_PERMISSION_CODE -> {
                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    sendSMS()
                } else {
                    sendMail()
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
