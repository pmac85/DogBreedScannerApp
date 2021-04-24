package com.android.dogbreedscanner.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.dogbreedscanner.databinding.FragmentSearchBinding
import com.android.dogbreedscanner.remote.api.BreedsApi
import com.android.dogbreedscanner.remote.helper.ResponseHandler
import com.android.dogbreedscanner.remote.helper.RetrofitBuilderHelper
import com.android.dogbreedscanner.remote.repository.BreedsRepository

class SearchFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this, getViewModelFactory()).get(SearchViewModel::class.java)
    }
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        viewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textDashboard.text = it
        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                    return SearchViewModel(
                        BreedsRepository(
                            RetrofitBuilderHelper.getRetrofitInstance()
                                .create(BreedsApi::class.java),
                            ResponseHandler()
                        )
                    ) as T
                }
                throw IllegalArgumentException("Unknown class name")
            }
        }
    }
}