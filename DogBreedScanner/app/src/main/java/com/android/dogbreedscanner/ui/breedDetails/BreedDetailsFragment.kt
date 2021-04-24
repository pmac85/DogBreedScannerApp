package com.android.dogbreedscanner.ui.breedDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.android.dogbreedscanner.R
import com.android.dogbreedscanner.databinding.FragmentBreedDetailsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory

class BreedDetailsFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(BreedDetailsViewModel::class.java)
    }
    private val args: BreedDetailsFragmentArgs by navArgs()

    private var _binding: FragmentBreedDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBreedDetailsBinding.inflate(inflater, container, false)

        val detailData = args.breedDetails

        detailData?.let {
            binding.apply {
                detail = it

                val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

                Glide.with(itemImage.context)
                    .load(it.url)
                    .transition(DrawableTransitionOptions.withCrossFade(factory))
                    .apply(RequestOptions().centerCrop().placeholder(R.drawable.image_large))
                    .into(itemImage)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}