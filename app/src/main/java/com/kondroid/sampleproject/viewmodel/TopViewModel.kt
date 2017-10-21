package com.kondroid.sampleproject.viewmodel

import android.content.Context
import android.databinding.ObservableField
import android.view.View
import com.kondroid.sampleproject.auth.AccountManager
import com.kondroid.sampleproject.helper.makeWeak
import com.kondroid.sampleproject.model.AuthModel
import com.kondroid.sampleproject.request.AuthRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.operators.single.SingleDoOnSuccess
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by kondo on 2017/09/28.
 */

class TopViewModel(context: Context) : BaseViewModel(context) {
    val startButtonVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    val authModel = AuthModel()

    lateinit var onTapStart: () -> Unit

    fun login(onSuccess: () -> Unit, onFailed: (e: Throwable) -> Unit) {
        if (requesting) return
        requesting = true

        val weakSelf = makeWeak(this)
        val params = AuthRequest.LoginParams(AccountManager.getUserId())
        val observable = authModel.login(params)
        val d = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({t ->
                    weakSelf.get()?.requesting = false
                    AccountManager.token = t.token
                    AccountManager.user = t.user
                    onSuccess()
                }, {e ->
                    weakSelf.get()?.requesting = false
                    onFailed(e)
                }, {
                    weakSelf.get()?.requesting = false
                })
        compositeDisposable.add(d)
    }

    fun tapStart() {
        if (requesting) return
        onTapStart()
    }

}