package com.android.dogbreedscanner

import com.android.dogbreedscanner.remote.helper.ResponseHandler
import com.android.dogbreedscanner.remote.helper.ServiceResponse
import com.android.dogbreedscanner.remote.model.dto.BreedDTO
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-25.
 * </prev>
 */

@RunWith(JUnit4::class)
class ResponseHandlerTest {
    lateinit var responseHandler: ResponseHandler

    @Before
    fun setUp() {
        responseHandler = ResponseHandler()
    }

    @Test
    fun `when exception code is 401 then return unauthorised`() {
        val httpException = HttpException(Response.error<BreedDTO>(401, mock()))
        val result = responseHandler.handleException(httpException)
        Assert.assertEquals("Unauthorised", (result as ServiceResponse.Error).errorData)
    }

    @Test
    fun `when timeout then return timeout error`() {
        val socketTimeoutException = SocketTimeoutException()
        val result = responseHandler.handleException(socketTimeoutException)
        Assert.assertEquals("Timeout", (result as ServiceResponse.Error).errorData)
    }

    @Test
    fun `when success then return ServiceResponse Success`() {
        val listBreeds = arrayListOf(
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
        val result = responseHandler.handleSuccess(listBreeds)
        Assert.assertTrue(result is ServiceResponse.Success)
        Assert.assertEquals(listBreeds, (result as ServiceResponse.Success).successData)
    }
}