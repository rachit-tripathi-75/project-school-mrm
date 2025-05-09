package com.example.schoolapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolapp.R
import com.example.schoolapp.databinding.FragmentPaymentHistoryBinding
import com.google.android.material.chip.Chip


class PaymentHistoryFragment : Fragment() {

    private lateinit var binding: FragmentPaymentHistoryBinding
    private lateinit var paymentHistoryAdapter: PaymentHistoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPaymentHistoryBinding.inflate(inflater, container,  false)

        initialisers()

        return binding.root
    }

    private fun initialisers() {
        setPaymentHistoryRecyclerView()
        loadPaymentHistoryData()
    }

    private fun setPaymentHistoryRecyclerView() {

        binding.transactionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        paymentHistoryAdapter = PaymentHistoryAdapter()
        binding.transactionsRecyclerView.adapter = paymentHistoryAdapter
    }

    private fun loadPaymentHistoryData() {
        // loaded data from a database or API........

        val records = mutableListOf<PaymentHistoryRecord>()


        // Add some dummy data for other PTA Meetings......
        records.add(PaymentHistoryRecord("15 Apr 2025", "TXN78945", "Paid", "₹12,500"))
        records.add(PaymentHistoryRecord("15 Apr 2025", "TXN78945", "Paid", "₹12,500"))
        records.add(PaymentHistoryRecord("15 Apr 2025", "TXN78945", "Paid", "₹12,500"))
        records.add(PaymentHistoryRecord("15 Apr 2025", "TXN78945", "Paid", "₹12,500"))
        records.add(PaymentHistoryRecord("15 Apr 2025", "TXN78945", "Paid", "₹12,500"))

        paymentHistoryAdapter.setPaymentHistory(records)
    }

    data class PaymentHistoryRecord(val transactionDate: String, val transactionId: String, val status: String, val amount: String)

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
            holder.tvTransactionDate.text = record.transactionDate
            holder.tvTransactionId.text = record.transactionId
            holder.tvStatus.text = record.status
            holder.tvAmount.text = record.amount

        }

        override fun getItemCount() = paymentHistoryRecords.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvTransactionDate: TextView = itemView.findViewById(R.id.tvTransactionDate)
            val tvTransactionId: TextView = itemView.findViewById(R.id.tvTransactionId)
            val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
            val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        }
    }



}