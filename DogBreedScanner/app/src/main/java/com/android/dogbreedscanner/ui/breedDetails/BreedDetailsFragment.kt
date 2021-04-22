package com.android.dogbreedscanner.ui.breedDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.android.dogbreedscanner.R

class BreedDetailsFragment : Fragment() {

    private lateinit var breedDetailsViewModel: BreedDetailsViewModel

    private val args: BreedDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        breedDetailsViewModel =
            ViewModelProvider(this).get(BreedDetailsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_breed_details, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        breedDetailsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val detailsTextView: TextView = root.findViewById(R.id.text_details)
        detailsTextView.text = args.breedDetails?.toString()



        return root
    }
}