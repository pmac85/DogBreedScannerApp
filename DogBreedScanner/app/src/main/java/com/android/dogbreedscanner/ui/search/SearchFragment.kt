package com.android.dogbreedscanner.ui.search

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.dogbreedscanner.R
import com.android.dogbreedscanner.databinding.FragmentSearchBinding
import com.android.dogbreedscanner.remote.api.BreedsApi
import com.android.dogbreedscanner.remote.helper.ResponseHandler
import com.android.dogbreedscanner.remote.helper.RetrofitBuilderHelper
import com.android.dogbreedscanner.remote.repository.BreedsRepository
import com.android.dogbreedscanner.util.VerticalItemDividerDecorator
import com.android.dogbreedscanner.util.ViewModelOutputUIModel

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

        setupInitialObservers()
        setupUpdateObserver()
        setupResumeUiObserver()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.initData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                val searchView: SearchView = item.actionView as SearchView
                setupSearchView(searchView)
                searchView.setOnQueryTextListener(getSearchQueryTextListener(item, searchView))

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupInitialObservers() {
        viewModel.initialListBreedOutput.observe(viewLifecycleOwner, { response ->
            when (response) {
                is ViewModelOutputUIModel.Content -> {
                    createAdapter(response.contentData)
                }
                is ViewModelOutputUIModel.Empty -> Toast.makeText(
                    requireContext(),
                    response.emptyData,
                    Toast.LENGTH_LONG
                ).show()
                is ViewModelOutputUIModel.Error -> Toast.makeText(
                    requireContext(),
                    response.errorData,
                    Toast.LENGTH_LONG
                ).show()
                ViewModelOutputUIModel.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.searchRv.visibility = View.GONE
                }
            }
        })
    }

    private fun setupUpdateObserver() {
        viewModel.updateListBreedOutput.observe(viewLifecycleOwner, { response ->
            when (response) {
                is ViewModelOutputUIModel.Content -> {
                    updateAdapter(response.contentData)
                }
                is ViewModelOutputUIModel.Empty -> Toast.makeText(
                    requireContext(),
                    response.emptyData,
                    Toast.LENGTH_LONG
                ).show()
                is ViewModelOutputUIModel.Error -> Toast.makeText(
                    requireContext(),
                    response.errorData,
                    Toast.LENGTH_LONG
                ).show()
                ViewModelOutputUIModel.Loading -> {
                    // do nothing
                }
            }
        })
    }

    private fun setupResumeUiObserver() {
        viewModel.resumeUiOutput.observe(viewLifecycleOwner, { response ->
            when (response) {
                is ViewModelOutputUIModel.Content -> {
                    createAdapter(response.contentData.first)
                    binding.searchRv.scrollToPosition(response.contentData.second)
                }
                is ViewModelOutputUIModel.Empty -> {
                    // do nothing
                }
                is ViewModelOutputUIModel.Error -> {
                    // do nothing
                }
                ViewModelOutputUIModel.Loading -> {
                    // do nothing
                }
            }
        })
    }

    private fun createAdapter(items: ArrayList<SearchViewModel.BreedSearchListUiData>) {
        val listAdapter = SearchListItemAdapter(items) { id, position ->
            val action = SearchFragmentDirections.actionNavigationSearchToBreedDetailsFragment(
                viewModel.getDetails(id, position)
            )
            findNavController().navigate(action)
        }

        binding.apply {
            progressBar.visibility = View.VISIBLE

            searchRv.apply {
                visibility = View.GONE
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

                addItemDecoration(
                    VerticalItemDividerDecorator(
                        resources.getDimensionPixelSize(R.dimen.adapter_divider),
                        true
                    )
                )

                adapter = listAdapter

                progressBar.visibility = View.GONE
                visibility = View.VISIBLE
            }
        }
    }

    private fun updateAdapter(items: ArrayList<SearchViewModel.BreedSearchListUiData>) {
        val adapter = binding.searchRv.adapter
        (adapter as SearchListItemAdapter).updateList(items)
    }

    private fun setupSearchView(searchView: SearchView) {
        val searchText: EditText =
            searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        val searchPlate: View = searchView.findViewById(androidx.appcompat.R.id.search_plate)

        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.queryHint = getString(R.string.search_breed)
        searchPlate.setBackgroundResource(R.drawable.shape_list_search_background)

        searchText.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.hintColor))

        searchText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                searchPlate.setBackgroundResource(R.drawable.shape_search_background_focused)
            else
                searchPlate.setBackgroundResource(R.drawable.shape_list_search_background)
        }
    }

    private fun getSearchQueryTextListener(
        item: MenuItem,
        searchView: SearchView
    ): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                item.collapseActionView()
                searchView.setQuery("", false)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchBreedName(newText)
                return true
            }
        }
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