package com.mopsicus.downloadertv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mopsicus.downloadertv.databinding.ActivityMainBinding

/**
 * Main Activity for Android TV application
 * Manages navigation between Download and Copy fragments
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up menu buttons
        setupMenuButtons()
        
        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(DownloadFragment())
            binding.btnDownload.requestFocus()
        }
    }
    
    /**
     * Configure menu button click listeners
     */
    private fun setupMenuButtons() {
        binding.btnDownload.setOnClickListener {
            loadFragment(DownloadFragment())
        }
        
        binding.btnCopy.setOnClickListener {
            loadFragment(CopyFragment())
        }
    }
    
    /**
     * Load a fragment into the container
     * @param fragment Fragment to display
     */
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}