package se.galvend.isick

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_login_register.*
import se.galvend.isick.firebase.Auth

class LoginRegisterActivity : AppCompatActivity() {

    companion object {
        val TAG = "LoginRegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)

        doneButton.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val auth = Auth()

        auth.login(emailText.text.toString(), passwordText.text.toString(), {
            if(it != null) {
                //someting gone wrong
                Log.d(TAG, it.message)
            } else {
                //all's good
                Log.d(TAG, "loggat in")
                val intent = Intent(this, TabActivity::class.java)
                startActivity(intent)
                this.finish()
            }
        })
    }
}
