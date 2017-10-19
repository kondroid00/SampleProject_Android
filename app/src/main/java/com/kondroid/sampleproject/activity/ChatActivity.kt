package com.kondroid.sampleproject.activity

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kondroid.sampleproject.R
import com.kondroid.sampleproject.databinding.ActivityChatBinding
import com.kondroid.sampleproject.viewmodel.ChatViewModel

class ChatActivity : BaseActivity() {
    private  lateinit var vm: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = DataBindingUtil.setContentView<ActivityChatBinding>(this, R.layout.activity_chat)
        vm = ChatViewModel(this)
        binding.vm = vm

        vm.initVM()

        val roomId = intent.getStringExtra("roomId")
        val roomName = intent.getStringExtra("roomName")

        setTitle(roomName)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onStop() {
        super.onStop()
        vm.release()
    }
}
