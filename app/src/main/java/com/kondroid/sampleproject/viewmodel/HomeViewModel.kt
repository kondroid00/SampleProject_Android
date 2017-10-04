package com.kondroid.sampleproject.viewmodel

import com.kondroid.sampleproject.auth.AccountManager
import com.kondroid.sampleproject.dto.RoomDto
import com.kondroid.sampleproject.model.RoomsModel
import com.kondroid.sampleproject.request.RoomRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by kondo on 2017/10/04.
 */

class HomeViewModel : BaseViewModel() {

    var rooms: List<RoomDto> = listOf()

    val roomModel = RoomsModel()

    lateinit var fetchRoomOnSuccess: () -> Unit
    lateinit var fetchRoomOnFailed: (Throwable) -> Unit

    fun fetchRooms(onSuccess: () -> Unit, onFailed: (e: Throwable) -> Unit) {
        if (requesting) return
        requesting = true

        val params = RoomRequest.FetchParams()
        val observable = roomModel.fetchRooms(params)
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<RoomRequest.FetchResult>() {
                    override fun onComplete() {
                        requesting = false
                    }

                    override fun onNext(t: RoomRequest.FetchResult) {
                        requesting = false
                        rooms = t.rooms?.let {it} ?: listOf()
                        onSuccess()
                    }

                    override fun onError(e: Throwable) {
                        requesting = false
                        onFailed(e)
                    }

                })
    }
}