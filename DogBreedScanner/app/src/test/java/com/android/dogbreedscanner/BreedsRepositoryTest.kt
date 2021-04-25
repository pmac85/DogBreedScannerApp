package com.android.dogbreedscanner

import com.android.dogbreedscanner.remote.api.BreedsApi
import com.android.dogbreedscanner.remote.helper.ResponseHandler
import com.android.dogbreedscanner.remote.helper.ServiceResponse
import com.android.dogbreedscanner.remote.model.domain.Breed
import com.android.dogbreedscanner.remote.model.domain.ImageItem
import com.android.dogbreedscanner.remote.model.dto.BreedDTO
import com.android.dogbreedscanner.remote.model.dto.ImageItemDTO
import com.android.dogbreedscanner.remote.model.input.GetBreedsInput
import com.android.dogbreedscanner.remote.model.input.GetImageByBreedId
import com.android.dogbreedscanner.remote.model.input.SearchByBreedInput
import com.android.dogbreedscanner.remote.repository.BreedsRepository
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-25.
 * </prev>
 */
@RunWith(JUnit4::class)
class BreedsRepositoryTest {
    private val responseHandler = ResponseHandler()
    private lateinit var breedsApi: BreedsApi
    private lateinit var breedsRepository: BreedsRepository
    private val getBreedsDTO = arrayListOf(
        BreedDTO(
            "breedFor",
            "breedGroup",
            "countryCode",
            null,
            1,
            null,
            "lifeSpan",
            "name",
            null,
            null,
            null
        ),
        BreedDTO(
            "breedFor",
            "breedGroup",
            "countryCode",
            null,
            2,
            null,
            "lifeSpan",
            "name",
            null,
            null,
            null
        )
    )
    private val getBreedsOutput = arrayListOf(
        Breed(
            "breedFor",
            "breedGroup",
            "countryCode",
            null,
            1,
            null,
            "lifeSpan",
            "name",
            null,
            null,
            null
        ),
        Breed(
            "breedFor",
            "breedGroup",
            "countryCode",
            null,
            2,
            null,
            "lifeSpan",
            "name",
            null,
            null,
            null
        )
    )
    private val searchByNameDTO = arrayListOf(
        BreedDTO(
            "breedFor",
            "breedGroup",
            "countryCode",
            null,
            1,
            null,
            "lifeSpan",
            "Border Collie",
            null,
            null,
            null
        ),
        BreedDTO(
            "breedFor",
            "breedGroup",
            "countryCode",
            null,
            2,
            null,
            "lifeSpan",
            "Border Terrier",
            null,
            null,
            null
        )
    )
    private val searchByNameOutput = arrayListOf(
        Breed(
            "breedFor",
            "breedGroup",
            "countryCode",
            null,
            1,
            null,
            "lifeSpan",
            "Border Collie",
            null,
            null,
            null
        ),
        Breed(
            "breedFor",
            "breedGroup",
            "countryCode",
            null,
            2,
            null,
            "lifeSpan",
            "Border Terrier",
            null,
            null,
            null
        )
    )
    private val imageItemDTO = arrayListOf(
        ImageItemDTO(
            560,
            "21334",
            "url",
            780,
            arrayListOf(
                BreedDTO(
                    "breedFor",
                    "breedGroup",
                    "countryCode",
                    null,
                    2,
                    null,
                    "lifeSpan",
                    "Border Terrier",
                    null,
                    null,
                    null
                )
            )
        )
    )
    private val imageItemOutput = arrayListOf(
        ImageItem(
            560,
            "21334",
            "url",
            780,
            arrayListOf(
                Breed(
                    "breedFor",
                    "breedGroup",
                    "countryCode",
                    null,
                    2,
                    null,
                    "lifeSpan",
                    "Border Terrier",
                    null,
                    null,
                    null
                )
            )
        )
    )

    private val errorResponse = ServiceResponse.Error("Unauthorised")
    private val invalidPage = "5555"
    private val invalidTag = "Invalid tag"
    private val searchName = "bor"
    private val breedId = "1"

    @Before
    fun setUp() {
        breedsApi = mock()
        val mockException: HttpException = mock()
        whenever(mockException.code()).thenReturn(401)

        runBlocking {
            whenever(
                breedsApi.getBreeds(
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull()
                )
            ).thenReturn(getBreedsDTO)
            whenever(breedsApi.getBreeds(anyOrNull(), eq(invalidPage), anyOrNull())).thenThrow(
                mockException
            )
            whenever(breedsApi.searchByBreed(eq(searchName))).thenReturn(searchByNameDTO)
            whenever(breedsApi.searchByBreed(eq(invalidTag))).thenThrow(mockException)
            whenever(breedsApi.getImageByBreedId(eq(breedId))).thenReturn(imageItemDTO)
            whenever(breedsApi.getImageByBreedId(eq(invalidTag))).thenThrow(mockException)
        }

        breedsRepository = BreedsRepository(breedsApi, responseHandler)
    }


    @Test
    fun `test getBreeds, then ServiceResponseSuccess with a list of Breed objects is returned`() =
        runBlocking {
            assertEquals(
                ServiceResponse.Success(getBreedsOutput), breedsRepository.getBreeds(
                    GetBreedsInput()
                )
            )
        }

    @Test
    fun `test getBreeds when invalid tag is requested, then ServiceResponseError with error is returned`() =
        runBlocking {
            assertEquals(
                errorResponse,
                breedsRepository.getBreeds(GetBreedsInput(page = invalidPage))
            )
        }


    @Test
    fun `test searchByName when valid name is requested, then ServiceResponseSuccess with the list of matching Breed items is returned`() =
        runBlocking {
            assertEquals(
                ServiceResponse.Success(searchByNameOutput), breedsRepository.searchByBreed(
                    SearchByBreedInput(searchName)
                )
            )
        }

    @Test
    fun `test searchByName when invalid name is requested, then ServiceResponseError with error is returned`() =
        runBlocking {
            assertEquals(
                errorResponse,
                breedsRepository.searchByBreed(SearchByBreedInput(invalidTag))
            )
        }

    @Test
    fun `test getImageByBreedId when valid BreedId is requested, then ServiceResponseSuccess with the list of matching list of ImageItem items is returned`() =
        runBlocking {
            assertEquals(
                ServiceResponse.Success(imageItemOutput), breedsRepository.getImageByBreedId(
                    GetImageByBreedId(breedId)
                )
            )
        }

    @Test
    fun `test getImageByBreedId when invalid BreedId is requested, then ServiceResponseError with error is returned`() =
        runBlocking {
            assertEquals(
                errorResponse,
                breedsRepository.getImageByBreedId(GetImageByBreedId(invalidTag))
            )
        }


}