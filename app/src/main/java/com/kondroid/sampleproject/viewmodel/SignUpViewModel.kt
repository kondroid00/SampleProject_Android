package com.kondroid.sampleproject.viewmodel

import android.content.Context
import android.databinding.ObservableField
import com.kondroid.sampleproject.auth.AccountManager
import com.kondroid.sampleproject.helper.RxUtils
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
        val nameValid = RxUtils.toObservable(nameText)
                .map { return@map Validation.textLength(context, it, 1, 12) }
                .share()
        nameValid.subscribe { nameValidationText.set(it) }

        val registerButtonValid = nameValid.map { return@map it == "" }.share()
        registerButtonValid.subscribe { registerButtonEnabled.set(it) }
    }

    fun tapRegister() {
        if (requesting) return
        onTapRegister()
    }

    fun signUp(onSuccess: () -> Unit, onFailed: (e: Throwable) -> Unit) {
        if (requesting) return
        requesting = true

        val params = UserRequest.CreateParams(nameText.get())
        val observable = userModel.createUser(params)
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<UserRequest.CreateResult>() {
                    override fun onComplete() {
                        requesting = false
                    }

                    override fun onNext(t: UserRequest.CreateResult) {
                        requesting = false
                        AccountManager.token = t.token
                        AccountManager.user = t.user
                        onSuccess()
                    }

                    override fun onError(e: Throwable) {
                        requesting = false
                        onFailed(e)
                    }
                })
    }


}