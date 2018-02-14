package se.galvend.isick.reportfragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_report.*

import se.galvend.isick.R
import se.galvend.isick.classes.UserViewModel

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
            if(checked) kidRecycler.visibility = View.VISIBLE
            else kidRecycler.visibility = View.GONE
        }

        kidRecycler.adapter = KidAdapter()
        kidRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

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
                Log.d(TAG, "${it.name}: ${it.isSick}")
            }
        }
    }

}// Required empty public constructor


