package com.android.dogbreedscanner.ui.breedDetails

import java.io.Serializable

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-22.
 * </prev>
 */
data class BreedDetailsData(
    val name: String,
    val breedGroup: String?,
    val countryCode: String?,
    val temperament: String?,
    val url: String?,
    val lifeSpan: String?
) : Serializable
