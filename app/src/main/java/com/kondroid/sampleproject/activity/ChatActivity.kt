package com.kondroid.sampleproject.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kondroid.sampleproject.R

class ChatActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val roomId = intent.getStringExtra("roomId")
    }
}