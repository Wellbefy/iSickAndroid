package se.galvend.isick.reportfragment


import android.animation.Animator
import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.fragment_report.*
import se.galvend.isick.EditAddKid
import se.galvend.isick.R
import se.galvend.isick.classes.*
import se.galvend.isick.sendactivity.SendActivity
import java.io.Serializable
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReportFragment : Fragment() {
    companion object {
        const val TAG = "ReportFragment"
        const val BUNDLE = "BUNDLE"
        const val VAB = "VAB"
    }

    private var viewModel: ViewModel? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vabSwitch.setOnCheckedChangeListener { _, checked ->
            animateRecycler(checked)
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
            (viewModel as UserViewModel).staticUser = User(it?.name ?: "", it?.email ?: "")
        })

        val fetchedPersonNumber = (viewModel as UserViewModel).sharedPrefs.fetchSharedPrefs(context)

        val checkPersonNumber = CheckPersonNumber()

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
                if(checkPersonNumber.checkNumber(text)) {
                    checkUserPersonNumber.visibility = View.VISIBLE
                } else {
                    checkUserPersonNumber.visibility = View.INVISIBLE
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })

        sendButton.setOnClickListener {
            Log.d(TAG, "send")
            if(!checkPersonNumber.checkNumber(prsnrTF.text.toString())) {
                shakeEditText()
            } else {
                (viewModel as UserViewModel).sharedPrefs.saveToSharedPrefs(context, prsnrTF.text.toString())

                if(vabSwitch.isChecked && (kidRecycler.adapter as KidAdapter).kids.isEmpty()) {
                    val alertDialog = MyAlertDialog()
                    alertDialog.twoAction(context, "Inga barn tillagda", "Vill du lägga till ett?", { OK ->
                        if(OK) {
                            toAddKids()
                        } else {
                            toSend()
                        }
                    })
                } else {
                    toSend()
                }
            }
        }
    }

    private fun animateRecycler(show: Boolean, reset: Boolean = false) {
        if((kidRecycler.adapter as KidAdapter).kids.isEmpty()) return

        if(show) {
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

        if(reset) {
            (kidRecycler.adapter as KidAdapter).kids.forEach {
                it.isSick = false
            }
            kidRecycler.adapter.notifyDataSetChanged()
            vabSwitch.isChecked = false
        }
    }

    private fun toSend() {
        val mailAndMessages = ArrayList<MailAndMessage>()

        val date = Date()
        val formatter = DateFormat.getDateInstance(DateFormat.DEFAULT)
        val formattedDate = formatter.format(date)

        val mailAndMessage = MailAndMessage(mail = (viewModel as UserViewModel).staticUser?.email,
                message = if(vabSwitch.isChecked) {
            getString(R.string.vabmail, formattedDate, nameLabel.text, prsnrTF.text.toString())
        } else {
            getString(R.string.sickmail, formattedDate, nameLabel.text, prsnrTF.text.toString())
        })

        mailAndMessages.add(mailAndMessage)

        (kidRecycler.adapter as KidAdapter).kids.forEach {
            if (it.isSick) {
                val kidMailAndMessage = MailAndMessage(it.email, getString(R.string.sickmail, formattedDate, it.name, it.personNumber))
                mailAndMessages.add(kidMailAndMessage)
            }
        }

        val intent = Intent(context, SendActivity::class.java)

        intent.putExtra(BUNDLE, mailAndMessages as Serializable)

        intent.putExtra(VAB, vabSwitch.isChecked)

        startActivity(intent)

        animateRecycler(false, true)
    }

    private fun toAddKids() {
        val intent = Intent(context, EditAddKid::class.java)
        startActivity(intent)
    }

    private fun shakeEditText() {
        prsnrTF.startAnimation(AnimationUtils.loadAnimation(context, R.anim.wobble))
    }

    override fun onDestroy() {
        super.onDestroy()
        (viewModel as UserViewModel).kids.removeObservers(this)
        (viewModel as UserViewModel).user.removeObservers(this)
    }
}// Required empty public constructor



