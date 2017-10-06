package com.kondroid.sampleproject.helper

import java.lang.ref.WeakReference

/**
 * Created by kondo on 2017/10/07.
 */

fun <T> makeWeak(ref: T): WeakReference<T> {
    return WeakReference<T>(ref)
}