package com.example.opsc7213_goalignite

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * A simple [Fragment] subclass.
 * Use the [DocumentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DocumentFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DocumentAdapter
    private var documentList = mutableListOf<Document>()

    private val sharedPreferences by lazy {
        requireActivity().getSharedPreferences("DocumentPrefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_documents, container, false)

        val addFileButton: ImageView = view.findViewById(R.id.add)
        recyclerView = view.findViewById(R.id.recycler_view_files)

        documentList = getSavedFiles().toMutableList()

        // Initialize RecyclerView
        adapter = DocumentAdapter(documentList) { document ->
            // Handle file viewing here
            openFile(document)
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // Handle Add File button click
        addFileButton.setOnClickListener {
            openFilePicker()
        }

        return view
    }
    private fun getSavedFiles(): List<Document> {
        val json = sharedPreferences.getString("document_list", null)
        return if (json != null) {
            // Deserialize the JSON back to a list of DocumentModel
            val gson = Gson()
            val type = object : TypeToken<List<Document>>() {}.type
            gson.fromJson(json, type)
        } else {
            // Return an empty list if no saved files
            emptyList()
        }
    }
    // Function to save files to SharedPreferences
    private fun saveFiles() {
        val gson = Gson()
        val json = gson.toJson(documentList) // Serialize the documentList to JSON
        sharedPreferences.edit().putString("document_list", json).apply() // Save to SharedPreferences
    }

    // Function to open file picker
    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*" // Allows all types first, but will restrict below
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        // Specify MIME types for PDF and Word documents
        intent.setType("application/pdf") // PDF
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document")) // Word

        startActivityForResult(Intent.createChooser(intent, "Select Document"), 1000)
    }
    private fun showTitleInputDialog(uri: Uri) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Enter File Title")

        // Set up the input
        val input = EditText(requireContext())
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val title = input.text.toString()
            val filePath = uri.toString()

            // Add the document to the list as DocumentModel
            documentList.add(Document(title, filePath))
            adapter.notifyDataSetChanged() // Notify adapter of data change
            saveFiles() // Save the updated list
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }
    // Handling the result of file selection
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == AppCompatActivity.RESULT_OK) {
            val uri = data?.data ?: return
            val fileName = uri.lastPathSegment ?: "Unknown File"
            val filePath = uri.toString()

            val document = Document(fileName, uri.toString())


            // Add the document to the list and notify adapter
            documentList.add(Document(fileName, filePath))
            adapter.notifyDataSetChanged()
            saveFiles()
        }
    }
    private fun getFileName(uri: Uri): String? {
        var fileName: String? = null
        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                fileName = it.getString(nameIndex)
            }
        }
        return fileName
    }
    // Function to open the file when clicked in RecyclerView
    private fun openFile(document: Document) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(document.filePath), "*/*")
        startActivity(intent)
    }
    companion object {
        private const val FILE_PICKER_REQUEST_CODE = 1000
    }
}