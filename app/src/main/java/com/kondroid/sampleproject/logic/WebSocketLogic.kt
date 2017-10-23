package com.kondroid.sampleproject.logic

import android.content.Context
import android.os.Handler
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
import kotlinx.coroutines.experimental.sync.Mutex
import org.java_websocket.WebSocket
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.framing.CloseFrame
import org.java_websocket.framing.Framedata
import org.java_websocket.handshake.ServerHandshake
import org.jetbrains.anko.runOnUiThread
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.URI


/**
 * Created by kondo on 2017/10/20.
 */

class WebSocketLogic(context: Context) {

    enum class State {
        Opening,
        Opened,
        Closed
    }

    // State
    var state = State.Closed
        private set

    // Context
    private val context: WeakReference<Context> = WeakReference(context)

    // WebSocket
    private var socket: WebSocketClient? = null
    lateinit var delegate: WeakReference<Delegate>

    // ClientData
    var clientNo: Int? = null
        private set
    private var name: String? = null

    // PingPong
    private val PingPongInterval: Long = 5000 // ５秒間Pongが帰ってこなかったら切断
    private val CheckPingPongInterval: Long = 1000 // １秒間ごとにPingPongのチェックをする
    private var pongReceiveTime: Long? = null // 最後にPongを受け取った時間
    private val pingPongHandler = Handler()
    private lateinit var pingPongRunnable: Runnable

    fun connect(roomId: String) {

        val url = URI(NetworkConstants.socketUrl + "/" + roomId)

        val weakSelf = makeWeak(this)
        socket = object : WebSocketClient(url, Draft_6455(), null, 5000) {
            init {
                // クラッシュするのでPingPongの処理を止めて自前で管理する
                stopConnectionLostTimer()
            }

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

            override fun onWebsocketPong(conn: WebSocket?, f: Framedata?) {
                super.onWebsocketPong(conn, f)
                weakSelf.get()?.onPong()
            }
        }

        socket?.let {
            it.connect()
            state = State.Opening

            // PingPong
            pingPongRunnable = Runnable {
                weakSelf.get()?.checkPingPong()
                pingPongHandler.removeCallbacks(pingPongRunnable)
                if (weakSelf.get()?.state != State.Closed) pingPongHandler.postDelayed(pingPongRunnable, CheckPingPongInterval)
            }
            pingPongHandler.postDelayed(pingPongRunnable, CheckPingPongInterval)
        }
    }

    fun disconnect() {
        state = State.Closed
        socket?.close()
        pingPongHandler.removeCallbacks(pingPongRunnable)
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
        if (state == State.Opened) {
            val jsonData = prefix.value + json
            socket?.send(jsonData)
        }
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
        context.get()?.runOnUiThread {
            delegate.get()?.onReceiveJoined(data)
        }
    }

    private fun receiveRemoved(data: WebSocketActionDto) {
        context.get()?.runOnUiThread {
            delegate.get()?.onReceiveRemoved(data)
        }
    }

    private fun receiveMessage(data: WebSocketMessageDto) {
        context.get()?.runOnUiThread {
            delegate.get()?.onReceiveMessage(data)
        }
    }

    private fun receiveError(data: WebSocketErrorDto) {
        context.get()?.runOnUiThread {
            delegate.get()?.onReceiveError(data)
        }
    }

    //-------------------------------------------------------------------------------------------
    //  PingPong
    //-------------------------------------------------------------------------------------------
    private fun checkPingPong() {
        // Ping送信
        if (state == State.Opened) socket?.sendPing()

        // Pongが帰ってきてるかチェック
        pongReceiveTime?.let {
            val diff = System.currentTimeMillis() - it
            if (diff > PingPongInterval) {
                disconnect()
                delegate.get()?.onClose(CloseFrame.GOING_AWAY, null, true)
            }
        }

    }

    //-------------------------------------------------------------------------------------------
    //  WebSocketClient Callback
    //-------------------------------------------------------------------------------------------
    private fun onOpen(handshakedata: ServerHandshake?) {
        context.get()?.runOnUiThread {
            state = State.Opened
            delegate.get()?.onOpen()
        }
    }

    private fun onClose(code: Int, reason: String?, remote: Boolean) {
        context.get()?.runOnUiThread {
            delegate.get()?.onClose(code, reason, remote)
            state = State.Closed
        }
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
                        context.get()?.runOnUiThread {
                            if (data != null) receiveJoined(data)
                        }
                    }
                    ChatConstants.MsgPrefix.REMOVED -> {
                        val data = fromJson<WebSocketActionDto>(json)
                        context.get()?.runOnUiThread {
                            if (data != null) receiveRemoved(data)
                        }
                    }
                    ChatConstants.MsgPrefix.MESSAGE -> {
                        val data = fromJson<WebSocketMessageDto>(json)
                        context.get()?.runOnUiThread {
                            if (data != null) receiveMessage(data)
                        }
                    }
                    ChatConstants.MsgPrefix.ERROR -> {
                        val data = fromJson<WebSocketErrorDto>(json)
                        context.get()?.runOnUiThread {
                            if (data != null) receiveError(data)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("valueOf", e.localizedMessage)
            }
        }
    }

    private fun onError(ex: Exception?) {
        Log.d("Socket Error", ex?.let { it.message?.let {it} ?: ""} ?: "")
    }

    private fun onPong() {
        context.get()?.runOnUiThread {
            pongReceiveTime = System.currentTimeMillis()
        }
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
