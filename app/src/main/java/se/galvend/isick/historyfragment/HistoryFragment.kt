package se.galvend.isick.historyfragment


import android.animation.ObjectAnimator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
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
            (historyRecycler.adapter as HistoryAdapter).events = viewModel.sortDates()
            historyRecycler.adapter.notifyDataSetChanged()
            viewModel.getCounts { workPercent, sickPercent, vabPercent ->
                workLabel.text = context.getString(R.string.string_workpercent, "%.1f".format(workPercent))
                sickLabel.text = context.getString(R.string.string_sickpercent, "%.1f".format(sickPercent))
                vabLabel.text = context.getString(R.string.string_vabpercent, "%.1f".format(vabPercent))

                animateCircles(workProgress, workPercent)
                animateCircles(sickProgress, sickPercent)
                animateCircles(vabProgress, vabPercent)
            }
        })
    }

    private fun animateCircles(progressBar: ProgressBar, value: Float) {
        val workAnimator = ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, value.toInt())
        workAnimator.duration = 2000
        workAnimator.interpolator = LinearInterpolator()
        workAnimator.start()
    }
}// Required empty public constructor
