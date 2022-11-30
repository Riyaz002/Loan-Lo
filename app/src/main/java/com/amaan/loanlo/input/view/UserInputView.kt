package com.amaan.loanlo.input.view

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import com.amaan.loanlo.databinding.FragmentUserInputBinding
import com.amaan.loanlo.input.FieldTypes
import com.amaan.loanlo.main.MainActivity
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class UserInputView(view: View) {
    val binding = FragmentUserInputBinding.bind(view)
    var fieldType: FieldTypes? = null
    var timer: Timer? = null

    init {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                fieldType?.let {
                    timer?.cancel()
                    clearError(it)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                fieldType?.let {
                    timer?.cancel()
                    timer = Timer()
                    timer?.schedule(
                        object : TimerTask(){
                            override fun run() {
                                (view.context as MainActivity).runOnUiThread{
                                    validate(it)
                                }
                            }
                        },
                        1000
                    )
                    validate(it)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }

        initializeInputField(FieldTypes.AGE, textWatcher)
        initializeInputField(FieldTypes.GROSS_INCOME, textWatcher)
        initializeInputField(FieldTypes.CREDIT_SCORE, textWatcher)
        initializeInputField(FieldTypes.LOAN_AMOUNT, textWatcher)
        initializeInputField(FieldTypes.INTEREST_RATE, textWatcher)
        initializeInputField(FieldTypes.TENURE, textWatcher)

        binding.bAction.setOnClickListener {
            validateAll()
            if (allFieldsAreValid()) {
                calculateLoan()
            }
        }
    }

    private fun validateAll() {
        validate(FieldTypes.AGE)
        validate(FieldTypes.GROSS_INCOME)
        validate(FieldTypes.CREDIT_SCORE)
        validate(FieldTypes.LOAN_AMOUNT)
        validate(FieldTypes.TENURE)
        validate(FieldTypes.INTEREST_RATE)
    }

    private fun calculateLoan() {

    }

    private fun allFieldsAreValid(): Boolean {
        var isValid = true
        if (!getFieldObjects(FieldTypes.AGE).first.text.isNullOrEmpty()
            || getFieldObjects(FieldTypes.AGE).second.error != null ||
            !getFieldObjects(FieldTypes.GROSS_INCOME).first.text.isNullOrEmpty()
            || getFieldObjects(FieldTypes.GROSS_INCOME).second.error != null ||
            !getFieldObjects(FieldTypes.INTEREST_RATE).first.text.isNullOrEmpty()
            || getFieldObjects(FieldTypes.INTEREST_RATE).second.error != null ||
            !getFieldObjects(FieldTypes.LOAN_AMOUNT).first.text.isNullOrEmpty()
            || getFieldObjects(FieldTypes.INTEREST_RATE).second.error != null ||
            !getFieldObjects(FieldTypes.CREDIT_SCORE).first.text.isNullOrEmpty()
            || getFieldObjects(FieldTypes.CREDIT_SCORE).second.error != null ||
            !getFieldObjects(FieldTypes.TENURE).first.text.isNullOrEmpty()
            || getFieldObjects(FieldTypes.TENURE).second.error != null
        ) {
            isValid = false
        }
        return isValid
    }

    private fun initializeInputField(selectedField: FieldTypes, textWatcher: TextWatcher) {
        val (et, _) = getFieldObjects(selectedField)
        et.hint = selectedField.id
        et.addTextChangedListener(textWatcher)
        et.setOnFocusChangeListener { view, b ->
            fieldType = if (!b) {
                validate(selectedField)
                null
            } else selectedField
        }
    }

    private fun clearError(selectedField: FieldTypes) {
        getFieldObjects(selectedField).second.text = null
    }

    fun validate(selectedField: FieldTypes) {
        val (et,htv) = getFieldObjects(selectedField)
        if (et.text?.trim().isNullOrEmpty()) {
            htv.text = "field is required"
        }
        try {
            when (selectedField) {
                FieldTypes.AGE -> {
                    if (et.text.toString().toInt() !in 18..60) {
                        htv.text = "invalid age"
                    } else clearError(selectedField)
                }
                FieldTypes.GROSS_INCOME -> {
                    if (et.text.toString().toInt() < 0) {
                        htv.text = "gross income cannot be negative"
                    } else clearError(selectedField)
                }
                FieldTypes.LOAN_AMOUNT -> {
                    if (et.text.toString().toInt() < 10000) {
                        htv.text = "Loan amount cannot less than 10000"
                    } else clearError(selectedField)
                }
                FieldTypes.INTEREST_RATE -> {
                    if (et.text.toString().toInt() < 10000) {
                        htv.text = "interest rate cannot be negative"
                    } else clearError(selectedField)
                }
                FieldTypes.CREDIT_SCORE -> {
                    if (et.text.toString().toInt() < 0) {
                        htv.text = "credit score cannot be negative"
                    } else clearError(selectedField)
                }
                FieldTypes.TENURE -> {
                    if (et.text.toString().toInt() < 0) {
                        htv.text = "tenure cannot be negative"
                    } else clearError(selectedField)
                }
            }
        } catch (e: Exception) {
            if(et.text?.trim().toString().isEmpty()){
                htv.text = "field is required"
            } else htv.text = "invalid input"
        }
    }

    private fun getFieldObjects(selectedField: FieldTypes): Pair<TextInputEditText, TextView> {
        return when (selectedField) {
            FieldTypes.AGE -> Pair(binding.age, binding.ageHint)
            FieldTypes.GROSS_INCOME -> Pair(binding.gIncome, binding.gIncomeHint)
            FieldTypes.LOAN_AMOUNT -> Pair(binding.amount, binding.amountHint)
            FieldTypes.CREDIT_SCORE -> Pair(binding.cScore, binding.cScoreHint)
            FieldTypes.INTEREST_RATE -> Pair(binding.interest, binding.interestHint)
            FieldTypes.TENURE -> Pair(binding.tenure, binding.tenureHint)
        }
    }
}