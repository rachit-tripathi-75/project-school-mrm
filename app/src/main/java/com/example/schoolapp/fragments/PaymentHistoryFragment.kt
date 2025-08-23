package com.example.schoolapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolapp.R
import com.example.schoolapp.databinding.FragmentPaymentHistoryBinding
import com.example.schoolapp.responses.PaymentHistoryResponse
import com.example.schoolapp.viewmodels.PaymentHistoryViewModel
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class PaymentHistoryFragment : Fragment() {

    private lateinit var binding: FragmentPaymentHistoryBinding
    private val paymentHistoryViewModel: PaymentHistoryViewModel by activityViewModels()
    private lateinit var paymentHistoryAdapter: PaymentHistoryAdapter
    private val feeTypeMap = mapOf("1" to "Academic Fee", "3" to "Vehicle Fee", "5" to "Academic Fee")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentHistoryBinding.inflate(inflater, container, false)

        initialisers()

        return binding.root
    }

    private fun initialisers() {
        setPaymentHistoryRecyclerView()
        setPaymentHistoryInformation()
    }

    private fun setPaymentHistoryInformation() {
        paymentHistoryViewModel.paymentHistoryData.observe(viewLifecycleOwner) { feeData ->
            val gson = Gson()
            Log.d("fetchxxxx", gson.toJson(feeData))
            loadPaymentHistoryData(feeData)
        }
    }

    private fun setPaymentHistoryRecyclerView() {

        binding.transactionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        paymentHistoryAdapter = PaymentHistoryAdapter()
        binding.transactionsRecyclerView.adapter = paymentHistoryAdapter
    }

    private fun loadPaymentHistoryData(feeData: PaymentHistoryResponse) {
        // loaded data from a database or API........

        val records = mutableListOf<PaymentHistoryRecord>()

        for (i in 0 until feeData.data.size) {
            records.add(
                PaymentHistoryRecord(
                    feeData.data[i].transactionDate,
                    feeData.data[i].feeType,
                    feeData.data[i].installment,
                    feeData.data[i].amount,
                    feeData.data[i].type,
                    feeData.data[i].receipt.toString(),
                    feeData.data[i].payment_mode.toString(),
                    feeData.data[i].remark
                )
            )
        }

//        records.add(PaymentHistoryRecord("15 Apr 2025", "TXN78945", "Paid", "₹12,500"))
//        records.add(PaymentHistoryRecord("15 Apr 2025", "TXN78945", "Paid", "₹12,500"))
//        records.add(PaymentHistoryRecord("15 Apr 2025", "TXN78945", "Paid", "₹12,500"))
//        records.add(PaymentHistoryRecord("15 Apr 2025", "TXN78945", "Paid", "₹12,500"))
//        records.add(PaymentHistoryRecord("15 Apr 2025", "TXN78945", "Paid", "₹12,500"))

        paymentHistoryAdapter.setPaymentHistory(records)
    }

    data class PaymentHistoryRecord(
        val transactionDate: String,
        val feeType: String,
        val installment: String,
        val amount: String,
        val type: String,
        val receipt: String,
        val paymentMode: String?,
        val remark: String?
    )

    private inner class PaymentHistoryAdapter :
        RecyclerView.Adapter<PaymentHistoryFragment.PaymentHistoryAdapter.ViewHolder>() {
        private var paymentHistoryRecords = listOf<PaymentHistoryRecord>()

        fun setPaymentHistory(records: List<PaymentHistoryRecord>) {
            this.paymentHistoryRecords = records
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_fee_transaction, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val record = paymentHistoryRecords[position]
            val feeName = feeTypeMap[record.feeType] ?: "Academic Fee (${record.feeType})"
            holder.tvFeeType.text = "Fee Type: $feeName"
            holder.tvType.text = record.type
            holder.tvTransactionDate.text = "Transaction Date: " + getFormattedDateRange(record.transactionDate)
            holder.tvInstallment.text = "Installment: ${record.installment}"
            holder.tvRemark.text = "Remark: ${record.remark}"
            holder.tvAmount.text = "₹${record.amount}"
            holder.btnReceipt.text = record.receipt
            holder.tvPaymentMode.text = record.paymentMode

        }

        override fun getItemCount() = paymentHistoryRecords.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvTransactionDate: TextView = itemView.findViewById(R.id.tvTransactionDate)
            val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
            val tvFeeType: TextView = itemView.findViewById(R.id.tvFeeType)
            val tvType: TextView = itemView.findViewById(R.id.tvType)
            val tvInstallment: TextView = itemView.findViewById(R.id.tvInstallment)
            val tvRemark: TextView = itemView.findViewById(R.id.tvRemark)
            val btnReceipt: TextView = itemView.findViewById(R.id.btnReceipt)
            val tvPaymentMode: TextView = itemView.findViewById(R.id.tvPaymentMode)
        }
    }

    fun getFormattedDateRange(transactionDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        val transactionDateFormatted =
            outputFormat.format(inputFormat.parse(transactionDate) ?: Date())

        return transactionDateFormatted
    }


}