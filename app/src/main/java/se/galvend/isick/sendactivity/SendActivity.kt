package se.galvend.isick.sendactivity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_send.*
import se.galvend.isick.R

class SendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)

        sendRecycler.adapter = SendMailAdapter()
        sendRecycler.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)

        (sendRecycler.adapter as SendMailAdapter).name = "Dennis Galvén"
        (sendRecycler.adapter as SendMailAdapter).personNumber = "831228-4170"
    }
}
