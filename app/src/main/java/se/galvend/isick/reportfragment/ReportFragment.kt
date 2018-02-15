package se.galvend.isick.reportfragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.nfc.Tag
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_login_register.*
import kotlinx.android.synthetic.main.fragment_report.*

import se.galvend.isick.R
import se.galvend.isick.classes.Event
import se.galvend.isick.classes.UserViewModel
import se.galvend.isick.firebase.FbEvent
import java.sql.Timestamp
import java.util.*

class ReportFragment : Fragment() {
    companion object {
        val TAG = "ReportFragment"
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vabSwitch.setOnCheckedChangeListener { _, checked ->
            if(checked) {
                kidRecycler.visibility = View.VISIBLE
                kidRecycler.animate()
                        .scaleY(1f)
                        .setDuration(100L)
                        .start()
            } else {
                kidRecycler.animate()
                        .scaleY(0f)
                        .setDuration(100L)
                        .start()
                kidRecycler.visibility = View.GONE
            }
        }

        kidRecycler.adapter = KidAdapter()
        kidRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        kidRecycler.scaleY = 0f
        kidRecycler.visibility = View.GONE

        val viewModel = ViewModelProviders.of(activity).get(UserViewModel::class.java)

        viewModel.kids.observe(this , Observer{
            (kidRecycler.adapter as KidAdapter).kids = it ?: emptyList()
            kidRecycler.adapter.notifyDataSetChanged()
        })

        viewModel.user.observe(this, Observer {
            nameLabel.text = it?.name ?: ""
        })

        sendButton.setOnClickListener {
            (kidRecycler.adapter as KidAdapter).kids.forEach {
                val seconds = System.currentTimeMillis()/1000
                val event = FbEvent(seconds, "Sandra Bengtsson", true, true)
                viewModel.uploadEvent(event)
            }
        }
    }

}// Required empty public constructor


