package com.kondroid.sampleproject.viewmodel

import android.content.Context
import android.databinding.ObservableField

/**
 * Created by kondo on 2017/10/19.
 */

class ChatViewModel(context: Context) : BaseViewModel(context) {
    var inputText: ObservableField<String> = ObservableField("")

    override fun initVM() {
        super.initVM()
    }

    fun tapSend() {

    }
}