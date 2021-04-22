package com.android.dogbreedscanner.remote.helper

import okhttp3.Interceptor
import okhttp3.Response

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-22.
 * </prev>
 */
class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
            .newBuilder()
            .addHeader("x-api-key", BuildConfig.API_KEY)
            .build()

        return chain.proceed(req)
    }
}