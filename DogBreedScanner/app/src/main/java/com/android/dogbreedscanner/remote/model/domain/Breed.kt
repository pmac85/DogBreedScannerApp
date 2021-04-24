package com.android.dogbreedscanner.remote.model.domain

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-22.
 * </prev>
 */
data class Breed(
    val bredFor: String?,
    val breedGroup: String?,
    val countryCode: String?,
    val height: Measure?,
    val breedId: Int,
    val image: Image?,
    val lifeSpan: String?,
    val name: String?,
    val referenceImageId: String?,
    val temperament: String?,
    val weight: Measure?
)
