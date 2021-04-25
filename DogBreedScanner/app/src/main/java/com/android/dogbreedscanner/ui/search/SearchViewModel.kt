package com.android.dogbreedscanner.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.dogbreedscanner.remote.helper.ServiceResponse
import com.android.dogbreedscanner.remote.model.domain.Breed
import com.android.dogbreedscanner.remote.model.input.GetBreedsInput
import com.android.dogbreedscanner.remote.model.input.SearchByBreedInput
import com.android.dogbreedscanner.remote.repository.BreedsRepository
import com.android.dogbreedscanner.ui.breedDetails.BreedDetailsData
import com.android.dogbreedscanner.util.SingleLiveData
import com.android.dogbreedscanner.util.ViewModelOutputUIModel
import kotlinx.coroutines.launch

class SearchViewModel(private val breedsRepository: BreedsRepository) : ViewModel() {

    private val _initialListBreedOutput =
        SingleLiveData<ViewModelOutputUIModel<ArrayList<BreedSearchListUiData>, String, String>>()
    val initialListBreedOutput: LiveData<ViewModelOutputUIModel<ArrayList<BreedSearchListUiData>, String, String>> =
        _initialListBreedOutput
    private val _updateListBreedOutput =
        SingleLiveData<ViewModelOutputUIModel<ArrayList<BreedSearchListUiData>, String, String>>()
    val updateListBreedOutput: LiveData<ViewModelOutputUIModel<ArrayList<BreedSearchListUiData>, String, String>> =
        _updateListBreedOutput
    private val _resumeUiOutput =
        SingleLiveData<ViewModelOutputUIModel<Pair<ArrayList<BreedSearchListUiData>, Int>, Nothing, Nothing>>()
    val resumeUiOutput: LiveData<ViewModelOutputUIModel<Pair<ArrayList<BreedSearchListUiData>, Int>, Nothing, Nothing>> =
        _resumeUiOutput

    private var mapOfCurrentBreedsById = hashMapOf<Int, Breed>()
    private var currentAdapterPosition = 1

    fun initData() {
        if (mapOfCurrentBreedsById.isEmpty()) {
            getInitialList()
        } else {
            _resumeUiOutput.postValue(
                ViewModelOutputUIModel.Content(
                    Pair(
                        mapOfCurrentBreedsById.values.toList().mapToOutput(),
                        currentAdapterPosition
                    )
                )
            )
        }
    }

    fun searchBreedName(name: String?) {
        name.takeIf { !it.isNullOrBlank() }?.let {
            viewModelScope.launch {
                when (val response = breedsRepository.searchByBreed(SearchByBreedInput(it))) {
                    is ServiceResponse.Error -> _updateListBreedOutput.postValue(
                        ViewModelOutputUIModel.Error(
                            response.errorData
                        )
                    )
                    is ServiceResponse.Success -> parseSuccessResult(response.successData, false)
                }
            }
        }
    }

    fun getDetails(breedId: Int, adapterPosition: Int): BreedDetailsData? {
        currentAdapterPosition = adapterPosition

        return mapOfCurrentBreedsById[breedId]?.let {
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

    private fun getInitialList() {
        viewModelScope.launch {

            when (val response = breedsRepository.getBreeds(
                GetBreedsInput(
                    limit = BREEDS_PER_PAGE.toString()
                )
            )) {
                is ServiceResponse.Error -> _initialListBreedOutput.postValue(
                    ViewModelOutputUIModel.Error(
                        response.errorData
                    )
                )
                is ServiceResponse.Success -> parseSuccessResult(response.successData, true)
            }
        }
    }

    private fun parseSuccessResult(listBreeds: List<Breed>, isStart: Boolean) {
        mapOfCurrentBreedsById = hashMapOf()

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

    private fun List<Breed>.mapToOutput(): ArrayList<BreedSearchListUiData> {
        val resultList = arrayListOf<BreedSearchListUiData>()
        this.forEach {
            it.name?.let { name ->
                mapOfCurrentBreedsById[it.breedId] = it

                resultList.add(
                    BreedSearchListUiData(
                        name,
                        it.breedGroup,
                        it.countryCode,
                        it.breedId
                    )
                )
            }
        }
        return resultList
    }

    data class BreedSearchListUiData(
        val name: String,
        val breedGroup: String?,
        val countryCode: String?,
        val id: Int
    )

    companion object {
        private const val BREEDS_PER_PAGE = 20
    }
}