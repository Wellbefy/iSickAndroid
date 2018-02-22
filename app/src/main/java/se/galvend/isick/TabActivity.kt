package se.galvend.isick

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.backendless.Backendless
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_tab.*
import se.galvend.isick.historyfragment.HistoryFragment
import se.galvend.isick.reportfragment.ReportFragment
import se.galvend.isick.settingsfragment.SettingsFragment

class TabActivity : AppCompatActivity() {

    companion object {
        const val APP_ID = "BFE28D15-3D32-8469-FFE8-5889EFA17800"
        const val API_KEY = "27D05CDF-048E-1857-FF64-AC7F53B33400"
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, ReportFragment())
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, HistoryFragment())
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, SettingsFragment())
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_tab)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, ReportFragment())
                    .commit()
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener {
            if(it.currentUser == null) {
                val intent = Intent(this, LoginRegisterActivity::class.java)
                startActivity(intent)
                this.finish()
            } else {
                Backendless.initApp(this, APP_ID, API_KEY)
                Backendless.Messaging.sendTextEmail("Hej", "Testar", "dennis.galven@gmail.com")
            }
        }
    }
}
