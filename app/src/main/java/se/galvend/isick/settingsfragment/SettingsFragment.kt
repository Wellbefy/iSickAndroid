package se.galvend.isick.settingsfragment


import android.app.Application
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.fragment_settings.*
import se.galvend.isick.R
import se.galvend.isick.classes.UserViewModel
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import se.galvend.isick.EditAddKid


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

        //User view model
        val viewModel = ViewModelProviders.of(activity).get(UserViewModel::class.java)

        //View model observes user name and user email
        viewModel.user.observe(this, Observer {
            settingsNameTF.setText(it?.name ?: "")
            settingsEmailTF.setText(it?.email ?: "")
        })

        //Sets edit texts input type to null
        settingsNameTF.inputType = InputType.TYPE_NULL
        settingsEmailTF.inputType = InputType.TYPE_NULL

        //Recycler view adapter and layout manager
        editKidRecycler.adapter = EditKidRecyclerAdapter()
        editKidRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //View model observes users [kids]
        viewModel.kids.observe(this, Observer {
            (editKidRecycler.adapter as EditKidRecyclerAdapter).kids = it ?: emptyList()
            editKidRecycler.adapter.notifyDataSetChanged()
        })

        //Edit name button listener
        editNameButton.setOnClickListener {
            Log.d(TAG, "edit name")
            focusTextView(settingsNameTF, false)
        }

        //Edit email button listener
        editMailButton.setOnClickListener {
            Log.d(TAG, "edit email")
            focusTextView(settingsEmailTF, true)
        }

        //Add kid button listener
        addKidButton.setOnClickListener {
            val intent = Intent(context, EditAddKid::class.java)
            startActivity(intent)
        }

        //Edit reminder button listener
        editReminderLabel.text = getString(R.string.label_påminnelse, "07:00")
        editPushButton.setOnClickListener {
            Log.d(TAG, "edit push")
        }

        //Sign out button listener
        signOutButton.setOnClickListener {
            Log.d(TAG, "Sign out")
        }

        //Return key listener for edit name Edit text
        settingsNameTF.setOnEditorActionListener { _, action, _ ->
            if(action == EditorInfo.IME_ACTION_DONE) {
                focusTextView(settingsNameTF, false)
                viewModel.changeUserName(settingsNameTF.text.toString())
                true
            } else {
                false
            }
        }

        //Return key listener for edit email Edit text
        settingsEmailTF.setOnEditorActionListener { _, action, _ ->
            if(action == EditorInfo.IME_ACTION_DONE) {
                if(correctEmail()) {
                    focusTextView(settingsEmailTF, true)
                    viewModel.changeUserEmail(settingsEmailTF.text.toString())
                }
                true
            } else {
                false
            }
        }
    }

    //Checks if email address is correct
    private fun correctEmail(): Boolean {
        return settingsEmailTF.text.contains("@") && settingsEmailTF.text.contains(".")
    }

    //Adds/removes focus from edit texts and hides/shows keyboard
    private fun focusTextView(editText: EditText, email: Boolean) {
        val imm = activity.getSystemService(Application.INPUT_METHOD_SERVICE) as InputMethodManager
        if(editText.inputType != InputType.TYPE_NULL) {
            editText.clearFocus()
            editText.inputType = InputType.TYPE_NULL
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        } else {
            editText.requestFocus()
            if(email){
                editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            } else {
                editText.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            }

            editText.setSelection(editText.text.count())
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
    }
}// Required empty public constructor
