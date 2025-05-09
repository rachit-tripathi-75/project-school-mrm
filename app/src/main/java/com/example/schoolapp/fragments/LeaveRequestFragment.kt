package com.example.schoolapp.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.schoolapp.databinding.FragmentLeaveRequestBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class LeaveRequestFragment : Fragment() {

    private lateinit var binding: FragmentLeaveRequestBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLeaveRequestBinding.inflate(inflater, container, false)

        initialisers()
        listeners()

        return binding.root
    }

    private fun initialisers() {
        setUpLeaveTypeSpinner()
    }

    private fun listeners() {
        binding.tvFromDateSelector.setOnClickListener {
            showDatePickerDialog(true)
        }
        binding.tvToDateSelector.setOnClickListener {
            showDatePickerDialog(false)
        }
    }

    private fun showDatePickerDialog(flag: Boolean) {
        val calendar = Calendar.getInstance(Locale.getDefault())
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedMonth = String.format("%02d", selectedMonth + 1)
                val formattedDay = String.format("%02d", selectedDay)
                val selectedDate = "$formattedDay/$formattedMonth/$selectedYear"
                if (flag) {
                    binding.tvFromDateSelector.text = selectedDate
                    binding.tvFromDateSelector.background = null
                } else {
                    binding.tvToDateSelector.text = selectedDate
                    binding.tvToDateSelector.background = null
                    updateNumberOfDays()
                }

            },
            year,
            month,
            day
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun updateNumberOfDays() {
        binding.tvNoOfDays.text = "No. Of Days: " + daysBetween(
            binding.tvFromDateSelector.text.toString(),
            binding.tvToDateSelector.text.toString()
        ).toString()
    }

    fun daysBetween(startDateString: String, endDateString: String, pattern: String = "dd/MM/yyyy"): Long {
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        try {
            val startDate = simpleDateFormat.parse(startDateString)
            val endDate = simpleDateFormat.parse(endDateString)

            if (startDate != null && endDate != null) {
                val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val startDateOnly = dateFormatter.parse(dateFormatter.format(startDate))
                val endDateOnly = dateFormatter.parse(dateFormatter.format(endDate))

                if (startDateOnly != null && endDateOnly != null) {
                    val timeDifference = endDateOnly.time - startDateOnly.time
                    return TimeUnit.DAYS.convert(timeDifference, TimeUnit.MILLISECONDS) + 1 // counting the present day leave also.......
                }
            }
            return -1
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }
    }
    private fun setUpLeaveTypeSpinner() {

        val leaveTypes = arrayOf("Medical Leave", "Sick Leave", "Half Day", "Emergency Leave")


        val leaveTypeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, leaveTypes)
        leaveTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerLeaveType.adapter = leaveTypeAdapter
        binding.spinnerLeaveType.setSelection(0)

    }

}