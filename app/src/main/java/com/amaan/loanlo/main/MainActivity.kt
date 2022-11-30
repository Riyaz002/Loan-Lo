package com.amaan.loanlo.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amaan.loanlo.R
import com.amaan.loanlo.start.StartFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.fragment, StartFragment()).commit()
    }
}