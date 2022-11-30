package com.amaan.loanlo.start

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.amaan.loanlo.R
import com.amaan.loanlo.databinding.FragmentStartBinding
import com.amaan.loanlo.input.UserInputFragment

class StartFragment : Fragment() {

    companion object {
        fun newInstance() = StartFragment()
    }

    private lateinit var viewModel: StartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentStartBinding>(
            inflater,
            R.layout.fragment_start,
            container,
            false
        )
        binding.bAction.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragment, UserInputFragment())
                .addToBackStack("user-input").commit()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[StartViewModel::class.java]
    }

}