package se.galvend.isick.settingsfragment


import android.app.Application
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.util.Log
import android.view.*
import kotlinx.android.synthetic.main.fragment_settings.*
import se.galvend.isick.R
import se.galvend.isick.classes.UserViewModel
import se.galvend.isick.firebase.Auth
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {
    companion object {
        const val TAG = "SettingsFragment"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(activity).get(UserViewModel::class.java)

        viewModel.user.observe(this, Observer {
            settingsNameTF.setText(it?.name ?: "")
            settingsEmailTF.setText(it?.email ?: "")
        })

        settingsNameTF.setRawInputType(InputType.TYPE_NULL)
        settingsEmailTF.setRawInputType(InputType.TYPE_NULL)

        editKidRecycler.adapter = EditKidRecyclerAdapter()
        editKidRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        viewModel.kids.observe(this, Observer {
            (editKidRecycler.adapter as EditKidRecyclerAdapter).kids = it ?: emptyList()
            editKidRecycler.adapter.notifyDataSetChanged()
        })

        editNameButton.setOnClickListener {
            Log.d(TAG, "edit name")
            focusTextView(settingsNameTF, true)
        }

        editMailButton.setOnClickListener {
            Log.d(TAG, "edit email")
            focusTextView(settingsEmailTF, false)
        }

        addKidButton.setOnClickListener {
            Log.d(TAG, "add kid")
        }

        editReminderLabel.text = getString(R.string.label_påminnelse, "07:00")
        editPushButton.setOnClickListener {
            Log.d(TAG, "edit push")
        }

        signOutButton.setOnClickListener {
            val auth = Auth()
            auth.signOut()
        }
    }

    private fun focusTextView(editText: EditText, text: Boolean) {
        val imm = activity.getSystemService(Application.INPUT_METHOD_SERVICE) as InputMethodManager
        if(editText.inputType != InputType.TYPE_NULL) {
            editText.clearFocus()
            editText.setRawInputType(InputType.TYPE_NULL)
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        } else {
            editText.requestFocus()

            if (text) {
                editText.setRawInputType(InputType.TYPE_CLASS_TEXT)
            } else {
                editText.setRawInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
            }

            editText.setSelection(editText.text.count())
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
    }

}// Required empty public constructor
