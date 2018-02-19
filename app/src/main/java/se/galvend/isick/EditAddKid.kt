package se.galvend.isick

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_edit_add_kid.*
import kotlinx.android.synthetic.main.activity_tab.*
import se.galvend.isick.classes.Kid
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
            hideKeyboard()
            finish()
        }

        deleteKidButton.setOnClickListener {
            hideKeyboard()
            viewModel.removeKid(name)
            finish()
        }

        doneEditKidButton.setOnClickListener {
            hideKeyboard()
            val name = kidNameTF.text.toString()
            val email = kidEmailTF.text.toString()
            val personNumber = kidPersonNummerTF.text.toString()

            val kid = Kid(name = name, email = email, personNumber = personNumber)

            viewModel.editAddKid(kid)
            finish()
        }

        kidPersonNummerTF.addTextChangedListener(object: TextWatcher {
            var inserted = false
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(p0: Editable?) {
                if(kidPersonNummerTF.text.count()==6 && !inserted) {
                    kidPersonNummerTF.setText("${kidPersonNummerTF.text}-")
                    inserted = true
                    kidPersonNummerTF.setSelection(kidPersonNummerTF.text.count())
                }
                if(kidPersonNummerTF.text.count()<6) inserted = false
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Application.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editRoot.windowToken, 0)
    }
}
