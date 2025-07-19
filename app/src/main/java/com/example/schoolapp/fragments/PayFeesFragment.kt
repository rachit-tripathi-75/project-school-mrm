package com.example.schoolapp.fragments

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.schoolapp.R
import com.example.schoolapp.classes.ApiClient
import com.example.schoolapp.classes.PrefsManager
import com.example.schoolapp.databinding.FragmentPayFeesBinding
import com.example.schoolapp.models.FeeItem
import com.example.schoolapp.responses.FeeInstallmentDetailResponse
import com.example.schoolapp.viewmodels.FeeInstallmentDetailsViewModel
import com.example.schoolapp.viewmodels.FeeInstallmentViewModel
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import kotlin.collections.mutableListOf


class PayFeesFragment : Fragment() {

    private lateinit var binding: FragmentPayFeesBinding
    private val feeInstallmentViewModel: FeeInstallmentViewModel by activityViewModels()
    private val feeInstallmentDetailsViewModel: FeeInstallmentDetailsViewModel by activityViewModels()
    private lateinit var radioGroupMap: Map<String, String>
    private var selectedId: String = "-1"
    private var feeItemList = mutableListOf<FeeItem>()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPayFeesBinding.inflate(inflater, container, false)


        initialisers() // setting up the webview..... below logic is needed no more !!!
//        setFeeInstallmentInformation()
//        listeners()

