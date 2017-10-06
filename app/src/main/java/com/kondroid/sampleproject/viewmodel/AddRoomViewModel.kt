package com.kondroid.sampleproject.viewmodel

import android.content.Context
import android.databinding.ObservableField
import com.kondroid.sampleproject.helper.RxUtils
import com.kondroid.sampleproject.helper.validation.Validation
import com.kondroid.sampleproject.model.RoomsModel
import com.kondroid.sampleproject.request.RoomRequest
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.combineLatest
import io.reactivex.schedulers.Schedulers

/**
 * Created by kondo on 2017/10/05.
 */

class AddRoomViewModel(context: Context) : BaseViewModel(context) {
    var nameText: ObservableField<String> = ObservableField("")
    var themeText: ObservableField<String> = ObservableField("")
    var nameValidationText: ObservableField<String> = ObservableField("")
    var themeValidationText: ObservableField<String> = ObservableField("")
    var createButtonEnabled: ObservableField<Boolean> = ObservableField(false)

    val roomModel = RoomsModel()

    lateinit var onTapCreate: () -> Unit

    init {
        //Validation
        val nameValid = RxUtils.toObservable(nameText)
                .map { return@map Validation.textLength(context, it, 1, 20) }
                .share()
        nameValid.subscribe { nameValidationText.set(it) }

        val themeValid = RxUtils.toObservable(themeText)
                .map { return@map Validation.textLength(context, it, 1, 20) }
                .share()
        themeValid.subscribe { themeValidationText.set(it) }

        val createButtonValid = Observables.combineLatest(nameValid, themeValid, {name, theme ->
            return@combineLatest name == "" && theme == ""
        }).share()
        createButtonValid.subscribe { createButtonEnabled.set(it) }
    }

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