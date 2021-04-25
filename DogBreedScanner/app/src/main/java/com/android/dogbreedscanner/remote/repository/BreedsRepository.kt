package com.android.dogbreedscanner.remote.repository

import com.android.dogbreedscanner.remote.api.BreedsApi
import com.android.dogbreedscanner.remote.helper.ResponseHandler
import com.android.dogbreedscanner.remote.helper.ServiceResponse
import com.android.dogbreedscanner.remote.model.domain.Breed
import com.android.dogbreedscanner.remote.model.domain.Image
import com.android.dogbreedscanner.remote.model.domain.ImageItem
import com.android.dogbreedscanner.remote.model.domain.Measure
import com.android.dogbreedscanner.remote.model.dto.BreedDTO
import com.android.dogbreedscanner.remote.model.dto.ImageItemDTO
import com.android.dogbreedscanner.remote.model.input.GetBreedsInput
import com.android.dogbreedscanner.remote.model.input.GetImageByBreedId
import com.android.dogbreedscanner.remote.model.input.SearchByBreedInput

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-22.
 * </prev>
 */
class BreedsRepository(
    private val breedsApi: BreedsApi,
    private val responseHandler: ResponseHandler
) {

    suspend fun getBreeds(getBreedsInput: GetBreedsInput): ServiceResponse<List<Breed>> {
        return try {
            val response = breedsApi.getBreeds(
                getBreedsInput.attachBreed,
                getBreedsInput.page,
                getBreedsInput.limit
            )

            responseHandler.handleSuccess(response.map { it.mapToOutput() })
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    suspend fun searchByBreed(searchByBreedInput: SearchByBreedInput): ServiceResponse<List<Breed>> {
        return try {
            val response = breedsApi.searchByBreed(
                searchByBreedInput.breedName
            )

            responseHandler.handleSuccess(response.map { it.mapToOutput() })
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    suspend fun getImageByBreedId(getImageByBreedId: GetImageByBreedId): ServiceResponse<List<ImageItem>> {
        return try {
            val response = breedsApi.getImageByBreedId(
                getImageByBreedId.breedId
            )

            responseHandler.handleSuccess(response.map { it.mapToOutput() })
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    private fun BreedDTO.mapToOutput(): Breed = Breed(
        this.bredFor,
        this.breedGroup,
        this.countryCode,
        this.height?.let { Measure(it.imperial, it.metric) },
        this.breedId,
        this.image?.let { Image(it.height, it.imageId, it.url, it.width) },
        this.lifeSpan,
        this.name,
        this.referenceImageId,
        this.temperament,
        this.weight?.let { Measure(it.imperial, it.metric) }
    )

    private fun ImageItemDTO.mapToOutput(): ImageItem = ImageItem(
        this.height,
        this.imageId,
        this.url, this.width,
        this.breeds?.map {
            it.mapToOutput()
        }
    )
}