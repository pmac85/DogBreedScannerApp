package com.android.dogbreedscanner.ui.breedDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.dogbreedscanner.util.SingleLiveData

class BreedDetailsViewModel : ViewModel() {

    private val _text = SingleLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
}