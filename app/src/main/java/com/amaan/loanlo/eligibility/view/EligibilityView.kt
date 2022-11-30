package com.amaan.loanlo.eligibility.view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.amaan.loanlo.databinding.FragmentEligibilityBinding
import com.amaan.loanlo.data.util.SuggestedLoans
import com.amaan.loanlo.eligibility.model.SuggestedLoanItem
import com.github.mikephil.charting.data.PieData

import com.github.mikephil.charting.data.PieDataSet

import com.github.mikephil.charting.data.PieEntry
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter


class EligibilityView(view: View, arguments: Bundle?, val fragmentManager: FragmentManager) {
    val binding = FragmentEligibilityBinding.bind(view)
    val items = ItemAdapter<SuggestedLoanItem>()
    val fastAdapter = FastAdapter.with(items)

    init {
        showPieChart(arguments)
        binding.tvEmiAmount.setText("₹" + (arguments?.getInt("loan") ?: 0).toString())
        binding.rvSuggestion.addItemDecoration(
            DividerItemDecoration(binding.root.context, RecyclerView.VERTICAL)
        )
        binding.rvSuggestion.adapter = fastAdapter
        val list = ArrayList<SuggestedLoanItem>()
        val callback = object : ViewCallback {
            override fun getFragmentManager(): FragmentManager  = fragmentManager
        }
        SuggestedLoans.getExternalSuggestionLinks().forEach { it ->
            list.add(SuggestedLoanItem(it, callback).also {
            })
        }
        items.add(list)
    }

    interface ViewCallback {
        fun getFragmentManager(): FragmentManager
    }

    private fun showPieChart(args: Bundle?) {
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val label = "type"

        //initializing data
        val typeAmountMap: MutableMap<String, Int> = HashMap()
        typeAmountMap["Principle Amount"] = args?.getInt("amount") ?: 0
        typeAmountMap["Interest Amount"] =
            args?.getInt("interest")?.times(args.getInt("tenure")) ?: 0

        //initializing colors for the entries
        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor("#03a9f4"))
        colors.add(Color.parseColor("#01579b"))
        colors.add(Color.parseColor("#FF018786"))
        colors.add(Color.parseColor("#FF3700B3"))
        colors.add(Color.parseColor("#ff5f67"))
        colors.add(Color.parseColor("#3ca567"))

        //input data and fit data into pie chart entry
        for (type in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
        }

        //collecting the entries with label name
        val pieDataSet = PieDataSet(pieEntries, label)
        //setting text size of the value
        pieDataSet.valueTextSize = 12f
        //providing color list for coloring different entries
        pieDataSet.colors = colors
        //grouping the data set from entry to chart
        val pieData = PieData(pieDataSet)
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true)
        binding.pieChartView.setData(pieData)
        binding.pieChartView.invalidate()
        binding.pieChartView.setCenterTextColor(Color.parseColor("#555555"))
    }
}