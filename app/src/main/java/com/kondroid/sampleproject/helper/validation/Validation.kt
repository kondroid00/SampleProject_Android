package com.kondroid.sampleproject.helper.validation

import android.content.Context
import com.kondroid.sampleproject.R

/**
 * Created by kondo on 2017/10/06.
 */

object Validation {

    fun textLength(context: Context?, text: String, min: Int? = null, max: Int? = null): String {

        if (context == null) return "エラー"

        val count = text.length

        var minResult = false
        min?.let {
            if (min <= count) minResult = true
        }

        var maxResult = false
        max?.let {
            if (max >= count) maxResult = true
        }

        if (min != null && max != null && !(minResult && maxResult)) {
            return context.getString(R.string.validation_text_length_min_max, min, max)
        }
        if (min != null && !minResult) {
            return context.getString(R.string.validation_text_length_min, min)
        }
        if (max != null && !maxResult) {
            return context.getString(R.string.validation_text_length_max, max)
        }

        // RxJava2ではnullが許容されないので問題がないときは空文字を返す
        return ""
    }
}