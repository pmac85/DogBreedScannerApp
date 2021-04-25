package com.android.dogbreedscanner.ui.breeds

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.dogbreedscanner.R
import com.android.dogbreedscanner.databinding.FragmentBreedsBinding
import com.android.dogbreedscanner.remote.api.BreedsApi
import com.android.dogbreedscanner.remote.helper.ResponseHandler
import com.android.dogbreedscanner.remote.helper.RetrofitBuilderHelper
import com.android.dogbreedscanner.remote.repository.BreedsRepository
import com.android.dogbreedscanner.util.HorizontalItemDividerDecorator
import com.android.dogbreedscanner.util.PaginationScrollListener
import com.android.dogbreedscanner.util.VerticalItemDividerDecorator
import com.android.dogbreedscanner.util.ViewModelOutputUIModel


class BreedsFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            getViewModelFactory()
        ).get(BreedsViewModel::class.java)
    }
    private var _binding: FragmentBreedsBinding? = null
    private val binding get() = _binding!!

    private var currentViewType = ListType.ListView
    private var gridAdapter: BreedGridViewItemAdapter? = null
    private var listAdapter: BreedListViewItemAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBreedsBinding.inflate(inflater, container, false)

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
        inflater.inflate(R.menu.breeds_list_menu, menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_reorder -> {
                gridAdapter?.orderListAlphabetically()
                listAdapter?.orderListAlphabetically()

                true
            }
            R.id.action_change_view -> {
                swipeAdapters(item)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        gridAdapter = null
        listAdapter = null
    }

    private fun setupInitialObservers() {
        viewModel.initialListBreedOutput.observe(viewLifecycleOwner, { response ->
            when (response) {
                is ViewModelOutputUIModel.Content -> {
                    setupInitialUi(response.contentData)
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
                    binding.recyclerView.visibility = View.GONE
                }
            }
        })
    }

    private fun setupUpdateObserver() {
        viewModel.updateListBreedOutput.observe(viewLifecycleOwner, { response ->
            when (response) {
                is ViewModelOutputUIModel.Content -> {
                    gridAdapter?.addMoreItems(response.contentData)
                    listAdapter?.addMoreItems(response.contentData)
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
                is ViewModelOutputUIModel.Content -> resumeUI(
                    response.contentData.first,
                    response.contentData.second
                )
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

    private fun setupInitialUi(items: HashMap<String, BreedsViewModel.BreedListUiData>) {
        createListViewAdapter(items)
    }

    private fun resumeUI(items: HashMap<String, BreedsViewModel.BreedListUiData>, position: Int) {
        when (currentViewType) {
            ListType.GridView -> createGridViewAdapter(items)
            ListType.ListView -> createListViewAdapter(items)
        }

        binding.recyclerView.scrollToPosition(position)
    }

    private fun createGridViewAdapter(items: HashMap<String, BreedsViewModel.BreedListUiData>) {
        gridAdapter = BreedGridViewItemAdapter(items) { id, position ->
            val action = BreedsFragmentDirections.actionNavigationBreedsToBreedDetailsFragment(
                viewModel.getDetails(id, position)
            )
            findNavController().navigate(action)
        }

        binding.apply {
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE

            recyclerView.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)

                val paginationScrollListener = object :
                    PaginationScrollListener(layoutManager as GridLayoutManager) {

                    override fun isLastPage(): Boolean {
                        return viewModel.isLastPage()
                    }

                    override fun loadMoreItems() {
                        viewModel.getMoreBreeds()
                    }
                }

                addItemDecoration(
                    VerticalItemDividerDecorator(
                        resources.getDimensionPixelSize(R.dimen.adapter_divider),
                        true
                    )
                )
                addItemDecoration(HorizontalItemDividerDecorator(resources.getDimensionPixelSize(R.dimen.adapter_divider)))

                adapter = gridAdapter!!

                addOnScrollListener(paginationScrollListener)

                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }

            listAdapter = null
        }
        gridAdapter!!.addMoreItems(items)
    }

    private fun createListViewAdapter(items: HashMap<String, BreedsViewModel.BreedListUiData>) {
        listAdapter = BreedListViewItemAdapter(hashMapOf()) { id, position ->
            val action = BreedsFragmentDirections.actionNavigationBreedsToBreedDetailsFragment(
                viewModel.getDetails(id, position)
            )
            findNavController().navigate(action)
        }

        binding.apply {
            progressBar.visibility = View.VISIBLE

            recyclerView.apply {
                visibility = View.GONE
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

                val paginationScrollListener = object :
                    PaginationScrollListener(layoutManager as LinearLayoutManager) {

                    override fun isLastPage(): Boolean {
                        return viewModel.isLastPage()
                    }

                    override fun loadMoreItems() {
                        viewModel.getMoreBreeds()
                    }
                }

                addItemDecoration(
                    VerticalItemDividerDecorator(
                        resources.getDimensionPixelSize(R.dimen.adapter_divider),
                        true
                    )
                )

                adapter = listAdapter!!

                addOnScrollListener(paginationScrollListener)

                progressBar.visibility = View.GONE
                visibility = View.VISIBLE
            }

            gridAdapter = null
        }
        listAdapter!!.addMoreItems(items)
    }

    private fun swipeAdapters(item: MenuItem) {
        when (currentViewType) {
            ListType.GridView -> {
                val items = gridAdapter!!.getMapItems()

                createListViewAdapter(items)

                item.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_list_view)

                currentViewType = ListType.ListView
            }
            ListType.ListView -> {
                val items = listAdapter!!.getMapItems()

                createGridViewAdapter(items)

                item.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_grid_view)

                currentViewType = ListType.GridView

            }
        }
    }

    private fun getViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(BreedsViewModel::class.java)) {
                    return BreedsViewModel(
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

    private enum class ListType {
        GridView,
        ListView
    }
}