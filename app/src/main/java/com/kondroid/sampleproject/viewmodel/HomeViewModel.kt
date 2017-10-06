package com.kondroid.sampleproject.viewmodel

import android.content.Context
import com.kondroid.sampleproject.auth.AccountManager
import com.kondroid.sampleproject.dto.RoomDto
import com.kondroid.sampleproject.helper.makeWeak
import com.kondroid.sampleproject.model.RoomsModel
import com.kondroid.sampleproject.request.RoomRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by kondo on 2017/10/04.
 */

class HomeViewModel(context: Context) : BaseViewModel(context) {
    var rooms: List<RoomDto> = listOf()

    val roomModel = RoomsModel()

    fun fetchRooms(onSuccess: () -> Unit, onFailed: (e: Throwable) -> Unit) {
        if (requesting) return
        requesting = true

        val weakSelf = makeWeak(this)
        val params = RoomRequest.FetchParams()
        val observable = roomModel.fetchRooms(params)
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<RoomRequest.FetchResult>() {
                    override fun onComplete() {
                        weakSelf.get()?.requesting = false
                    }

                    override fun onNext(t: RoomRequest.FetchResult) {
                        weakSelf.get()?.requesting = false
                        weakSelf.get()?.rooms = t.rooms?.let {it} ?: listOf()
                        onSuccess()
                    }

                    override fun onError(e: Throwable) {
                        weakSelf.get()?.requesting = false
                        onFailed(e)
                    }

                })
    }
}