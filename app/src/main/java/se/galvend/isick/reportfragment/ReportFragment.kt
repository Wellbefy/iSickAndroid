package se.galvend.isick.reportfragment


import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.nfc.Tag
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_edit_add_kid.*
import kotlinx.android.synthetic.main.activity_login_register.*
import kotlinx.android.synthetic.main.fragment_report.*

import se.galvend.isick.R
import se.galvend.isick.classes.CheckPersonNumber
import se.galvend.isick.classes.Event
import se.galvend.isick.classes.UserViewModel
import se.galvend.isick.firebase.FbEvent
import java.sql.Timestamp
import java.util.*

class ReportFragment : Fragment() {
    companion object {
        const val TAG = "ReportFragment"
        const val PREFS_NAME = "com.iSick.prefs"
        const val PERSON_NUMBER = "personNumber"
    }

    var viewModel: ViewModel? = null

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

        viewModel = ViewModelProviders.of(activity).get(UserViewModel::class.java)

        (viewModel as UserViewModel).kids.observe(this , Observer{
            (kidRecycler.adapter as KidAdapter).kids = it ?: emptyList()
            kidRecycler.adapter.notifyDataSetChanged()
        })

        (viewModel as UserViewModel).user.observe(this, Observer {
            nameLabel.text = it?.name ?: ""
        })

        val sharedPreferences = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val fetchedPersonNumber = sharedPreferences?.getString(PERSON_NUMBER, "")

        val checkPersonNumber = CheckPersonNumber()
        activateDoneButton(checkPersonNumber.checkNumber(fetchedPersonNumber ?: ""))

        prsnrTF.setText(fetchedPersonNumber)
        prsnrTF.setSelection(prsnrTF.text.count())

        prsnrTF.addTextChangedListener(object : TextWatcher {
            var inserted = false
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(p0: Editable?) {
                //inserts "-" after six't number
                if (prsnrTF.text.count() == 6 && !inserted) {
                    prsnrTF.setText("${prsnrTF.text}-")
                    inserted = true
                    prsnrTF.setSelection(prsnrTF.text.count())
                }
                //reverts inserted to false so user can delete characters
                if (prsnrTF.text.count() < 6) inserted = false

                val text = prsnrTF.text.toString()
                activateDoneButton(checkPersonNumber.checkNumber(text))
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })

        sendButton.setOnClickListener {
            (kidRecycler.adapter as KidAdapter).kids.forEach {
//                val seconds = System.currentTimeMillis()/1000
//                val event = FbEvent(seconds, "Sandra Bengtsson", true, true)
//                viewModel.uploadEvent(event)
                Log.d(TAG, "send")
                val editor = sharedPreferences.edit()
                editor.putString(PERSON_NUMBER, prsnrTF.text.toString())
                editor.apply()
            }
        }
    }

    private fun activateDoneButton(activate: Boolean) {
        if(activate) {
            checkUserPersonNumber.visibility = View.VISIBLE
            sendButton.isEnabled = true
        } else {
            checkUserPersonNumber.visibility = View.INVISIBLE
            sendButton.isEnabled = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (viewModel as UserViewModel).kids.removeObservers(this)
        (viewModel as UserViewModel).user.removeObservers(this)
    }
}// Required empty public constructor


