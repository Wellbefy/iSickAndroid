package se.galvend.isick.historyfragment


import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.annotation.UiThread
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
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

    object ThreadUtil {
        private val mainHandler = Handler(Looper.getMainLooper())
        fun onMainThread(runnable: Runnable) {
            mainHandler.post(runnable)
        }
        fun cancelRunnable(runnable: Runnable) {
            mainHandler.removeCallbacks(runnable)
        }
    }

    private var viewModel : ViewModel? = null
    private val timer = Timer()

    private var runnableText: Runnable? = null
    private var runnableCircle: Runnable? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        monthLabel.text = getYearAndMonth()

        historyRecycler.adapter = HistoryAdapter()
        historyRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel = ViewModelProviders.of(activity).get(UserViewModel::class.java)
        (viewModel as UserViewModel).events.observe(this, Observer {
            (historyRecycler.adapter as HistoryAdapter).events = (viewModel as UserViewModel).sortDates()
            historyRecycler.adapter.notifyDataSetChanged()
            (viewModel as UserViewModel).getCounts { workPercent, sickPercent, vabPercent ->

                animateLabels(workPercent, workLabel)
                animateLabels(sickPercent, sickLabel)
                animateLabels(vabPercent, vabLabel)

                animateCircles(workProgress, workPercent)
                animateCircles(sickProgress, sickPercent)
                animateCircles(vabProgress, vabPercent)
            }
        })

        val itemTouch = ItemTouch()
        itemTouch.createTouchHelper(context, historyRecycler, (viewModel as UserViewModel))

    }

    private fun animateCircles(progressBar: ProgressBar, value: Float) {
        val workAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, value.toInt()*1000)
        workAnimator.duration = 1000
        workAnimator.interpolator = LinearInterpolator()
        runnableCircle = Runnable {
            workAnimator.start()
        }
        if(runnableCircle != null) ThreadUtil.onMainThread(runnableCircle!!)
    }

    private fun animateLabels(value: Float, label: TextView) {
        var zero = 0f

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

    @SuppressLint("SetTextI18n")
    private fun upDateLabelTexts(value: Float, label: TextView) {
        if(this.isAdded) {
            runnableText = Runnable {
                when (label.id) {
                    R.id.workLabel -> label.text = "Närvaro:\n${"%.1f".format(value)}"
                    R.id.sickLabel -> label.text = "Sjuk:\n${"%.1f".format(value)}"
                    R.id.vabLabel -> label.text = "VAB:\n${"%.1f".format(value)}"
                }
            }
            if(runnableText != null) ThreadUtil.onMainThread(runnableText!!)
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

    override fun onDestroy() {
        super.onDestroy()
        (viewModel as UserViewModel).events.removeObservers(this)
        timer.cancel()
    }
}// Required empty public constructor
