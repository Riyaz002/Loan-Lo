package com.amaan.loanlo.eligibility.model

import android.os.Bundle
import android.view.View
import com.amaan.loanlo.R
import com.amaan.loanlo.databinding.SuggestedLoanItemBinding
import com.amaan.loanlo.eligibility.view.EligibilityView
import com.amaan.loanlo.webview.WebViewFragment
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

class SuggestedLoanItem(val loan: Pair<String, String>, var viewCallback: EligibilityView.ViewCallback): AbstractItem<SuggestedLoanItem.ViewHolder>() {
    class ViewHolder(view: View): FastAdapter.ViewHolder<SuggestedLoanItem>(view){
        private val binding = SuggestedLoanItemBinding.bind(view)
        override fun bindView(item: SuggestedLoanItem, payloads: List<Any>) {
            binding.root.setOnClickListener{
                val bundle = Bundle()
                bundle.putString("url", item.loan.second)
                val webViewFragment = WebViewFragment()
                webViewFragment.arguments = bundle
                item.viewCallback.getFragmentManager()
                    .beginTransaction()
                    .addToBackStack("webView")
                    .replace(R.id.webview_fragment, webViewFragment)
                    .commit()
            }
            binding.tvName.text = item.loan.first
        }

        override fun unbindView(item: SuggestedLoanItem) {
            binding.tvName.text = null
        }

    }

    override val type: Int
        get() = identifier.toInt()
    override val layoutRes: Int
        get() = R.layout.suggested_loan_item

    override fun getViewHolder(v: View) = ViewHolder(v)
}