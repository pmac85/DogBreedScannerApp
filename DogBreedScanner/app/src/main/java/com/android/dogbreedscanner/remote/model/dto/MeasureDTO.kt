package com.android.dogbreedscanner.remote.model.dto

import com.google.gson.annotations.SerializedName

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-22.
 * </prev>
 */
data class MeasureDTO(
    @SerializedName("imperial")
    val imperial: String,
    @SerializedName("metric")
    val metric: String
)
