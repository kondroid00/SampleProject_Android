package com.kondroid.sampleproject.helper

import android.databinding.ObservableField
import io.reactivex.Observable
import android.databinding.Observable as DataBindingObservable

object RxUtils {

    fun <T> toObservable(observableField: ObservableField<T>): Observable<T> {
        return Observable.create { subscriber ->
                observableField.addOnPropertyChangedCallback(object : DataBindingObservable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(p0: android.databinding.Observable?, p1: Int) {
                        try {
                            subscriber.onNext((p0 as ObservableField<T>).get())
                        } catch (e: Throwable) {
                            subscriber.onError(e)
                        }
                    }
                })
        }
    }
}

