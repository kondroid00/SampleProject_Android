package com.kondroid.sampleproject

import android.app.Application
import com.kondroid.sampleproject.auth.AccountManager
import com.kondroid.sampleproject.auth.AccountManager.context
import com.kondroid.sampleproject.constants.NetworkConstants
import com.kondroid.sampleproject.request.RequestFactory

/**
 * Created by kondo on 2017/10/02.
 */

class SampleProjectApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AccountManager.context = applicationContext
        NetworkConstants.context = applicationContext
        RequestFactory.init()
    }
}