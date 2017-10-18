package com.kondroid.sampleproject.viewmodel

import android.content.Context
import android.databinding.ObservableField
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

/**
 * Created by kondo on 2017/09/28.
 */

open class BaseViewModel(context: Context) {
    val progressBarVisibility: ObservableField<Int> = ObservableField(View.INVISIBLE)
    val context: WeakReference<Context> = WeakReference(context)
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()
    protected var requesting: Boolean
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

    open fun initVM() {

    }

    open fun release() {
        compositeDisposable.clear()
    }
}