package com.kondroid.sampleproject.viewmodel

import android.databinding.ObservableField
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

    val userModel = UsersModel()

    fun tapRegister() {
        signUp()
    }

    fun signUp() {
        val params = UserRequest.CreateParams(nameText.get())
        val observable = userModel.createUser(params)
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<UserRequest.CreateResult>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: UserRequest.CreateResult) {

                    }

                    override fun onError(e: Throwable) {

                    }

                })
    }


}