package com.kondroid.sampleproject.activity

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.kondroid.sampleproject.R
import com.kondroid.sampleproject.auth.AccountManager
import com.kondroid.sampleproject.databinding.ActivityChatBinding
import com.kondroid.sampleproject.dto.websocket.WebSocketActionDto
import com.kondroid.sampleproject.dto.websocket.WebSocketErrorDto
import com.kondroid.sampleproject.dto.websocket.WebSocketMessageDto
import com.kondroid.sampleproject.helper.makeWeak
import com.kondroid.sampleproject.logic.WebSocketLogic
import com.kondroid.sampleproject.view.adapter.ChatListAdapter
import com.kondroid.sampleproject.viewmodel.ChatViewModel
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : BaseActivity(), WebSocketLogic.Delegate {
    private lateinit var chatListAdapter: ChatListAdapter
    private lateinit var vm: ChatViewModel
    private val webSocketLogic = WebSocketLogic()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = DataBindingUtil.setContentView<ActivityChatBinding>(this, R.layout.activity_chat)
        vm = ChatViewModel(this)
        binding.vm = vm

        vm.initVM()

        val roomId = intent.getStringExtra("roomId")
        val roomName = intent.getStringExtra("roomName")

        setTitle(roomName)

        webSocketLogic.delegate = makeWeak(this)
        //webSocketLogic.connect(roomId)
        webSocketLogic.connect("test")

        val weakSelf = makeWeak(this)
        vm.onTapSend = {
            weakSelf.get()?.sendMessage()
        }

        setUpRecyclerView()
        setUpCallback()
    }

    override fun onStop() {
        super.onStop()
        vm.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketLogic.disconnect()
    }

    private fun setUpRecyclerView() {
        val recyclerView = chatRecyclerView
        recyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        chatListAdapter = ChatListAdapter(this)
        val weakSelf = makeWeak(this)
        recyclerView.adapter = chatListAdapter
    }

    private fun setUpCallback() {
        val weakSelf = makeWeak(this)
        vm.onTapSend = { weakSelf.get()?.sendMessage() }
    }

    private fun sendMessage() {
        webSocketLogic.sendMessage(vm.inputText.get())
    }

    //-------------------------------------------------------------------------------------------
    //  WebSocketLogic Delegate
    //-------------------------------------------------------------------------------------------
    override fun onOpen() {
        val a = AccountManager.user
        AccountManager.user?.let {
            webSocketLogic.sendJoin(it)
        }
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {

    }

    override fun onReceiveJoined(data: WebSocketActionDto) {
        chatListAdapter.clientNo = webSocketLogic.clientNo
    }

    override fun onReceiveRemoved(data: WebSocketActionDto) {

    }

    override fun onReceiveMessage(data: WebSocketMessageDto) {
        vm.addMessage(data)
        chatListAdapter.setMessages(vm.messages)
    }

    override fun onReceiveError(data: WebSocketErrorDto) {

    }

}
