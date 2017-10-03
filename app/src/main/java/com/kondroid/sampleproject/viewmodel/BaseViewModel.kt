package com.kondroid.sampleproject.viewmodel

import android.databinding.ObservableField
import android.view.View

/**
 * Created by kondo on 2017/09/28.
 */

open class BaseViewModel {
    val progressBarVisibility: ObservableField<Int> = ObservableField(View.INVISIBLE)
    var requesting: Boolean
        get() {
            return progressBarVisibility.get() == View.VISIBLE
        }
        set(value) {
            if (value) {
                progressBarVisibility.set(View.VISIBLE)
            } else {
                progressBarVisibility.set(View.INVISIBLE)
            }
        }
}