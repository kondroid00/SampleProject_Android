package com.kondroid.sampleproject.viewmodel

import android.content.Context
import android.databinding.ObservableField
import android.view.View
import java.lang.ref.WeakReference

/**
 * Created by kondo on 2017/09/28.
 */

open class BaseViewModel(context: Context) {
    val context: WeakReference<Context> = WeakReference(context)
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