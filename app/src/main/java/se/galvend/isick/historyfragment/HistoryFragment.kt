package se.galvend.isick.historyfragment


import android.animation.ObjectAnimator
import android.app.Activity
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
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_history.*

import se.galvend.isick.R
import se.galvend.isick.classes.UserViewModel
import java.util.*
import kotlin.concurrent.schedule


/**
 * A simple [Fragment] subclass.
 */
class HistoryFragment : Fragment() {
    companion object {
        val TAG = "HistroyFragment"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        monthLabel.text = getYearAndMonth()

        historyRecycler.adapter = HistoryAdapter()
        historyRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val viewModel = ViewModelProviders.of(activity).get(UserViewModel::class.java)
        viewModel.events.observe(this, Observer {
            (historyRecycler.adapter as HistoryAdapter).events = viewModel.sortDates()
            historyRecycler.adapter.notifyDataSetChanged()
            viewModel.getCounts { workPercent, sickPercent, vabPercent ->

                animateLabels(workPercent, workLabel)
                animateLabels(sickPercent, sickLabel)
                animateLabels(vabPercent, vabLabel)

                animateCircles(workProgress, workPercent)
                animateCircles(sickProgress, sickPercent)
                animateCircles(vabProgress, vabPercent)
            }
        })

        val itemTouch = ItemTouch()
        itemTouch.createTouchHelper(context, historyRecycler, viewModel)

    }

    private fun animateCircles(progressBar: ProgressBar, value: Float) {
        val workAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, value.toInt()*1000)
        workAnimator.duration = 1000
        workAnimator.interpolator = LinearInterpolator()
        activity.runOnUiThread {
            workAnimator.start()
        }
    }

    private fun animateLabels(value: Float, label: TextView) {
        var zero = 0f
        val timer = Timer()

        if (value <= 0) {
           upDateLabelTexts(zero, label)
            return
        }

        timer.schedule(0, 100/value.toLong(), {
            zero += .1f
            upDateLabelTexts(zero, label)
            if(zero >= value){
                timer.cancel()
                upDateLabelTexts(value, label)
            }
        })
    }

    private fun upDateLabelTexts(value: Float, label: TextView) {
        activity.runOnUiThread {
            when (label.id) {
                R.id.workLabel -> label.text = context.getString(R.string.string_workpercent, "%.1f".format(value))
                R.id.sickLabel -> label.text = context.getString(R.string.string_sickpercent, "%.1f".format(value))
                R.id.vabLabel -> label.text = context.getString(R.string.string_vabpercent, "%.1f".format(value))
            }
        }
    }

    private fun getYearAndMonth(): String {
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val months = resources.getStringArray(R.array.months)
        return "${months[month]} $year"
    }
}// Required empty public constructor
