package com.android.dogbreedscanner.remote.helper

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-22.
 * </prev>
 */
sealed class ServiceResponse<out SuccessData> {

    data class Success<SuccessData>(val successData: SuccessData) : ServiceResponse<SuccessData>()

    data class Error(val errorData: String) : ServiceResponse<Nothing>()
}
