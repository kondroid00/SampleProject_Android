package com.kondroid.sampleproject.helper

import com.kondroid.sampleproject.dto.websocket.WebSocketMessageDto
import com.squareup.moshi.Moshi
import java.lang.ref.WeakReference

/**
 * Created by kondo on 2017/10/07.
 */

fun <T> makeWeak(ref: T): WeakReference<T> {
    return WeakReference<T>(ref)
}

inline fun <reified T> toJson(data: T): String {
    return Moshi.Builder().build().adapter(T::class.java).toJson(data)
}

inline fun <reified T> fromJson(json: String): T? {
    return Moshi.Builder().build().adapter(T::class.java).fromJson(json)
}