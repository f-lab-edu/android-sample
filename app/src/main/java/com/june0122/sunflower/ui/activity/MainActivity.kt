package com.june0122.sunflower.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.june0122.sunflower.R
import com.june0122.sunflower.ui.fragment.PlantListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PlantListFragment.newInstance())
                .commit() // commit, commitNow 등의 차이점?
        }
    }
}