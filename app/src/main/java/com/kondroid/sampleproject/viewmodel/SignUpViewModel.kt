package com.kondroid.sampleproject.viewmodel

import android.databinding.ObservableField
import com.kondroid.sampleproject.auth.AccountManager
import com.kondroid.sampleproject.model.UsersModel
import com.kondroid.sampleproject.request.user.UserRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by kondo on 2017/09/28.
 */

class SignUpViewModel : BaseViewModel() {
    var nameText: ObservableField<String> = ObservableField("")
    var nameValidationText: ObservableField<String> = ObservableField("")

    lateinit var signUpOnSuccess: () -> Unit
    lateinit var signUpOnFailed: (Throwable) -> Unit

    val userModel = UsersModel()

    fun tapRegister() {
        signUp()
    }

    fun signUp() {
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
                        signUpOnSuccess()
                    }

                    override fun onError(e: Throwable) {
                        requesting = false
                        signUpOnFailed(e)
                    }
                })
    }


}