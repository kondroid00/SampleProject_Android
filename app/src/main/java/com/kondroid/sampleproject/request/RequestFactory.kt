package com.kondroid.sampleproject.request

import android.util.Log
import com.kondroid.sampleproject.constants.NetworkConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
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
                .baseUrl(NetworkConstants.apiUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    inline fun <reified T> createRequest(): T {
        return retrofit.create(T::class.java)
    }


}