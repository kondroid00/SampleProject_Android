package com.kondroid.sampleproject.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kondroid.sampleproject.R

class TopActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)

        supportActionBar?.hide()
    }
}
