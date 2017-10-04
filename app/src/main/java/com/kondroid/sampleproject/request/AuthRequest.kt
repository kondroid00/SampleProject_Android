package com.kondroid.sampleproject.request

import com.kondroid.sampleproject.dto.TokenDto
import com.kondroid.sampleproject.dto.UserDto
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by kondo on 2017/10/04.
 */

interface AuthRequest {

    //------------------------------------------------------------------------------------
    //    Login
    //------------------------------------------------------------------------------------
    class LoginParams(val userId: String?)

    class LoginResult(val user: UserDto?, val token: TokenDto?)

    @Headers("Content-Type: application/json")
    @POST("/login")
    fun login(@Body params: LoginParams): Observable<LoginResult>

}