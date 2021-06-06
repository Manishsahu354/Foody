package com.example.foody.ui.fragment.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import com.example.foody.R
import com.example.foody.databinding.FragmentOverviewBinding
import com.example.foody.model.Result
import org.jsoup.Jsoup


class OverviewFragment : Fragment() {

    private var _binding:FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =  FragmentOverviewBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle:Result? = args?.getParcelable("recipeBundle")

        binding.mainImageView.load(myBundle?.image)
        binding.titleTextView.text = myBundle?.title
        binding.likesTextView.text = myBundle?.aggregateLikes.toString()
        binding.timeTextView.text = myBundle?.readyInMinutes.toString()
        myBundle?.summary.let {
            val summary = Jsoup.parse(it).text()
            binding.summaryTextView.text = summary
        }



        myBundle?.let {bundle->

            updateColors(bundle.vegetarian, binding.vegetarianTextView, binding.veganImageView)
            updateColors(bundle.vegan, binding.veganTextView, binding.veganImageView)
            updateColors(bundle.cheap, binding.cheapTextView, binding.cheapImageView)
            updateColors(bundle.dairyFree, binding.dairyFreeTextView, binding.dairyFreeImageView)
            updateColors(bundle.glutenFree, binding.glutenFreeTextView, binding.glutenFreeImage)
            updateColors(bundle.veryHealthy, binding.healthyTextView, binding.healthyImageView)

        }



        return binding.root
    }

    private fun updateColors(stateIsOn: Boolean, textView: TextView, imageView: ImageView) {
        if (stateIsOn) {
            imageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}