package com.android.dogbreedscanner.remote.model.dto

import com.google.gson.annotations.SerializedName

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-22.
 * </prev>
 */
data class ImageDTO(
    @SerializedName("height")
    val height: Int,
    @SerializedName("id")
    val imageId: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
)
