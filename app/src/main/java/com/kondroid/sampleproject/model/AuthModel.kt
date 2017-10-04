package com.kondroid.sampleproject.model

import com.kondroid.sampleproject.request.AuthRequest
import com.kondroid.sampleproject.request.RequestFactory
import io.reactivex.Observable

/**
 * Created by kondo on 2017/10/04.
 */

class AuthModel {

    private val request = RequestFactory.createRequest<AuthRequest>()

    fun login(params: AuthRequest.LoginParams): Observable<AuthRequest.LoginResult> {
        return request.login(params)
    }

}