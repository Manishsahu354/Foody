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
import com.example.foody.model.Result
import kotlinx.android.synthetic.main.fragment_overview.view.*
import org.jsoup.Jsoup


class OverviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_overview, container, false)

        val args = arguments
        val myBundle:Result? = args?.getParcelable("recipeBundle")

        view.mainImageView.load(myBundle?.image)
        view.titleTextView.text = myBundle?.title
        view.likesTextView.text = myBundle?.aggregateLikes.toString()
        view.timeTextView.text = myBundle?.readyInMinutes.toString()
        myBundle?.summary.let {
            val summary = Jsoup.parse(it).text()
            view.summary_textView.text = summary
        }



        myBundle?.let {bundle->

            updateColors(bundle.vegetarian, view.vegetarianTextView, view.veganImageView)
            updateColors(bundle.vegan, view.veganTextView, view.veganImageView)
            updateColors(bundle.cheap, view.cheap_textView, view.cheap_imageView)
            updateColors(bundle.dairyFree, view.dairy_free_textView, view.dairy_free_imageView)
            updateColors(bundle.glutenFree, view.glutenFreeTextView, view.glutenFreeImage)
            updateColors(bundle.veryHealthy, view.healthy_textView, view.healthy_imageView)

        }



        return view
    }

    private fun updateColors(stateIsOn: Boolean, textView: TextView, imageView: ImageView) {
        if (stateIsOn) {
            imageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }


}