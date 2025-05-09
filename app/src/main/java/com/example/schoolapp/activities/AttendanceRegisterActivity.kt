package com.example.schoolapp.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolapp.R
import com.example.schoolapp.databinding.ActivityAttendanceRegisterBinding
import java.util.Calendar

class AttendanceRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAttendanceRegisterBinding
    private lateinit var attendanceAdapter: AttendanceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAttendanceRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initialisers()
        listeners()
        setupMonthSpinner()
        setupAttendanceRecyclerView()
        
    }

    private fun initialisers() {


    }

    private fun listeners() {
        binding.btnShow.setOnClickListener {
            val selectedMonth = binding.spinnerMonth.selectedItem.toString()
            loadAttendanceData(selectedMonth)
        }

        binding.ivBack.setOnClickListener {
            onBackPressed();
        }
    }


    private fun setupMonthSpinner() {
        // Create array of months
        val months = arrayOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )


        val monthAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            months
        )


        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        binding.spinnerMonth.adapter = monthAdapter


        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        binding.spinnerMonth.setSelection(currentMonth)
    }

    private fun setupAttendanceRecyclerView() {
        binding.attendanceRecyclerView.layoutManager = LinearLayoutManager(this)

        attendanceAdapter = AttendanceAdapter()
        binding.attendanceRecyclerView.adapter = attendanceAdapter
    }

    private fun loadAttendanceData(selectedMonth: String) {
        // loaded data from a database or API........

        val records = mutableListOf<AttendanceRecord>()

        if (selectedMonth == "May") {
            for (i in 1..4) {
                records.add(AttendanceRecord("01 May 2023", "Present"))
            }
        } else {
            // Add some dummy data for other months
            records.add(AttendanceRecord("01 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("02 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("03 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("04 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("05 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("06 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("07 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("08 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("09 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("10 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("11 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("12 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("13 $selectedMonth 2023", "Absent"))
            records.add(AttendanceRecord("14 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("15 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("16 $selectedMonth 2023", "Absent"))
            records.add(AttendanceRecord("17 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("18 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("19 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("20 $selectedMonth 2023", "Absent"))
            records.add(AttendanceRecord("21 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("22 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("23 $selectedMonth 2023", "Absent"))
            records.add(AttendanceRecord("24 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("25 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("26 $selectedMonth 2023", "Absent"))
            records.add(AttendanceRecord("27 $selectedMonth 2023", "Absent"))
            records.add(AttendanceRecord("28 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("29 $selectedMonth 2023", "Present"))
            records.add(AttendanceRecord("30 $selectedMonth 2023", "Present"))
        }

        attendanceAdapter.setAttendanceRecords(records)
    }

    data class AttendanceRecord(val date: String, val status: String)

    private inner class AttendanceAdapter : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {
        private var attendanceRecords = listOf<AttendanceRecord>()

        fun setAttendanceRecords(records: List<AttendanceRecord>) {
            this.attendanceRecords = records
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_attendance, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val record = attendanceRecords[position]
            holder.tvDate.text = record.date
            holder.tvStatus.text = record.status

            if (record.status == "Present") {
                holder.tvStatus.setBackgroundResource(R.drawable.status_present_background)
            } else {
                holder.tvStatus.setBackgroundResource(R.drawable.status_absent_background)
            }
        }

        override fun getItemCount() = attendanceRecords.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvDate: TextView = itemView.findViewById(R.id.tvDate)
            val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        }
    }
}