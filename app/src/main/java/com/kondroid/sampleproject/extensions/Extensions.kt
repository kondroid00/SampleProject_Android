package com.kondroid.sampleproject.extensions

import java.util.*

/**
 * Created by kondo on 2017/10/01.
 */

//-----------------------------------------------------------------------
//   Date
//-----------------------------------------------------------------------
fun Date.currentTimeNanoMillis(): Long {
    return System.currentTimeMillis() * 1000L
}