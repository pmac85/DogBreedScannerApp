package com.android.dogbreedscanner.remote.model.domain

import java.io.Serializable

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-22.
 * </prev>
 */
data class Measure(
    val imperial: String,
    val metric: String
) : Serializable
