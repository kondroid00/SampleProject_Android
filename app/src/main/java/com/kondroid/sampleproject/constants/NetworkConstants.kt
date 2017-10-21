package com.kondroid.sampleproject.constants

import android.content.Context
import com.kondroid.sampleproject.R

/**
 * Created by kondo on 2017/10/02.
 */

object NetworkConstants {

    lateinit var context: Context

    val apiUrl: String
        get(): String {
            return context.getString(R.string.const_api_server_scheme) + context.getString(R.string.const_api_server_host)
        }

    val socketUrl: String
        get(): String {
            return context.getString(R.string.const_socket_server_scheme) + context.getString(R.string.const_socket_server_host)
        }
}