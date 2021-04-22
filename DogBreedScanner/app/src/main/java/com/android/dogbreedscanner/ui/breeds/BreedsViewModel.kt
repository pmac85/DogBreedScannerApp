package com.android.dogbreedscanner.ui.breeds

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.dogbreedscanner.remote.model.domain.Measure
import com.android.dogbreedscanner.ui.breedDetails.BreedDetailsData
import com.android.dogbreedscanner.util.SingleLiveData

class BreedsViewModel : ViewModel() {

    private val _text = SingleLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun getBreedDetails(): BreedDetailsData = BreedDetailsData(
        "Circus performer",
        "Non-Sporting",
        "US",
        Measure("15 - 19", "38 - 48"),
        "https://cdn2.thedogapi.com/images/Bymjyec4m.jpg",
        "12 - 15 years",
        "American Eskimo Dog",
        "Friendly, Alert, Reserved, Intelligent, Protective",
        Measure("20 - 40", "9 - 18")
    )
}