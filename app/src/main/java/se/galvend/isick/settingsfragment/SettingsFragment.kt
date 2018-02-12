package se.galvend.isick.settingsfragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import se.galvend.isick.R


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_settings, container, false)
    }

}// Required empty public constructor
