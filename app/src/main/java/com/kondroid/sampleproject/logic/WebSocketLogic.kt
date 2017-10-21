package com.kondroid.sampleproject.logic

import android.content.Context
import android.util.Log
import com.kondroid.sampleproject.R
import com.kondroid.sampleproject.constants.ChatConstants
import com.kondroid.sampleproject.constants.NetworkConstants
import com.kondroid.sampleproject.dto.UserDto
import com.kondroid.sampleproject.dto.websocket.WebSocketActionDto
import com.kondroid.sampleproject.dto.websocket.WebSocketErrorDto
import com.kondroid.sampleproject.dto.websocket.WebSocketMessageDto
import com.kondroid.sampleproject.helper.fromJson
import com.kondroid.sampleproject.helper.makeWeak
import com.kondroid.sampleproject.helper.toJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.URI


/**
 * Created by kondo on 2017/10/20.
 */

class WebSocketLogic {

    enum class State {
        Opening,
        Opened,
        Closed
    }

    // State
    var state = State.Closed
        private set

    // WebSocket
    private var socket: WebSocketClient? = null
    lateinit var delegate: WeakReference<Delegate>

    // ClientData
    var clientNo: Int? = null
        private set
    private var name: String? = null

    fun connect(roomId: String) {

        val url = URI(NetworkConstants.socketUrl + "/" + roomId);

        val weakSelf = makeWeak(this)
        socket = object : WebSocketClient(url) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                weakSelf.get()?.onOpen(handshakedata)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                weakSelf.get()?.onClose(code, reason, remote)
            }

            override fun onMessage(message: String?) {
                weakSelf.get()?.onMessage(message)
            }

            override fun onError(ex: Exception?) {
                weakSelf.get()?.onError(ex)
            }
        }
        if (socket != null) {
            socket?.connect()
            state = State.Opening
        }
    }

    fun disconnect() {
        state = State.Closed
        socket?.close()
    }

    fun isSelf(data: WebSocketMessageDto): ChatConstants.ChatMessageOwner {
        if (data.clientNo != null && clientNo != null) {
            return if (clientNo == data.clientNo) ChatConstants.ChatMessageOwner.MYSELF else ChatConstants.ChatMessageOwner.OTHER
        }
        return ChatConstants.ChatMessageOwner.UNKNOWN
    }

    private fun write(prefix: ChatConstants.MsgPrefix, params: Map<String, Any>) {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
        val json = moshi.adapter<Map<String, Any>>(type).toJson(params)
        write(prefix, json)
    }

    private fun write(prefix: ChatConstants.MsgPrefix, json: String) {
        val jsonData = prefix.value + json
        socket?.send(jsonData)
    }

    //-------------------------------------------------------------------------------------------
    //  Send
    //-------------------------------------------------------------------------------------------
    fun sendJoin(user: UserDto) {
        val map: Map<String, Any> = mapOf(
                "name" to (user.name?.let {it} ?: ""),
                "userId" to (user.id?.let {it} ?: "")
        )
        write(ChatConstants.MsgPrefix.JOINED, map)
    }

    fun sendMessage(message: String) {
        if (clientNo != null && name != null) {
            val data = WebSocketMessageDto(clientNo!!, name!!, message)
            val json = toJson(data)
            write(ChatConstants.MsgPrefix.MESSAGE, json)
        }
    }

    //-------------------------------------------------------------------------------------------
    //  Receive
    //-------------------------------------------------------------------------------------------
    private fun receiveJoined(data: WebSocketActionDto) {
        if (clientNo == null || name == null) {
            for (client in data.clients) {
                if (client.self) {
                    clientNo = client.clientNo
                    name = client.name
                }
            }
        }
        delegate.get()?.onReceiveJoined(data)
    }

    private fun receiveRemoved(data: WebSocketActionDto) {
        delegate.get()?.onReceiveRemoved(data)
    }

    private fun receiveMessage(data: WebSocketMessageDto) {
        delegate.get()?.onReceiveMessage(data)
    }

    private fun receiveError(data: WebSocketErrorDto) {
        delegate.get()?.onReceiveError(data)
    }

    //-------------------------------------------------------------------------------------------
    //  WebSocketClient Callback
    //-------------------------------------------------------------------------------------------
    private fun onOpen(handshakedata: ServerHandshake?) {
        state = State.Opened
        delegate.get()?.onOpen()
    }

    private fun onClose(code: Int, reason: String?, remote: Boolean) {

        state = State.Closed
    }

    private fun onMessage(message: String?) {
        Log.d("message", message)

        if (message != null) {
            try {
                val prefix = ChatConstants.MsgPrefix.fromString(message.substring(0..2))
                val json = message.substring(3)

                when (prefix) {
                    ChatConstants.MsgPrefix.JOINED -> {
                        val data = fromJson<WebSocketActionDto>(json)
                        if (data != null) receiveJoined(data)
                    }
                    ChatConstants.MsgPrefix.REMOVED -> {
                        val data = fromJson<WebSocketActionDto>(json)
                        if (data != null) receiveRemoved(data)
                    }
                    ChatConstants.MsgPrefix.MESSAGE -> {
                        val data = fromJson<WebSocketMessageDto>(json)
                        if (data != null) receiveMessage(data)
                    }
                    ChatConstants.MsgPrefix.ERROR -> {
                        val data = fromJson<WebSocketErrorDto>(json)
                        if (data != null) receiveError(data)
                    }
                }
            } catch (e: Exception) {
                Log.d("valueOf", e.localizedMessage)
            }
        }
    }

    private fun onError(ex: Exception?) {
    }

    interface Delegate {
        fun onOpen()
        fun onClose(code: Int, reason: String?, remote: Boolean)
        fun onReceiveMessage(data: WebSocketMessageDto)
        fun onReceiveJoined(data: WebSocketActionDto)
        fun onReceiveRemoved(data: WebSocketActionDto)
        fun onReceiveError(data: WebSocketErrorDto)
    }

}
