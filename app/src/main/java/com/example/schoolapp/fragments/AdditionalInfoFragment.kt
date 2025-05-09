package com.example.schoolapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.schoolapp.R
import com.example.schoolapp.databinding.FragmentAdditionalInfoBinding
import com.example.schoolapp.viewmodels.StudentDetailViewModel


class AdditionalInfoFragment : Fragment() {

    private lateinit var binding: FragmentAdditionalInfoBinding
    private val studentDetailViewModel: StudentDetailViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAdditionalInfoBinding.inflate(inflater, container, false)

        initialisers()
        setPersonalInformation()

        return binding.root
    }

    private fun initialisers() {

    }

    private fun setPersonalInformation() {
        studentDetailViewModel.studentData.observe(viewLifecycleOwner) { student ->
            binding.tvClassAndSection.text = "Class ${student.studentData[0].standard} - Section ${student.studentData[0].sectionName.get(student.studentData[0].sectionName.length - 1)}"
            binding.tvRollNumber.text = student.studentData[0].rollNumber
            binding.tvAcademicYear.text = student.studentData[0].academicYear
            binding.tvParentName.text = "Mother: ${student.studentData[0].motherName}, Father: ${student.studentData[0].fatherName}"
            binding.tvParentPhone.text = student.studentData[0].fatherMobile
        }
    }

}