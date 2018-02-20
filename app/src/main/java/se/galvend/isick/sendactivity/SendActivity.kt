package se.galvend.isick.sendactivity

import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.util.Log
import kotlinx.android.synthetic.main.activity_send.*
import se.galvend.isick.R
class SendActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SendActivity"
        const val BUNDLE = "BUNDLE"
        const val MESSAGES = "MESSAGES"
        const val VAB = "VAB"
    }

    private var viewModel: ViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)

        val vab = intent.getBooleanExtra(VAB, false)

        sendRecycler.adapter = SendMailAdapter()
        sendRecycler.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        val pager = PagerSnapHelper()
        pager.attachToRecyclerView(sendRecycler)

        val bundle = intent.getBundleExtra(BUNDLE) ?: Bundle()
        (sendRecycler.adapter as SendMailAdapter).messages = bundle.getStringArrayList(MESSAGES) ?: emptyList()
        sendRecycler.adapter.notifyDataSetChanged()

        sendBackButton.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel = null
    }
}
