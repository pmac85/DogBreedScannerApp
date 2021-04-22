package com.android.dogbreedscanner.remote.model.input

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-22.
 * </prev>
 */
data class GetBreedsInput(
    val attachBreed: String? = null,
    val page: String? = null,
    val limit: String? = null
)
