package com.android.dogbreedscanner.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.dogbreedscanner.remote.repository.BreedsRepository

class SearchViewModel(private val breedsRepository: BreedsRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}