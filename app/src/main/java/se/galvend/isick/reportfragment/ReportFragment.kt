package se.galvend.isick.reportfragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_report.*

import se.galvend.isick.R

class ReportFragment : Fragment() {
    companion object {
        val TAG = "ReportFragment"
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hRCnameLabel.text = "Dennis Galvén"

        vabSwitch.setOnCheckedChangeListener { _, _ ->
            Log.d(TAG, vabSwitch.isChecked.toString())
        }

        kidRecycler.adapter = KidAdapter()
        kidRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

}// Required empty public constructor


