package com.android.dogbreedscanner.remote.api

import com.android.dogbreedscanner.remote.model.dto.BreedDTO
import com.android.dogbreedscanner.remote.model.dto.ImageItemDTO
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-22.
 * </prev>
 */
interface BreedsApi {

    @GET("$API_VERSION/breeds")
    suspend fun getBreeds(
        @Query("attach_breed") attachBreed: String?,
        @Query("page") page: String?,
        @Query("limit") limit: String?
    ): List<BreedDTO>

    @GET("$API_VERSION/breeds/search")
    suspend fun searchByBreed(
        @Query("q") breedName: String
    ): List<BreedDTO>

    @GET("$API_VERSION/images/search")
    suspend fun getImageByBreedId(
        @Query("breed_id") breedId: String
    ): List<ImageItemDTO>

    companion object {
        private const val API_VERSION = "v1"
    }
}