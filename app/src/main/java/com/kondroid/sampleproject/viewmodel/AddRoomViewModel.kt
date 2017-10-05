package com.kondroid.sampleproject.viewmodel

import android.databinding.ObservableField
import com.kondroid.sampleproject.model.RoomsModel
import com.kondroid.sampleproject.request.RoomRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by kondo on 2017/10/05.
 */

class AddRoomViewModel : BaseViewModel() {
    var nameText: ObservableField<String> = ObservableField("")
    var themeText: ObservableField<String> = ObservableField("")
    var nameValidationText: ObservableField<String> = ObservableField("")
    var themeValidationText: ObservableField<String> = ObservableField("")

    val roomModel = RoomsModel()

    lateinit var onTapCreate: () -> Unit

    fun createRoom(onSuccess: () -> Unit, onFailed: (e: Throwable) -> Unit) {
        if (requesting) return
        requesting = true

        val params = RoomRequest.CreateParams(nameText.get(), themeText.get())
        val observable = roomModel.createRoom(params)
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<RoomRequest.CreateResult>() {
                    override fun onNext(t: RoomRequest.CreateResult) {
                        requesting = false
                        onSuccess()
                    }

                    override fun onError(e: Throwable) {
                        requesting = false
                        onFailed(e)
                    }

                    override fun onComplete() {
                        requesting = false
                    }
                })

    }

    fun tapCreate() {
        if (requesting) return
        onTapCreate()
    }
}