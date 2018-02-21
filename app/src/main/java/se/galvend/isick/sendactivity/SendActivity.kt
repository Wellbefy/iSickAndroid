package se.galvend.isick.sendactivity

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.PagerSnapHelper
import kotlinx.android.synthetic.main.activity_send.*
import se.galvend.isick.R
import se.galvend.isick.classes.MailAndMessage
import se.galvend.isick.classes.MyAlertDialog

class SendActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SendActivity"
        const val BUNDLE = "BUNDLE"
        const val VAB = "VAB"
    }

    private var viewModel: ViewModel? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)

        val vab = intent.getBooleanExtra(VAB, false)

        sendRecycler.adapter = SendMailAdapter()
        sendRecycler.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val pager = PagerSnapHelper()
        pager.attachToRecyclerView(sendRecycler)

        val mailAndMessages= (intent.getSerializableExtra(BUNDLE) as ArrayList<*>)
        (sendRecycler.adapter as SendMailAdapter).messages = mailAndMessages


        mailAndMessages.forEach {
            if(it is MailAndMessage) {
                if (sendToLabel.text.contains("Till:")) {
                    sendToLabel.text = "${sendToLabel.text}\n\t\t\t\t${it.mail}"
                } else {
                    sendToLabel.text = "Till:\t\t${it.mail}"
                }
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
        if(vab) {
            val alert = MyAlertDialog()
            alert.twoAction(this, titleText = "Anmäla till försäkringskassan?", message = "", callback = { ok ->
                if(ok) {
                  //skicka sms
                }
            })
        }

        //skicka mail
        //ladda upp event
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel = null
    }
}
