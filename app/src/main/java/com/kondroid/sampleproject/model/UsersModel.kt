package com.kondroid.sampleproject.model

import com.kondroid.sampleproject.request.RequestFactory
import com.kondroid.sampleproject.request.user.UserRequest
import io.reactivex.Observable

/**
 * Created by kondo on 2017/10/02.
 */

class UsersModel {

    private val request = RequestFactory.createRequest<UserRequest>()

    fun createUser(params: UserRequest.CreateParams): Observable<UserRequest.CreateResult> {
        return request.create(params)
    }
}