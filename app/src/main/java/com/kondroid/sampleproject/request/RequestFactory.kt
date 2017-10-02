package com.kondroid.sampleproject.request

import android.util.Log
import com.kondroid.sampleproject.constants.NetworkConstants
import com.kondroid.sampleproject.request.user.UserRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by kondo on 2017/10/02.
 */

object RequestFactory {

    lateinit var retrofit: Retrofit

    fun init() {
        val logging = HttpLoggingInterceptor({message -> Log.d("API LOG", message) })
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

        val client = OkHttpClient.Builder().addInterceptor(logging).build()
        retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(NetworkConstants.apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
    }

    inline fun <reified T> createRequest(): T {
        return retrofit.create(T::class.java)
    }


}