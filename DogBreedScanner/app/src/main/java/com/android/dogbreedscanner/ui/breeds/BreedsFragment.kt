package com.android.dogbreedscanner.ui.breeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.android.dogbreedscanner.R

class BreedsFragment : Fragment() {

    private lateinit var breedsViewModel: BreedsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        breedsViewModel =
            ViewModelProvider(this).get(BreedsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_breeds, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        breedsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val button: Button = root.findViewById(R.id.breedButton)
        button.setOnClickListener {
            val action = BreedsFragmentDirections.actionNavigationBreedsToBreedDetailsFragment(
                breedsViewModel.getBreedDetails()
            )
            it.findNavController().navigate(action)
        }

        return root
    }
}