package com.kondroid.sampleproject.viewmodel

import android.content.Context
import android.databinding.ObservableField
import com.kondroid.sampleproject.auth.AccountManager
import com.kondroid.sampleproject.helper.RxUtils
import com.kondroid.sampleproject.helper.makeWeak
import com.kondroid.sampleproject.helper.validation.Validation
import com.kondroid.sampleproject.model.UsersModel
import com.kondroid.sampleproject.request.UserRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by kondo on 2017/09/28.
 */

class SignUpViewModel(context: Context) : BaseViewModel(context) {
    var nameText: ObservableField<String> = ObservableField("")
    var nameValidationText: ObservableField<String> = ObservableField("")
    var registerButtonEnabled: ObservableField<Boolean> = ObservableField(false)

    lateinit var onTapRegister: () -> Unit

    val userModel = UsersModel()

    override fun initVM() {
        super.initVM()

        //Validation
        val weakSelf = makeWeak(this)
        val nameValid = RxUtils.toObservable(nameText)
                .map { return@map Validation.textLength(context.get(), it, 1, 12) }
                .share()
        val d1 = nameValid.subscribe { weakSelf.get()?.nameValidationText?.set(it) }

        val registerButtonValid = nameValid.map { return@map it == "" }.share()
        val d2 = registerButtonValid.subscribe { weakSelf.get()?.registerButtonEnabled?.set(it) }

        compositeDisposable.addAll(d1, d2)
    }

    fun tapRegister() {
        if (requesting) return
        onTapRegister()
    }

    fun signUp(onSuccess: () -> Unit, onFailed: (e: Throwable) -> Unit) {
        if (requesting) return
        requesting = true

        val weakSelf = makeWeak(this)
        val params = UserRequest.CreateParams(nameText.get())
        val observable = userModel.createUser(params)
        val d = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({t ->
                    weakSelf.get()?.requesting = false
                    AccountManager.token = t.token
                    AccountManager.user = t.user
                    onSuccess()
                },{e ->
                    weakSelf.get()?.requesting = false
                    onFailed(e)
                },{
                    weakSelf.get()?.requesting = false
                })
        compositeDisposable.add(d)
    }


}