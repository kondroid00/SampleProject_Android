package com.kondroid.sampleproject.constants

import android.content.Context
import com.kondroid.sampleproject.R

/**
 * Created by kondo on 2017/10/02.
 */

object ChatConstants {

    enum class MsgPrefix(val value: String) {
        JOINED("001"),
        REMOVED("002"),
        MESSAGE("003"),
        ERROR("999");

        companion object {
            private val map = MsgPrefix.values().associateBy(MsgPrefix::value)
            fun fromString(prefix: String) = map[prefix]
        }
    }

    enum class ChatMessageOwner {
        MYSELF,
        OTHER,
        UNKNOWN
    }
}