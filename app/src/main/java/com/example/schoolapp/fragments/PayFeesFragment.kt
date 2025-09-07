package com.example.schoolapp.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.schoolapp.classes.PrefsManager
import com.example.schoolapp.databinding.FragmentPayFeesBinding
import com.example.schoolapp.models.FeeItem
import com.example.schoolapp.viewmodels.FeeInstallmentDetailsViewModel
import com.example.schoolapp.viewmodels.FeeInstallmentViewModel


class PayFeesFragment : Fragment() {

    private lateinit var binding: FragmentPayFeesBinding
    // Base URL for the payment gateway
    private val BASE_PAYMENT_URL = "https://erp.apschitrakoot.in/Studentweb/FeeSubmitNew/"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPayFeesBinding.inflate(inflater, container, false)


        initWebView()
        listeners()

        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.webView.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
//                binding.webView.visibility = View.GONE
            }

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                Toast.makeText(activity, "Error: $description", Toast.LENGTH_SHORT).show()
            }
        }

        // for the JS alert dialog....... after clicking on "Pay Now"
        binding.webView.webChromeClient = object : android.webkit.WebChromeClient() {
            override fun onJsAlert(
                view: WebView?,
                url: String?,
                message: String?,
                result: android.webkit.JsResult?
            ): Boolean {
                androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Confirmation")
                    .setMessage(message)
                    .setPositiveButton("OK") { dialog, _ ->
                        result?.confirm()
                        dialog.dismiss()
                    }
                    .setCancelable(false)
                    .create()
                    .show()
                return true
            }
        }

    }

    private fun listeners() {
        val finalUrl = "$BASE_PAYMENT_URL${
            PrefsManager.getUserDetailedInformation(requireContext())?.studentData!!.get(0).studentId
        }"
        Log.d("PaymentURL", "Loading URL: $finalUrl")
        binding.webView.loadUrl(finalUrl)
    }


}