package com.kondroid.sampleproject.request

import com.kondroid.sampleproject.auth.AccountManager


/**
 * Created by kondo on 2017/10/01.
 */

open class BaseAuthParams {
    var token: String? = AccountManager.token?.token
}