package com.kondroid.sampleproject.request

import com.kondroid.sampleproject.dto.RoomDto
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by kondo on 2017/10/04.
 */

interface RoomRequest {

    //------------------------------------------------------------------------------------
    //    Create
    //------------------------------------------------------------------------------------
    class CreateParams(val name: String?,
                       val theme: String?) : BaseAuthParams()

    class CreateResult(val room: RoomDto?)

    @Headers("Content-Type: application/json")
    @POST("/room/create")
    fun create(@Body params: CreateParams): Observable<CreateResult>



    //------------------------------------------------------------------------------------
    //    Fetch
    //------------------------------------------------------------------------------------
    class FetchParams() : BaseAuthParams()

    class FetchResult(val rooms: List<RoomDto>?)

    @Headers("Content-Type: application/json")
    @POST("/room")
    fun fetch(@Body params: FetchParams): Observable<FetchResult>


}