package com.kondroid.sampleproject.viewmodel

import android.databinding.ObservableField
import android.view.View

/**
 * Created by kondo on 2017/09/28.
 */

class TopViewModel : BaseViewModel() {
    val startButtonVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    lateinit var onTapStart: () -> Unit
    

    fun login() {

    }

    fun tapStart() {
        onTapStart()
    }

}