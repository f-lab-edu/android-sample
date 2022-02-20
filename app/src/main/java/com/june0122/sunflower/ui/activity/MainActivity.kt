package com.june0122.sunflower.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.june0122.sunflower.R
import com.june0122.sunflower.databinding.ActivityMainBinding
import com.june0122.sunflower.ui.fragment.PlantListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PlantListFragment())
                .commit()
        }
    }
}