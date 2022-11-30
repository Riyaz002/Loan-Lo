package com.amaan.loanlo.data.util

object SuggestedLoans {
    private val links: ArrayList<Pair<String, String>> = arrayListOf()
    init {
        links.add(Pair("HDFC","https://www.hdfcbank.com/personal/borrow/popular-loans/personal-loan"))
        links.add(Pair("Kotak", "https://www.kotak.com/en/personal-banking/loans/personal-loan.html"))
        links.add(Pair("Bank of Baroda", "https://www.bankofbaroda.in/personal-banking/loans"))
        links.add(Pair("IDFC First Bank","https://www.idfcfirstbank.com/personal-banking/loans/personal-loanhtml"))
        links.add(Pair("Union Bank of India","https://www.unionbankofindia.co.in/english/personal-loan.aspx"))
        links.add(Pair("YES BANK", "https://www.yesbank.in/yes-bank-loans"))
    }
    fun getExternalSuggestionLinks(): List<Pair<String, String>>{
        return links
    }
}