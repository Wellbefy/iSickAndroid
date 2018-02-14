package se.galvend.isick.historyfragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_history.*

import se.galvend.isick.R
import se.galvend.isick.classes.UserViewModel


/**
 * A simple [Fragment] subclass.
 */
class HistoryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        monthLabel.text = "Februari 2018"
        historyRecycler.adapter = HistoryAdapter()
        historyRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val viewModel = ViewModelProviders.of(activity).get(UserViewModel::class.java)
        viewModel.events.observe(this, Observer {
            (historyRecycler.adapter as HistoryAdapter).events = it ?: emptyList()
            historyRecycler.adapter.notifyDataSetChanged()
        })
    }
}// Required empty public constructor
