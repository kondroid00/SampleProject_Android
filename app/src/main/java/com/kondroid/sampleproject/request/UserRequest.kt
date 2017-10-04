package com.kondroid.sampleproject.request

import com.kondroid.sampleproject.dto.TokenDto
import com.kondroid.sampleproject.dto.UserDto
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by kondo on 2017/10/01.
 */

interface UserRequest {

    //------------------------------------------------------------------------------------
    //    Create
    //------------------------------------------------------------------------------------

    class CreateParams(val name: String?) : BaseAuthParams()

    class CreateResult(val user: UserDto?,
                       val token: TokenDto?)

    @Headers("Content-Type: application/json")
    @POST("/user/create")
    fun create(@Body params: CreateParams): Observable<CreateResult>


}