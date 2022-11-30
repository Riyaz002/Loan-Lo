package com.amaan.loanlo.eligibility

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amaan.loanlo.R
import com.amaan.loanlo.eligibility.view.EligibilityView

class EligibilityFragment : Fragment() {

    companion object {
        fun newInstance() = EligibilityFragment()
    }

    private lateinit var viewModel: EligibilityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_eligibility, container, false)
        EligibilityView(view, arguments, parentFragmentManager)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EligibilityViewModel::class.java)
    }

}