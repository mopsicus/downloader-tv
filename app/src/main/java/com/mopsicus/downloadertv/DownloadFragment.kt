package com.mopsicus.downloadertv

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.mopsicus.downloadertv.databinding.FragmentDownloadBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Fragment for downloading files from URL
 * Handles permission requests and file downloads
 */
class DownloadFragment : Fragment() {
    
    private var _binding: FragmentDownloadBinding? = null
    private val binding get() = _binding!!
    
    /**
     * Permission launcher for write external storage
     */
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startDownload()
        } else {
            showStatus(getString(R.string.download_permission_denied), true)
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloadBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
    }
    
    /**
     * Configure button listeners
     */
    private fun setupButtons() {
        binding.btnDownloadFile.setOnClickListener {
            val url = binding.etUrl.text.toString().trim()
            if (url.isEmpty() || !isValidUrl(url)) {
                showStatus(getString(R.string.download_invalid_url), true)
                return@setOnClickListener
            }
            checkPermissionAndDownload()
        }
    }
    
    /**
     * Validate URL format
     */
    private fun isValidUrl(url: String): Boolean {
        return url.startsWith("http://") || url.startsWith("https://")
    }
    
    /**
     * Check storage permission and start download
     */
    private fun checkPermissionAndDownload() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                // Android 10+ doesn't need storage permission for Downloads
                startDownload()
            }
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                startDownload()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }
    
    /**
     * Download file from URL
     */
    private fun startDownload() {
        val urlString = binding.etUrl.text.toString().trim()
        showStatus(getString(R.string.download_in_progress), false)
        binding.btnDownloadFile.isEnabled = false
        
        lifecycleScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    downloadFile(urlString)
                }
                
                if (result) {
                    showStatus(getString(R.string.download_success), false)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.download_success),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    showStatus(getString(R.string.download_error), true)
                }
            } catch (e: Exception) {
                showStatus("${getString(R.string.download_error)}: ${e.message}", true)
            } finally {
                binding.btnDownloadFile.isEnabled = true
            }
        }
    }
    
    /**
     * Perform actual file download
     */
    private fun downloadFile(urlString: String): Boolean {
        return try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.connect()
            
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                return false
            }
            
            // Get filename from URL
            val fileName = urlString.substring(urlString.lastIndexOf('/') + 1)
                .ifEmpty { "downloaded_file" }
            
            // Save to Downloads directory
            val downloadsDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            )
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs()
            }
            
            val file = File(downloadsDir, fileName)
            
            // Download file
            connection.inputStream.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            
            connection.disconnect()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Show status message
     */
    private fun showStatus(message: String, isError: Boolean) {
        binding.tvStatus.text = message
        binding.tvStatus.visibility = View.VISIBLE
        binding.tvStatus.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (isError) android.R.color.holo_red_light else R.color.text_secondary
            )
        )
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
