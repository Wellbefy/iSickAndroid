package se.galvend.isick.settingsfragment


import android.app.Application
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import kotlinx.android.synthetic.main.fragment_settings.*

import se.galvend.isick.R
import se.galvend.isick.classes.UserViewModel
import se.galvend.isick.firebase.Auth


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {
    companion object {
        val TAG = "SettingsFragment"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(activity).get(UserViewModel::class.java)

        viewModel.user.observe(this, Observer {
            settingsNameLabel.text = it?.name ?: ""
            settingsEmailLabel.text = it?.email ?: ""
        })

        editNameButton.setOnClickListener {
            Log.d(TAG, "edit name")
        }

        editMailButton.setOnClickListener {
            Log.d(TAG, "edit email")
        }

        signOutButton.setOnClickListener {
            val auth = Auth()
            auth.signOut()
        }
    }

}// Required empty public constructor
