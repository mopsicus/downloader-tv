package com.mopsicus.downloadertv

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.mopsicus.downloadertv.databinding.FragmentCopyBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Fragment for file selection and copying
 * Allows users to pick files, open them, and copy content to clipboard
 */
class CopyFragment : Fragment() {
    
    private var _binding: FragmentCopyBinding? = null
    private val binding get() = _binding!!
    
    private var selectedFileUri: Uri? = null
    
    /**
     * File picker launcher
     */
    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            handleFileSelected(it)
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCopyBinding.inflate(inflater, container, false)
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
        binding.btnSelectFile.setOnClickListener {
            openFilePicker()
        }
        
        binding.btnOpenFile.setOnClickListener {
            selectedFileUri?.let { uri ->
                openFile(uri)
            }
        }
        
        binding.btnCopyToClipboard.setOnClickListener {
            selectedFileUri?.let { uri ->
                copyFileToClipboard(uri)
            }
        }
    }
    
    /**
     * Open file picker dialog
     */
    private fun openFilePicker() {
        // Prepare intent to check if there is any activity to handle document picking
        val pickIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }

        val resolver = requireContext().packageManager
        val resolved = resolver.queryIntentActivities(pickIntent, 0)
        if (resolved.isNullOrEmpty()) {
            // No handler installed on device/emulator
            showInstallFileManagerDialog()
            return
        }

        // Launch system picker using Activity Result API
        filePickerLauncher.launch(arrayOf("*/*"))
    }

    /**
     * Show a friendly dialog/Toast prompting the user to install a file manager
     */
    private fun showInstallFileManagerDialog() {
        // Use a Toast for simplicity; on TV you might want a dialog with an action
        Toast.makeText(
            requireContext(),
            "No file picker available. Install a file manager (Files by Google) or create an AVD with Google Play.",
            Toast.LENGTH_LONG
        ).show()
    }
    
    /**
     * Handle file selection
     */
    private fun handleFileSelected(uri: Uri) {
        selectedFileUri = uri
        
        // Get file name from URI
        val fileName = getFileName(uri)
        binding.tvFileName.text = fileName
        
        // Enable action buttons
        binding.btnOpenFile.isEnabled = true
        binding.btnCopyToClipboard.isEnabled = true
        
        // Request focus on open button
        binding.btnOpenFile.requestFocus()
        
        showStatus(getString(R.string.copy_no_file_selected), false)
    }
    
    /**
     * Get file name from URI
     */
    private fun getFileName(uri: Uri): String {
        return try {
            val cursor = requireContext().contentResolver.query(
                uri, null, null, null, null
            )
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        return it.getString(nameIndex)
                    }
                }
            }
            uri.lastPathSegment ?: "Unknown file"
        } catch (e: Exception) {
            uri.lastPathSegment ?: "Unknown file"
        }
    }
    
    /**
     * Open file with system viewer
     */
    private fun openFile(uri: Uri) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, requireContext().contentResolver.getType(uri))
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Cannot open file: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    
    /**
     * Copy file content to clipboard
     */
    private fun copyFileToClipboard(uri: Uri) {
        binding.btnCopyToClipboard.isEnabled = false
        showStatus(getString(R.string.download_in_progress), false)
        
        lifecycleScope.launch {
            try {
                val content = withContext(Dispatchers.IO) {
                    readFileContent(uri)
                }
                
                if (content != null) {
                    // Check file size (limit to 1MB for clipboard)
                    if (content.length > 1024 * 1024) {
                        showStatus(getString(R.string.copy_file_too_large), true)
                    } else {
                        copyToClipboard(content)
                        showStatus(getString(R.string.copy_success), false)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.copy_success),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    showStatus(getString(R.string.copy_error), true)
                }
            } catch (e: Exception) {
                showStatus("${getString(R.string.copy_error)}: ${e.message}", true)
            } finally {
                binding.btnCopyToClipboard.isEnabled = true
            }
        }
    }
    
    /**
     * Read file content
     */
    private fun readFileContent(uri: Uri): String? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            inputStream?.use { stream ->
                BufferedReader(InputStreamReader(stream)).use { reader ->
                    reader.readText()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Copy text to clipboard
     */
    private fun copyToClipboard(text: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("File Content", text)
        clipboard.setPrimaryClip(clip)
    }
    
    /**
     * Show status message
     */
    private fun showStatus(message: String, isError: Boolean) {
        binding.tvCopyStatus.text = message
        binding.tvCopyStatus.visibility = View.VISIBLE
        binding.tvCopyStatus.setTextColor(
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
