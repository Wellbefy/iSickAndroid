package se.galvend.isick

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_login_register.*
import org.jetbrains.anko.indeterminateProgressDialog
import se.galvend.isick.classes.MyAlertDialog
import se.galvend.isick.firebase.Auth

@Suppress("DEPRECATION")
class LoginRegisterActivity : AppCompatActivity() {

    companion object {
        val TAG = "LoginRegisterActivity"
    }

    private val alert = MyAlertDialog()
    private val auth = Auth()
    private var thisProgress: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)

        thisProgress = indeterminateProgressDialog("Laddar")
        thisProgress?.dismiss()

        repeatPasswordText.visibility = View.GONE
        nameText.visibility = View.GONE
        emailToText.visibility = View.GONE
        backToLogInButton.visibility = View.GONE
        registerDoneButton.visibility = View.GONE

        toRegisterButton.setOnClickListener {
            changeUI(false)
        }

        doneButton.setOnClickListener {
            wobbleIfWrong(emailText)
            wobbleIfWrong(passwordText)
            login()
        }

        backToLogInButton.setOnClickListener {
            changeUI()
        }

        registerDoneButton.setOnClickListener {
            wobbleIfWrong(emailText)
            wobbleIfWrong(passwordText)
            wobbleIfWrong(repeatPasswordText)
            wobbleIfWrong(nameText)
            wobbleIfWrong(emailToText)

            createUser()
        }
    }

    private fun changeUI(default: Boolean = true) {
        if(default) {
            doneButton.visibility = View.VISIBLE
            toRegisterButton.visibility = View.VISIBLE

            repeatPasswordText.visibility = View.GONE
            nameText.visibility = View.GONE
            emailToText.visibility = View.GONE
            backToLogInButton.visibility = View.GONE
            registerDoneButton.visibility = View.GONE
        } else {
            doneButton.visibility = View.GONE
            toRegisterButton.visibility = View.GONE

            repeatPasswordText.visibility = View.VISIBLE
            nameText.visibility = View.VISIBLE
            emailToText.visibility = View.VISIBLE
            backToLogInButton.visibility = View.VISIBLE
            registerDoneButton.visibility = View.VISIBLE
        }
    }

    private fun wobbleIfWrong(text: EditText) {
        if(text.text.isNullOrEmpty()) text.startAnimation(AnimationUtils.loadAnimation(this, R.anim.wobble))
    }

    private fun login() {
        if(emailText.text.isNullOrEmpty() || passwordText.text.isNullOrEmpty()) return

        loading()

        auth.login(emailText.text.toString(), passwordText.text.toString(), {
            if(it != null) {
                alert.oneAction(this, "Ojdå!", it.localizedMessage)
                loading(false)
            } else {
                allDone()
            }
        })
    }

    private fun createUser() {
        if(emailText.text.isNullOrEmpty() || passwordText.text.isNullOrEmpty()
                || repeatPasswordText.text.isNullOrEmpty() || nameText.text.isNullOrEmpty()
                || emailText.text.isNullOrEmpty()) return

        if(passwordText.text.toString() != repeatPasswordText.text.toString()) {
            alert.oneAction(this, "Ojdå!", "Lösenorden matchar inte")
            return
        }

        loading()

        auth.register(emailText.text.toString(), passwordText.text.toString(), { error, uid ->
            if(error != null) {
                loading(false)
                alert.oneAction(this, "Ojdå!", error.localizedMessage)
            }
            if(uid != null) {
                val email = emailToText.text.toString()
                val name = nameText.text.toString()
                auth.createNewUser(uid, name, email)
                allDone()
            }
        })
    }

    private fun loading(loading: Boolean = true) {
        if(loading) {
            thisProgress?.show()
        } else {
            thisProgress?.dismiss()
        }
    }

    private fun allDone() {
        loading(false)
        val intent = Intent(this, TabActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}
