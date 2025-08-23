package com.example.schoolapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schoolapp.adapters.LeaveRequestAdapter
import com.example.schoolapp.classes.ApiClient
import com.example.schoolapp.classes.PrefsManager
import com.example.schoolapp.databinding.FragmentLeaveRequestListBinding
import com.example.schoolapp.models.LeaveRequestModel
import com.example.schoolapp.responses.GetStudentLeaveRequestResponse
import com.example.schoolapp.viewmodels.LeaveRequestListViewModel
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response


class LeaveRequestListFragment : Fragment() {

    private var _binding: FragmentLeaveRequestListBinding? = null
    private val binding get() = _binding!!

    private val leaveRequestListViewModel: LeaveRequestListViewModel by viewModels()
    private lateinit var adapter: LeaveRequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeaveRequestListBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeLeaveRequestData()
        loadRequests()
        updateStatusCounts()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateStatusCounts()
    }

    private fun setupRecyclerView() {
        adapter = LeaveRequestAdapter(emptyList())
        binding.rvRequests.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRequests.adapter = adapter
    }

    private fun observeLeaveRequestData() {
        leaveRequestListViewModel.leaveRequestListData.observe(viewLifecycleOwner) { s ->
            val leaveRequestsList = mutableListOf<LeaveRequestModel>()
            for (item in s.data) {
                leaveRequestsList.add(
                    LeaveRequestModel(
                        item.id,
                        item.reason,
                        item.lType,
                        item.fDate,
                        item.tDate,
                        description = "",
                        item.approveStatus,
                        item.created
                    )
                )
            }
            adapter.updateList(leaveRequestsList.reversed())
        }
    }

    private fun updateStatusCounts() {
        // Optional: update your UI status counts here if needed
        // binding.tvPendingCount.text = LeaveRequestManager.getPendingCount().toString()
        // binding.tvAcceptedCount.text = LeaveRequestManager.getAcceptedCount().toString()
        // binding.tvRejectedCount.text = LeaveRequestManager.getRejectedCount().toString()
    }

    private fun loadRequests() {
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                delay(1500)
                ApiClient.leaveRequestListInstance.getStudentLeaveRequests(
                    "application/x-www-form-urlencoded",
                    "ci_session=tjfo3rn4v6anihl635kh20n2bbm5liup",
                    PrefsManager.getUserDetailedInformation(requireContext()).studentData.get(0).enrollment
                ).enqueue(object : retrofit2.Callback<GetStudentLeaveRequestResponse> {

                    override fun onResponse(
                        call: Call<GetStudentLeaveRequestResponse?>,
                        response: Response<GetStudentLeaveRequestResponse?>
                    ) {
                        binding.progressBar.visibility = View.GONE

                        val gson = Gson()
                        if (response.isSuccessful && response.body() != null) {
                            val s = response.body()
                            if (s?.status == 1 && s.data.isNotEmpty()) {
                                binding.llMainContent.visibility = View.VISIBLE
                                leaveRequestListViewModel.setLeaveRequestListData(s)
                            } else {
                                binding.llNoDataFound.visibility = View.VISIBLE
                            }
                        } else {
                            binding.llInternalServerError.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(
                        call: Call<GetStudentLeaveRequestResponse?>,
                        t: Throwable
                    ) {
                        binding.progressBar.visibility = View.GONE
                        binding.llInternalServerError.visibility = View.VISIBLE
                    }

                })
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
