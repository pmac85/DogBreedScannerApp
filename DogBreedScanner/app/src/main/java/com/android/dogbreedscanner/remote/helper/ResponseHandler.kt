package com.android.dogbreedscanner.remote.helper

import retrofit2.HttpException
import java.net.SocketTimeoutException

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-22.
 * </prev>
 */
enum class ErrorCodes(val code: Int) {
    SocketTimeOut(-1)
}

class ResponseHandler {
    fun <S> handleSuccess(data: S): ServiceResponse<S> {
        return ServiceResponse.Success(data)
    }

    fun handleException(e: Exception): ServiceResponse<Nothing> {
        return when (e) {
            is HttpException -> ServiceResponse.Error(getErrorMessage(e.code()))
            is SocketTimeoutException -> ServiceResponse.Error(getErrorMessage(ErrorCodes.SocketTimeOut.code))
            else -> ServiceResponse.Error(getErrorMessage(Int.MAX_VALUE))
        }
    }

    private fun getErrorMessage(code: Int): String {
        return when (code) {
            ErrorCodes.SocketTimeOut.code -> "Timeout"
            401 -> "Unauthorised"
            404 -> "Not found"
            else -> "Something went wrong"
        }
    }
}
