package com.kondroid.sampleproject.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kondroid.sampleproject.R
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun showAlert(message: String = getString(R.string.alert_default_message),
                  title: String = getString(R.string.alert_default_title),
                  button1Title: String = getString(R.string.alert_default_ok_btn_title),
                  handler1: (() -> Unit)? = null,
                  button2Title: String = getString(R.string.alert_default_cancel_btn_title),
                  handler2: (() -> Unit)? = null)
    {
        alert(message, title) {
            positiveButton(button1Title) {
                if (handler1 != null) {
                    handler1()
                }
            }
            if (handler2 != null) {
                negativeButton(button2Title) {
                    handler2()
                }
            }
        }.show()
    }

    fun goToHome() {
        startActivity<HomeActivity>()
    }
}

