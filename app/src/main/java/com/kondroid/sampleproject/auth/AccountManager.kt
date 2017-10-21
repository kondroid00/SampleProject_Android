package com.kondroid.sampleproject.auth

import android.content.Context
import android.preference.PreferenceManager
import com.kondroid.sampleproject.dto.TokenDto
import com.kondroid.sampleproject.dto.UserDto
import com.kondroid.sampleproject.extensions.currentTimeNanoMillis
import java.util.*

/**
 * Created by kondo on 2017/10/01.
 */

object AccountManager {

    private val UDTokenKey = "token"
    private val UDUserIdKey = "user_id"
    lateinit var context: Context

    var token: TokenDto? = null
        set(value) {
            if(value != null) {
                if (value.expiredAt!! <= Date().currentTimeNanoMillis()) {
                    field = null
                } else {
                    field = value
                }
            }
        }

    var user: UserDto? = null
        set(value) {
            value?.let {
                val pref = PreferenceManager.getDefaultSharedPreferences(context)
                var editor = pref!!.edit()
                editor.putString(UDUserIdKey, it.id)
                editor.commit()
                field = it
            }
        }

    fun hasUserId(): Boolean {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        if (pref.getString(UDUserIdKey, "") != "") {
            return true
        }
        return false
    }

    fun getUserId(): String {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        return pref.getString(UDUserIdKey, "")
    }

}