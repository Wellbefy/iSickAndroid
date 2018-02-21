package se.galvend.isick

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_edit_add_kid.*
import se.galvend.isick.classes.CheckPersonNumber
import se.galvend.isick.classes.Kid
import se.galvend.isick.classes.UserViewModel

class EditAddKid : AppCompatActivity() {

    companion object {
        const val TAG = "EditAddKid"
    }

    private var fromReport = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_add_kid)

        val viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        val checkPersonNumber = CheckPersonNumber()

        val name = intent.getStringExtra("name") ?: ""

        if(name.isEmpty()) {
            deleteKidButton.visibility = View.INVISIBLE
            personNumberCheck.visibility = View.INVISIBLE
            nameCheck.visibility = View.INVISIBLE
            emailCheck.visibility = View.INVISIBLE
        } else {
            kidNameTF.setText(name)
            viewModel.kids.observe(this, Observer {
                it?.forEach {
                    if(it.name == name){
                        kidEmailTF.setText(it.email)
                        kidEmailTF.setSelection(kidEmailTF.text.count())
                        val correctEmail = kidEmailTF.text.toString().contains("@") &&
                                kidEmailTF.text.toString().contains(".")
                        checkMarks(emailCheck, correctEmail)

                        kidPersonNummerTF.setText(it.personNumber)
                        kidPersonNummerTF.setSelection(kidPersonNummerTF.text.count())
                        val correctNr = checkPersonNumber.checkNumber(kidPersonNummerTF.text.toString())
                        checkMarks(personNumberCheck, correctNr)
                    }
                }
            })
        }

        kidNameTF.setSelection(kidNameTF.text.count())
        val correctName = kidNameTF.text.toString().contains(" ")
        checkMarks(nameCheck, correctName)

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

            if(allCorrect()) {
                val kidName = kidNameTF.text.toString()
                val email = kidEmailTF.text.toString()
                val personNumber = kidPersonNummerTF.text.toString()

                val kid = Kid(name = kidName, email = email, personNumber = personNumber)

                viewModel.editAddKid(kid)
                finish()
            }
        }

        kidNameTF.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val ok = kidNameTF.text.toString().contains(" ")
                checkMarks(nameCheck, ok)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })

        kidEmailTF.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val ok = kidEmailTF.text.toString().contains("@") &&
                        kidEmailTF.text.toString().contains(".")
                checkMarks(emailCheck, ok)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })


        kidPersonNummerTF.addTextChangedListener(object: TextWatcher {
            var inserted = false
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(p0: Editable?) {
                //inserts "-" after six't number
                if(kidPersonNummerTF.text.count()==6 && !inserted) {
                    kidPersonNummerTF.setText("${kidPersonNummerTF.text}-")
                    inserted = true
                    kidPersonNummerTF.setSelection(kidPersonNummerTF.text.count())
                }
                //reverts inserted to false so user can delete characters
                if(kidPersonNummerTF.text.count()<6) inserted = false

                val text = kidPersonNummerTF.text.toString()
                checkMarks(personNumberCheck, checkPersonNumber.checkNumber(text))
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

    private fun checkMarks(imageView: ImageView, show: Boolean) {
        if(show) {
            imageView.visibility = View.VISIBLE
        } else {
            imageView.visibility = View.INVISIBLE
        }
    }

    private fun allCorrect(): Boolean {
        return nameCheck.visibility == View.VISIBLE &&
                emailCheck.visibility == View.VISIBLE &&
                personNumberCheck.visibility == View.VISIBLE
    }
}
