package com.amaan.loanlo.input

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.amaan.loanlo.R
import com.amaan.loanlo.databinding.FragmentStartBinding.inflate
import com.amaan.loanlo.databinding.FragmentUserInputBinding
import com.amaan.loanlo.input.view.UserInputView
import java.util.zip.Inflater

class UserInputFragment : Fragment() {

    companion object {
        fun newInstance() = UserInputFragment()
    }

    private lateinit var viewModel: UserInputViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_user_input, container, false)
        val userView = UserInputView(view)
        userView.fragmentCallback = object : FragmentCallback {
            override fun getContext(): Context? = context
            override fun getFragmentManager(): FragmentManager = parentFragmentManager
        }

        return view
    }

    interface FragmentCallback{
        fun getContext(): Context?
        fun getFragmentManager(): FragmentManager
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserInputViewModel::class.java)
    }
}

interface CallBack {
    fun getFragmentManager(): FragmentManager
}

enum class FieldTypes(val id: String){
    AGE("Age"),
    GROSS_INCOME("Gross income"),
    CREDIT_SCORE("Credit score"),
    LOAN_AMOUNT("Loan amount"),
    TENURE("Tenure"),
    INTEREST_RATE("Interest rate")
}