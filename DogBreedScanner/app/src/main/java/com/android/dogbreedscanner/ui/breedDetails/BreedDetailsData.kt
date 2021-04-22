package com.android.dogbreedscanner.ui.breedDetails

import com.android.dogbreedscanner.remote.model.domain.Measure
import java.io.Serializable

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-22.
 * </prev>
 */
data class BreedDetailsData(
    val bredFor: String?,
    val breedGroup: String?,
    val countryCode: String?,
    val height: Measure,
    val imageUrl: String?,
    val lifeSpan: String,
    val name: String,
    val temperament: String,
    val weight: Measure
) : Serializable
