package com.kondroid.sampleproject.viewmodel

import android.databinding.ObservableField
import android.view.View

/**
 * Created by kondo on 2017/09/28.
 */

open class BaseViewModel {
    val progressBarVisibility: ObservableField<Int> = ObservableField(View.INVISIBLE)
}