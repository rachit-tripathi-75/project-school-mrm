package com.example.schoolapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.schoolapp.R
import com.example.schoolapp.databinding.FragmentBasicInfoBinding
import com.example.schoolapp.viewmodels.StudentDetailViewModel


class BasicInfoFragment : Fragment() {

    private lateinit var binding: FragmentBasicInfoBinding
    private val studentDetailViewModel: StudentDetailViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBasicInfoBinding.inflate(inflater, container, false)

        initialisers()
        setBasicInformation()
        return binding.root
    }

    private fun initialisers() {

    }

    private fun setBasicInformation() {
        studentDetailViewModel.studentData.observe(viewLifecycleOwner) { student ->
            binding.tvStudentName.text = student.studentData[0].name
            binding.tvStudentEmail.text = student.studentData[0].email
            binding.tvStudentPhoneNumber.text = student.studentData[0].mobile
            binding.tvStudentDOB.text = student.studentData[0].dateOfBirth
            binding.tvStudentAddress.text = student.studentData[0].address
        }
    }


}