package com.example.schoolapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.schoolapp.R
import com.example.schoolapp.databinding.FragmentGradesBinding


class GradesFragment : Fragment() {

    private lateinit var binding: FragmentGradesBinding
    private var flag1: Boolean = false // for term1
    private var flag2: Boolean = false // for term2
    private var flag3: Boolean = false // for term3
    private var flag4: Boolean = false // for term4

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGradesBinding.inflate(inflater, container, false)

        listeners()

        return binding.root
    }

    private fun listeners() {
        binding.llTerm1.setOnClickListener {
            if(!flag1) {
                binding.llTerm1Content.visibility = View.VISIBLE
                binding.ivTerm1Arrow.setImageResource(R.drawable.ic_chevron_up)
                flag1 = !flag1
            } else {
                binding.llTerm1Content.visibility = View.GONE
                binding.ivTerm1Arrow.setImageResource(R.drawable.ic_chevron_down)
                flag1 = !flag1
            }

        }

        binding.llTerm2.setOnClickListener {
            if(!flag2) {
                binding.llTerm2Content.visibility = View.VISIBLE
                binding.ivTerm2Arrow.setImageResource(R.drawable.ic_chevron_up)
                flag2 = !flag2
            } else {
                binding.llTerm2Content.visibility = View.GONE
                binding.ivTerm2Arrow.setImageResource(R.drawable.ic_chevron_down)
                flag2 = !flag2
            }
        }

        binding.llTerm3.setOnClickListener {
            if(!flag3) {
                binding.llTerm3Content.visibility = View.VISIBLE
                binding.ivTerm3Arrow.setImageResource(R.drawable.ic_chevron_up)
                flag3 = !flag3
            } else {
                binding.llTerm3Content.visibility = View.GONE
                binding.ivTerm3Arrow.setImageResource(R.drawable.ic_chevron_down)
                flag3 = !flag3
            }
        }

        binding.llTerm4.setOnClickListener {
            if(!flag4) {
                binding.llTerm4Content.visibility = View.VISIBLE
                binding.ivTerm4Arrow.setImageResource(R.drawable.ic_chevron_up)
                flag4 = !flag4
            } else {
                binding.llTerm4Content.visibility = View.GONE
                binding.ivTerm4Arrow.setImageResource(R.drawable.ic_chevron_down)
                flag4 = !flag4
            }
        }
    }


}