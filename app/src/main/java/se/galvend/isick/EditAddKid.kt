package se.galvend.isick

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_edit_add_kid.*
import se.galvend.isick.classes.UserViewModel

class EditAddKid : AppCompatActivity() {

    companion object {
        const val TAG = "EditAddKid"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_add_kid)

        val viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        val name = intent.getStringExtra("name") ?: ""

        if(name.isEmpty()) {
            deleteKidButton.visibility = View.INVISIBLE
        } else {
            kidNameTF.setText(name)
            viewModel.kids.observe(this, Observer {
                it?.forEach {
                    if(it.name == name){
                        kidEmailTF.setText(it.email)
                        kidPersonNummerTF.setText(it.personNumber)
                    }
                }
            })
        }

        kidNameTF.setSelection(kidNameTF.text.count())

        backFromEditKidButton.setOnClickListener {
            finish()
        }
    }
}
