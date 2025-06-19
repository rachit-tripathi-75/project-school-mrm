package com.example.schoolapp.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.schoolapp.activities.HomeWorkActivity
import com.example.schoolapp.activities.LeaveRequestActivity
import com.example.schoolapp.classes.ApiClient
import com.example.schoolapp.classes.ApiServices
import com.example.schoolapp.classes.PrefsManager
import com.example.schoolapp.databinding.FragmentLeaveRequestBinding
import com.example.schoolapp.models.LeaveRequestModel
import com.example.schoolapp.responses.HomeworkResponse
import com.example.schoolapp.responses.SubmitLeaveRequestResponse
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.util.Calendar

class LeaveRequestFragment : Fragment() {

    private lateinit var binding: FragmentLeaveRequestBinding
    private val leaveRequestBinding get() = binding

    private val leaveTypes = arrayOf(
        "Medical Leave",
        "Personal Leave",
        "Emergency Leave",
        "Family Leave",
        "Other"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLeaveRequestBinding.inflate(inflater, container, false)


        setupLeaveTypeDropdown()
        setupDatePickers()
        setupSubmitButton()

        return binding.root
    }

    private fun setupLeaveTypeDropdown() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            leaveTypes
        )
        binding.actvLeaveType.setAdapter(adapter)
    }

    private fun setupDatePickers() {
        binding.etStartDate.setOnClickListener {
            showDatePicker { date ->
                binding.etStartDate.setText(date)
            }
        }

        binding.etEndDate.setOnClickListener {
            showDatePicker { date ->
                binding.etEndDate.setText(date)
            }
        }
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedYear-${
                    String.format("%02d", selectedMonth + 1)
                }-${String.format("%02d", selectedDay)}"
                onDateSelected(formattedDate)
            },
            year, month, day
        ).apply {
            datePicker.minDate = System.currentTimeMillis()
            show()
        }
    }

    private fun setupSubmitButton() {
        binding.btnSubmit.setOnClickListener {
            if (validateForm()) {
                submitLeaveRequest()
            }
        }
    }

    private fun validateForm(): Boolean {
        val reason = binding.etReason.text.toString().trim()
        val leaveType = binding.actvLeaveType.text.toString().trim()
        val startDate = binding.etStartDate.text.toString().trim()
        val endDate = binding.etEndDate.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()

        when {
            reason.isEmpty() -> {
                binding.etReason.error = "Please enter reason for leave"
                return false
            }

            leaveType.isEmpty() -> {
                binding.actvLeaveType.error = "Please select leave type"
                return false
            }

            startDate.isEmpty() -> {
                binding.etStartDate.error = "Please select start date"
                return false
            }

            endDate.isEmpty() -> {
                binding.etEndDate.error = "Please select end date"
                return false
            }

            !isValidDateRange(startDate, endDate) -> {
                Toast.makeText(
                    requireContext(),
                    "End date must be after or equal to start date",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }
        return true
    }

    private fun isValidDateRange(startDate: String, endDate: String): Boolean {
        return try {
            val start = startDate.replace("-", "").toInt()
            val end = endDate.replace("-", "").toInt()
            end >= start
        } catch (e: Exception) {
            false
        }
    }

    private fun submitLeaveRequest() {
//        val request = LeaveRequestModel(
//            reason = binding.etReason.text.toString().trim(),
//            leaveType = binding.actvLeaveType.text.toString().trim(),
//            startDate = binding.etStartDate.text.toString().trim(),
//            endDate = binding.etEndDate.text.toString().trim(),
//            description = binding.etDescription.text.toString().trim()
//        )

//        LeaveRequestManager.addLeaveRequest(request)

        // loaded data from a database or API........
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSubmit.visibility = View.GONE

        lifecycleScope.launch {
            try {
                delay(1500)
                ApiClient.submitLeaveRequestInstance.submitLeaveRequest(
                    "application/x-www-form-urlencoded",
                    "ci_session=giedbf3v4u7lahicjd40sf8ridgpgj9i",
                    PrefsManager.getUserDetailedInformation(requireContext()).studentData.get(0).enrollment,
                    binding.actvLeaveType.text.toString().trim(),
                    binding.etStartDate.text.toString().trim(),
                    binding.etEndDate.text.toString().trim(),
                    binding.etReason.text.toString().trim()).enqueue(object: retrofit2.Callback<SubmitLeaveRequestResponse> {

                    override fun onResponse(call: Call<SubmitLeaveRequestResponse?>, response: Response<SubmitLeaveRequestResponse?>) {
                        binding.progressBar.visibility = View.GONE
                        binding.btnSubmit.visibility = View.VISIBLE

                        if (response.isSuccessful) {
                            val s = response.body()
                            val gson = Gson()
                            if (s?.status == 1) {
                                Toast.makeText(requireContext(), "Leave request submitted", Toast.LENGTH_SHORT).show()
                                clearForm()
                                Log.d("submitLeaveRequestTAG", "if-part: ${gson.toJson(s)}")
                            } else {
                                Toast.makeText(requireContext(), "Leave couldn't be submitted. Please try again later.", Toast.LENGTH_SHORT).show()
                                Log.d("submitLeaveRequestTAG", "else-part: ${gson.toJson(s)}")
                            }
                        }
                    }

                    override fun onFailure(call: Call<SubmitLeaveRequestResponse?>, t: Throwable) {
                        Toast.makeText(requireContext(), "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show()
                        Log.d("submitLeaveRequestTAG", "onFailure: ${t.message}")
                    }

                })
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }



    }

    private fun clearForm() {
        binding.etReason.text?.clear()
        binding.actvLeaveType.text?.clear()
        binding.etStartDate.text?.clear()
        binding.etEndDate.text?.clear()
        binding.etDescription.text?.clear()
    }

}