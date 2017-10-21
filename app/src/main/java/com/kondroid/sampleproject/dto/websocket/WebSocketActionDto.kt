package com.kondroid.sampleproject.dto.websocket

/**
 * Created by kondo on 2017/10/19.
 */

data class WebSocketActionDto(val clients: List<WebSocketActionDto.Client>) {
    data class Client(val clientNo: Int,
                      val name: String?,
                      val action: Boolean,
                      val self: Boolean)
}