        return binding.root
    }

    private fun initialisers() {

        val stuId = PrefsManager.getUserDetailedInformation(requireContext()).studentData[0].studentId
        val url = "https://erp.apschitrakoot.in/Studentweb/FeeSubmitNew/$stuId"

        binding.webViewPayFee.settings.javaScriptEnabled = true

        // Ensure all navigation happens inside the WebView
        binding.webViewPayFee.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { view?.loadUrl(it) }
                return true
            }
        }

        // Handle JS alerts and confirms
        binding.webViewPayFee.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                AlertDialog.Builder(requireContext())
                    .setTitle("Alert")
                    .setMessage(message)
                    .setPositiveButton("OK") { _, _ -> result?.confirm() }
                    .setCancelable(false)
                    .create()
                    .show()
                return true
            }

            override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                AlertDialog.Builder(requireContext())
                    .setTitle("Confirm")
                    .setMessage(message)
                    .setPositiveButton("OK") { _, _ -> result?.confirm() }
                    .setNegativeButton("Cancel") { _, _ -> result?.cancel() }
                    .create()
                    .show()
                return true
            }
        }

        binding.webViewPayFee.loadUrl(url)

        // Back button handling
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webViewPayFee.canGoBack()) {
                    binding.webViewPayFee.goBack()
                } else {
                    requireActivity().finish()
                }
            }
        })
    }



    private fun setFeeInstallmentInformation() {
        feeInstallmentViewModel.feeInstallmentData.observe(viewLifecycleOwner) { feeData ->
//            binding.radioAprilJune.text = feeData.data[0].monthname
            binding.radioJulySeptember.text = feeData.data[1].monthname
            binding.radioOctoberDecember.text = feeData.data[2].monthname
            binding.radioJanuaryMarch.text = feeData.data[3].monthname
            radioGroupMap = mapOf(
                feeData.data[0].monthname to feeData.data[0].feeInstallment,
                feeData.data[1].monthname to feeData.data[1].feeInstallment,
                feeData.data[2].monthname to feeData.data[2].feeInstallment,
                feeData.data[3].monthname to feeData.data[3].feeInstallment
            )
        }
    }

    private fun listeners() {


        binding.installmentRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioAprilJune -> {
                    selectedId = radioGroupMap[binding.radioAprilJune.text].toString()
                }

                R.id.radioJulySeptember -> {
                    selectedId = radioGroupMap[binding.radioJulySeptember.text].toString()
                }

                R.id.radioOctoberDecember -> {
                    selectedId = radioGroupMap[binding.radioOctoberDecember.text].toString()
                }

                R.id.radioJanuaryMarch -> {
                    selectedId = radioGroupMap[binding.radioJanuaryMarch.text].toString()
                }

                else -> {
                    selectedId = "-1"
                }
            }
        }

        binding.llGoBack.setOnClickListener {
            binding.installmentCard.visibility = View.VISIBLE
            binding.btnSubmit.visibility = View.VISIBLE
            binding.llFeeDetailsCard.visibility = View.GONE
        }


        binding.btnSubmit.setOnClickListener {
            if (binding.installmentRadioGroup.checkedRadioButtonId == -1) {
                Toast.makeText(requireContext(), "Select an installment month", Toast.LENGTH_SHORT)
                    .show()

            } else {
                fetchFeeInstallmentDetails()
            }

        }
    }

    private fun fetchFeeInstallmentDetails() {
        // add the loading screens, view etc........
        binding.btnSubmit.visibility = View.GONE
        binding.progressBarBehindSubmitButton.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                delay(1500)
                ApiClient.feeInstallmentDetailInstance.getFeeInstallmentDetails(
                    "Bearer 8a56598bd5114ab31f6f70e76e1873e8945eafcd915b3f6ada4c0132d212a57e",
                    "ci_session=a1b0q4t86ag26bo2u9kq9o7hl4b4mf8r",
                    PrefsManager.getSectionId(requireContext()), // sec_id (sectionID of student)
                    getInstallmentId(), // inst (fee installment ID)
                    "1", // fee_id (feeID, this should be dynamic, not constant)
                    "2025" // sessionid (year)
                ).enqueue(object : retrofit2.Callback<FeeInstallmentDetailResponse> {

                    override fun onResponse(call: Call<FeeInstallmentDetailResponse?>, response: Response<FeeInstallmentDetailResponse?>) {
                        binding.installmentCard.visibility = View.GONE
                        binding.btnSubmit.visibility = View.GONE
                        binding.progressBarBehindSubmitButton.visibility = View.GONE
                        binding.llFeeDetailsCard.visibility = View.VISIBLE
//                        binding.viewPager.visibility = View.VISIBLE
//                        binding.llProgressBar.visibility = View.GONE
//                        binding.tabLayout.visibility = View.VISIBLE
                        if (response.isSuccessful && response.body() != null) {
                            val s = response.body()
                            val gson = Gson()
                            if (s?.status == 1) {
                                Log.d("feeInstallmentDetailTAG", "${gson.toJson(s)}}")
                                feeInstallmentDetailsViewModel.setFeeData(s)
                                setFeeDetailInformation()
                            }
                        }
                    }

                    override fun onFailure(call: Call<FeeInstallmentDetailResponse?>, t: Throwable) {
                        binding.installmentCard.visibility = View.VISIBLE
                        binding.llFeeDetailsCard.visibility = View.GONE
                        binding.cvFeeStructureContent.visibility = View.GONE
                        Toast.makeText(requireContext(), "Some error occurred", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("feeInstallmentDetailTAG", "${t.message}")
                    }

                })
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setFeeDetailInformation() {
        feeInstallmentDetailsViewModel.feeData.observe(viewLifecycleOwner) { feeData ->
            if (feeData.data.isEmpty()) {
                binding.cvStudentInformationContent.visibility = View.GONE
                binding.cvFeeStructureContent.visibility = View.GONE
                binding.cvPaymentContent.visibility = View.GONE
                binding.cvNothingToShow.visibility = View.VISIBLE
                return@observe
            }
            feeItemList.clear()
            for (i in 0 until feeData.data.size) {
                val s = FeeItem(
                    feeData.data[i].feeType,
                    feeData.data[i].amount,
                    feeData.data[i].monthName
                )
                feeItemList.add(s)
            }


            makeFeeItemViews()

//            binding.tvFeeType1.text = "Fee Type = ${feeData.data[0].feeType}"
//            binding.tvFeeType1Amount.text = "Rs. ${feeData.data[0].amount} /-"
//
//            binding.tvFeeType2.text = "Fee Type = ${feeData.data[1].feeType}"
//            binding.tvFeeType2Amount.text = "Rs. ${feeData.data[1].amount} /-"
//
//            binding.tvFeeType3.text = "Fee Type = ${feeData.data[2].feeType}"
//            binding.tvFeeType3Amount.text = "Rs. ${feeData.data[2].amount} /-"
//
//            binding.tvFeeType4.text = "Fee Type = ${feeData.data[3].feeType}"
//            binding.tvFeeType4Amount.text = "Rs. ${feeData.data[3].amount} /-"
//
//            binding.tvFeeType5.text = "Fee Type = ${feeData.data[4].feeType}"
//            binding.tvFeeType5Amount.text = "Rs. ${feeData.data[4].amount} /-"
//
//            binding.tvFeeType6.text = "Fee Type = ${feeData.data[5].feeType}"
//            binding.tvFeeType6Amount.text = "Rs. ${feeData.data[5].amount} /-"
//
//            binding.tvFeeType7.text = "Fee Type = ${feeData.data[6].feeType}"
//            binding.tvFeeType7Amount.text = "Rs. ${feeData.data[6].amount} /-"

        }
    }

    private fun makeFeeItemViews() {

        // Clear existing views (if needed)
        binding.feeItemsContainer.removeAllViews()

        // Loop through the list and add items
        feeItemList.forEachIndexed { index, feeItem ->
            // Create a new LinearLayout (similar to ll1)
            val feeItemLayout = LinearLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    if (index > 0) {
                        setMargins(0, 8.dpToPx(), 0, 0) // Add margin between items
                    }
                }
                orientation = LinearLayout.HORIZONTAL
                setBackgroundResource(R.drawable.fee_item_background)
                setPadding(12.dpToPx(), 12.dpToPx(), 12.dpToPx(), 12.dpToPx())
            }

            // TextView for Fee Type
            val tvFeeType = TextView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f // weight = 1
                )
                text = if (feeItem.monthName.isNotEmpty()) {
                    "${feeItem.feeType} (${feeItem.monthName})"
                } else {
                    feeItem.feeType
                }
            }

            // TextView for Amount
            val tvAmount = TextView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                text = "Rs. ${feeItem.amount} /-"
                setTypeface(null, Typeface.BOLD)
            }

            // Add TextViews to the LinearLayout
            feeItemLayout.addView(tvFeeType)
            feeItemLayout.addView(tvAmount)

            // Add the LinearLayout to the container
            binding.feeItemsContainer.addView(feeItemLayout)
        }
    }

    fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    private fun getInstallmentId(): String {
        return selectedId
    }


}