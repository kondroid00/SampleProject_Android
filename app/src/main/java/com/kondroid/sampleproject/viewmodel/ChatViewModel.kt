package com.kondroid.sampleproject.viewmodel

import android.content.Context
import android.databinding.ObservableField
import com.kondroid.sampleproject.dto.websocket.WebSocketMessageDto

/**
 * Created by kondo on 2017/10/19.
 */

class ChatViewModel(context: Context) : BaseViewModel(context) {
    var inputText: ObservableField<String> = ObservableField("")

    var messages: MutableList<WebSocketMessageDto> = mutableListOf()

    lateinit var onTapSend: () -> Unit

    override fun initVM() {
        super.initVM()
    }

    fun tapSend() {
        val input = inputText.get()
        if (input.length > 0) {
            onTapSend()
        }
    }

    fun addMessage(message: WebSocketMessageDto) {
        messages.add(message)
    }
}