package com.amaan.loanlo.input.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.amaan.loanlo.R
import com.amaan.loanlo.databinding.FragmentUserInputBinding
import com.amaan.loanlo.eligibility.EligibilityFragment
import com.amaan.loanlo.input.FieldTypes
import com.amaan.loanlo.input.UserInputFragment
import com.amaan.loanlo.main.MainActivity
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.util.*
import kotlin.math.roundToInt

class UserInputView(view: View) {
    val binding = FragmentUserInputBinding.bind(view)
    lateinit var fragmentCallback: UserInputFragment.FragmentCallback
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
            } else {
                showSnackBar("All fields must be valid" ,view)
            }
        }
    }

    private fun showSnackBar(text: String ,view: View) {
        val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setTextColor(Color.parseColor("#CF0000"))
            .setBackgroundTint(Color.parseColor("#FFFFFF"))

        val lp = FrameLayout.LayoutParams(
            snackbar.view.layoutParams.width,
            snackbar.view.layoutParams.height
        )
        lp.gravity = Gravity.TOP
        lp.setMargins(16, 16, 16, 0)
        snackbar.view.layoutParams = lp
        snackbar.show()
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
        val p = binding.amount.text?.trim().toString().toDouble()
        val t = binding.tenure.text?.trim().toString().toDouble()*12
        val r = binding.interest.text?.trim().toString().toDouble()/(t*10)
        val loan = (p*r*(1+r)*t)/(t*(1 + r)-1)
        val bundle = Bundle()
        bundle.putInt("amount", p.toInt())
        bundle.putInt("interest", binding.interest.text?.trim().toString().toDouble().toInt())
        bundle.putInt("tenure", t.toInt())
        bundle.putInt("loan", loan.toInt())
        val fragment = EligibilityFragment()
        fragment.arguments = bundle
        fragmentCallback.getFragmentManager().beginTransaction().replace(
            R.id.fragment, fragment
        ).addToBackStack("eligibility").commit()
    }

    private fun allFieldsAreValid(): Boolean {
        var isValid = true
        if(getFieldObjects(FieldTypes.AGE).first.text.isNullOrEmpty() || getFieldObjects(FieldTypes.AGE).second.text.isNotEmpty()) isValid = false
        if(getFieldObjects(FieldTypes.CREDIT_SCORE).first.text.isNullOrEmpty() || getFieldObjects(FieldTypes.CREDIT_SCORE).second.text.isNotEmpty()) isValid = false
        if(getFieldObjects(FieldTypes.GROSS_INCOME).first.text.isNullOrEmpty() || getFieldObjects(FieldTypes.GROSS_INCOME).second.text.isNotEmpty()) isValid = false
        if(getFieldObjects(FieldTypes.INTEREST_RATE).first.text.isNullOrEmpty() || getFieldObjects(FieldTypes.INTEREST_RATE).second.text.isNotEmpty()) isValid = false
        if(getFieldObjects(FieldTypes.LOAN_AMOUNT).first.text.isNullOrEmpty() || getFieldObjects(FieldTypes.LOAN_AMOUNT).second.text.isNotEmpty()) isValid = false
        if(getFieldObjects(FieldTypes.TENURE).first.text.isNullOrEmpty() || getFieldObjects(FieldTypes.TENURE).second.text.isNotEmpty()) isValid = false
        return isValid
    }

    private fun initializeInputField(selectedField: FieldTypes, textWatcher: TextWatcher) {
        val (et, _, slider) = getFieldObjects(selectedField)
        et.hint = selectedField.id
        et.addTextChangedListener(textWatcher)
        et.setOnFocusChangeListener { view, b ->
            fieldType = if (!b) {
                validate(selectedField)
                null
            } else selectedField
        }
        slider.addOnChangeListener { slider, value, fromUser ->
            et.setText(value.roundToInt().toString())
            et.setSelection(et.text?.length?:0)

            try{
                timer?.cancel()
                timer = Timer()
                timer?.schedule(
                    object: TimerTask(){
                        override fun run() {
                            validate(selectedField)
                        }
                    }, 1000
                )
            } catch (e: Exception){

            }
        }
    }

    private fun clearError(selectedField: FieldTypes) {
        getFieldObjects(selectedField).second.text = null
    }

    fun validate(selectedField: FieldTypes) {
        val (et,htv, slider) = getFieldObjects(selectedField)
        if (et.text?.trim().isNullOrEmpty()) {
            htv.text = "field is required"
        }
        try {
            when (selectedField) {
                FieldTypes.AGE -> {
                    if (et.text.toString().toInt() !in 21..60) {
                        htv.text = "invalid age"
                    } else {
                        slider.value = et.text.toString().toFloat()
                        clearError(selectedField)
                    }
                }
                FieldTypes.GROSS_INCOME -> {
                    if (et.text.toString().toInt() !in 25000..1000000) {
                        htv.text = "gross income should at least be 25000"
                    } else {
                        slider.value = et.text.toString().toFloat()
                        clearError(selectedField)
                    }
                }
                FieldTypes.LOAN_AMOUNT -> {
                    if (et.text.toString().toInt() !in 1000..1000000) {
                        htv.text = "loan amount cannot be less than 1000"
                    } else {
                        slider.value = et.text.toString().toFloat()
                        clearError(selectedField)
                    }
                }
                FieldTypes.INTEREST_RATE -> {
                    if (et.text.toString().toInt() !in 5..50) {
                        htv.text = "interest rate cannot be negative"
                    } else {
                        slider.value = et.text.toString().toFloat()
                        clearError(selectedField)
                    }
                }
                FieldTypes.CREDIT_SCORE -> {
                    if (et.text.toString().toInt() !in 750..1000) {
                        htv.text = "credit score must be greater than or equal to 750"
                    } else {
                        slider.value = et.text.toString().toFloat()
                        clearError(selectedField)
                    }
                }
                FieldTypes.TENURE -> {
                    if (et.text.toString().toInt() !in 1..15) {
                        htv.text = "tenure should be between 1 to 15"
                    } else {
                        slider.value = et.text.toString().toFloat()
                        clearError(selectedField)
                    }
                }
            }
        } catch (e: Exception) {
            if(et.text?.trim().toString().isEmpty()){
                htv.text = "field is required"
            } else htv.text = "invalid input"
        }
    }

    private fun getFieldObjects(selectedField: FieldTypes): Triple<TextInputEditText, TextView, Slider> {
        return when (selectedField) {
            FieldTypes.AGE -> Triple(binding.age, binding.ageHint, binding.ageSlider)
            FieldTypes.GROSS_INCOME -> Triple(binding.gIncome, binding.gIncomeHint, binding.gSlider )
            FieldTypes.LOAN_AMOUNT -> Triple(binding.amount, binding.amountHint, binding.amountSlider)
            FieldTypes.CREDIT_SCORE -> Triple(binding.cScore, binding.cScoreHint, binding.cSlider)
            FieldTypes.INTEREST_RATE -> Triple(binding.interest, binding.interestHint, binding.interestSlider)
            FieldTypes.TENURE -> Triple(binding.tenure, binding.tenureHint, binding.tenureSlider)
        }
    }

}