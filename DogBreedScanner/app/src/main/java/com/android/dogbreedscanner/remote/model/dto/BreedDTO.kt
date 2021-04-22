package com.android.dogbreedscanner.remote.model.dto

import com.google.gson.annotations.SerializedName

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-22.
 * </prev>
 */
data class BreedDTO(
    @SerializedName("bred_for")
    val bredFor: String?,
    @SerializedName("breed_group")
    val breedGroup: String?,
    @SerializedName("country_code")
    val countryCode: String?,
    @SerializedName("height")
    val height: MeasureDTO,
    @SerializedName("id")
    val breedId: Int,
    @SerializedName("image")
    val image: ImageDTO?,
    @SerializedName("life_span")
    val lifeSpan: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("reference_image_id")
    val referenceImageId: String,
    @SerializedName("temperament")
    val temperament: String,
    @SerializedName("weight")
    val weight: MeasureDTO
)
