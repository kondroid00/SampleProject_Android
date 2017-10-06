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

    init {
        //Validation
        val weakSelf = makeWeak(this)
        val nameValid = RxUtils.toObservable(nameText)
                .map { return@map Validation.textLength(context, it, 1, 12) }
                .share()
        nameValid.subscribe { weakSelf.get()?.nameValidationText?.set(it) }

        val registerButtonValid = nameValid.map { return@map it == "" }.share()
        registerButtonValid.subscribe { weakSelf.get()?.registerButtonEnabled?.set(it) }
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
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<UserRequest.CreateResult>() {
                    override fun onComplete() {
                        weakSelf.get()?.requesting = false
                    }

                    override fun onNext(t: UserRequest.CreateResult) {
                        weakSelf.get()?.requesting = false
                        AccountManager.token = t.token
                        AccountManager.user = t.user
                        onSuccess()
                    }

                    override fun onError(e: Throwable) {
                        weakSelf.get()?.requesting = false
                        onFailed(e)
                    }
                })
    }


}