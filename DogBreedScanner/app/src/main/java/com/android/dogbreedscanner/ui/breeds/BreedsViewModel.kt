package com.android.dogbreedscanner.ui.breeds

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.dogbreedscanner.remote.helper.ServiceResponse
import com.android.dogbreedscanner.remote.model.domain.Breed
import com.android.dogbreedscanner.remote.model.input.GetBreedsInput
import com.android.dogbreedscanner.remote.repository.BreedsRepository
import com.android.dogbreedscanner.ui.breedDetails.BreedDetailsData
import com.android.dogbreedscanner.util.SingleLiveData
import com.android.dogbreedscanner.util.ViewModelOutputUIModel
import kotlinx.coroutines.launch

class BreedsViewModel(private val breedsRepository: BreedsRepository) : ViewModel() {

    private val _initialListBreedOutput =
        SingleLiveData<ViewModelOutputUIModel<HashMap<String, BreedListUiData>, String, String>>()
    val initialListBreedListOutput: LiveData<ViewModelOutputUIModel<HashMap<String, BreedListUiData>, String, String>> =
        _initialListBreedOutput
    private val _updateListBreedOutput =
        SingleLiveData<ViewModelOutputUIModel<HashMap<String, BreedListUiData>, String, String>>()
    val updateListBreedListOutput: LiveData<ViewModelOutputUIModel<HashMap<String, BreedListUiData>, String, String>> =
        _updateListBreedOutput
    private val _resumeUiOutput =
        SingleLiveData<ViewModelOutputUIModel<Pair<HashMap<String, BreedListUiData>, Int>, Nothing, Nothing>>()
    val resumeUiOutput: LiveData<ViewModelOutputUIModel<Pair<HashMap<String, BreedListUiData>, Int>, Nothing, Nothing>> =
        _resumeUiOutput

    private var actualPage: Int = 0
    private var isLastPage = false

    private val mapOfAllBreedsById = hashMapOf<Int, Breed>()
    private val fullUiDataMap = hashMapOf<String, BreedListUiData>()
    private var currentAdapterPosition = 1

    fun initData() {
        if (fullUiDataMap.isEmpty()) {
            getBreedList(true)
        } else {
            _resumeUiOutput.postValue(
                ViewModelOutputUIModel.Content(
                    Pair(fullUiDataMap, currentAdapterPosition)
                )
            )
        }
    }

    fun isLastPage(): Boolean = isLastPage

    fun getMoreBreeds() {
        if (!isLastPage) {
            actualPage++
            getBreedList(false)
        }
    }

    fun getDetails(breedId: Int, adapterPosition: Int): BreedDetailsData? {
        currentAdapterPosition = adapterPosition

        return mapOfAllBreedsById[breedId]?.let {
            BreedDetailsData(
                it.name!!,
                it.breedGroup,
                it.countryCode,
                it.temperament,
                it.image?.url,
                it.lifeSpan
            )
        }
    }

    fun getCurrentFullAdapterMap() = fullUiDataMap

    private fun getBreedList(isStart: Boolean) {
        viewModelScope.launch {

            when (val response = breedsRepository.getBreeds(
                GetBreedsInput(
                    page = actualPage.toString(),
                    limit = BREEDS_PER_PAGE.toString()
                )
            )) {
                is ServiceResponse.Error -> _initialListBreedOutput.postValue(
                    ViewModelOutputUIModel.Error(
                        response.errorData
                    )
                )
                is ServiceResponse.Success -> getBreedsWithSuccess(response.successData, isStart)
            }
        }
    }

    private fun getBreedsWithSuccess(listBreeds: List<Breed>, isStart: Boolean) {
        isLastPage = listBreeds.size != BREEDS_PER_PAGE

        if (isStart) {
            _initialListBreedOutput.postValue(
                ViewModelOutputUIModel.Content(
                    listBreeds.mapToOutput()
                )
            )
        } else {
            _updateListBreedOutput.postValue(
                ViewModelOutputUIModel.Content(
                    listBreeds.mapToOutput()
                )
            )
        }
    }

    private fun List<Breed>.mapToOutput(): HashMap<String, BreedListUiData> {
        val resultMap = hashMapOf<String, BreedListUiData>()
        this.forEach {
            it.name?.let { name ->
                mapOfAllBreedsById[it.breedId] = it
                val item = BreedListUiData(name, it.image?.url, it.breedId)
                fullUiDataMap[name] = item

                resultMap[name] = item
            }
        }
        return resultMap
    }

    data class BreedListUiData(
        val name: String,
        val url: String?,
        val id: Int
    )

    enum class ListOrderType {
        None,
        Ascending,
        Descending
    }

    companion object {
        const val BREEDS_PER_PAGE = 20
    }
}