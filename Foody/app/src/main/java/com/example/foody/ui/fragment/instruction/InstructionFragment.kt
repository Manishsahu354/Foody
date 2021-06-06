package com.example.foody.ui.fragment.instruction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.foody.databinding.FragmentInstructionBinding
import com.example.foody.model.Result
import com.example.foody.util.Constants

class InstructionFragment : Fragment() {

    private var _binding:FragmentInstructionBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstructionBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)

        binding.progressBar.visibility = View.VISIBLE
//        binding.instructionWebView.loadUrl(myBundle!!.sourceUrl.toString())
//        val webSettings: WebSettings = binding.instructionWebView.getSettings()
//        webSettings.javaScriptEnabled = true
//        binding.instructionWebView.webViewClient = WebViewClient()


        binding.instructionWebView.webViewClient = object : WebViewClient() {}
        val websiteUrl: String = myBundle!!.sourceUrl.toString()
        binding.instructionWebView.loadUrl(websiteUrl)
        binding.progressBar.visibility = View.INVISIBLE
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}