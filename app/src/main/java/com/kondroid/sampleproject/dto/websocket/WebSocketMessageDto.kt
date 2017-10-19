package com.kondroid.sampleproject.dto.websocket

/**
 * Created by kondo on 2017/10/19.
 */

data class WebSocketMessageDto(val clientNo: Int,
                               val name: String,
                               val message: String)