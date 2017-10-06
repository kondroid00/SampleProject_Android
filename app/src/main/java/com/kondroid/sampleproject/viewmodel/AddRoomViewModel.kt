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

    override fun initVM() {
        super.initVM()

        //Validation
        val nameValid = RxUtils.toObservable(nameText)
                .map { return@map Validation.textLength(context.get(), it, 1, 20) }
                .share()
        val d1 = nameValid.subscribe { nameValidationText.set(it) }

        val themeValid = RxUtils.toObservable(themeText)
                .map { return@map Validation.textLength(context.get(), it, 1, 20) }
                .share()
        val d2 = themeValid.subscribe { themeValidationText.set(it) }

        val createButtonValid = Observables.combineLatest(nameValid, themeValid, {name, theme ->
            return@combineLatest name == "" && theme == ""
        }).share()
        val d3 = createButtonValid.subscribe { createButtonEnabled.set(it) }

        compositeDisposable.addAll(d1, d2, d3)
    }

    fun createRoom(onSuccess: () -> Unit, onFailed: (e: Throwable) -> Unit) {
        if (requesting) return
        requesting = true

        val params = RoomRequest.CreateParams(nameText.get(), themeText.get())
        val observable = roomModel.createRoom(params)
        val d = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({t ->
                    requesting = false
                    onSuccess()
                },{e ->
                    requesting = false
                    onFailed(e)
                },{
                    requesting = false
                })
        compositeDisposable.add(d)
    }

    fun tapCreate() {
        if (requesting) return
        onTapCreate()
    }
}