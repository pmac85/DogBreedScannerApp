package com.android.dogbreedscanner.remote.helper

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-22.
 * </prev>
 */
object RetrofitBuilderHelper {

    fun getRetrofitInstance(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(getOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create(getGsonBuilder()))
        .build()

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(getLoggingInterceptor())
            .build()
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC
        return logger
    }

    private fun getGsonBuilder() = GsonBuilder().setLenient().create()

}