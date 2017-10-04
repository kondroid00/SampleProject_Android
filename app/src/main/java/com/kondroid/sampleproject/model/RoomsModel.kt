package com.kondroid.sampleproject.model

import com.kondroid.sampleproject.request.RequestFactory
import com.kondroid.sampleproject.request.RoomRequest
import io.reactivex.Observable

/**
 * Created by kondo on 2017/10/04.
 */

class RoomsModel {

    private val request = RequestFactory.createRequest<RoomRequest>()

    fun createRoom(params: RoomRequest.CreateParams): Observable<RoomRequest.CreateResult> {
        return request.create(params)
    }

    fun fetchRooms(params: RoomRequest.FetchParams): Observable<RoomRequest.FetchResult> {
        return request.fetch(params)
    }
}