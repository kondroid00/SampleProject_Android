package com.kondroid.sampleproject.viewmodel.cell

import android.databinding.ObservableField
import com.kondroid.sampleproject.dto.websocket.WebSocketMessageDto

/**
 * Created by kondo on 2017/10/19.
 */

class ChatListViewModel {
    val messageText: ObservableField<String> = ObservableField("")
    val nameText: ObservableField<String> = ObservableField("")

    fun loadItem(data: WebSocketMessageDto) {
        messageText.set(data.message)
        nameText.set(data.name)
    }
}