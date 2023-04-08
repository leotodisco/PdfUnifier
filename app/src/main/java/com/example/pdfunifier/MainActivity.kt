package com.example.pdfunifier

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.multipdf.PDFMergerUtility

import java.io.*


/**
 * @author Leopoldo Todisco.
 *
 * TO DO: GET PDFS FROM PATH.
 */
class MainActivity : AppCompatActivity() {
    lateinit var buttonOne: Button
    lateinit var buttonTwo: Button
    lateinit var path: String
    private var file1: InputStream? = null
    private var file2: InputStream? = null


    /**
     * Siccome startForActivityResult è deprecato si usa registerForActivityResult.
     */
    private val pickFileActivityResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            if (result != null) {
                val selectedFileUri: Uri = result
                Toast.makeText(this, selectedFileUri.toString(), Toast.LENGTH_LONG).show()
                path = selectedFileUri.toString()
                val contentResolver = contentResolver
                if (file1 == null) {
                    file1 = contentResolver.openInputStream(selectedFileUri)
                }  else {
                    file2 = contentResolver.openInputStream(selectedFileUri)

                    mergePdf(file1, file2)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonOne = findViewById(R.id.browser1)
        buttonTwo = findViewById(R.id.browser2)

        buttonOne.setOnClickListener { onBrowserButtonClick() }
        buttonTwo.setOnClickListener { onBrowserButtonClick() }
    }

    /**
     * Funzione che viene chiamata quando si vuole accedere al file system
     * per specificare un file.
     * Usa intent per ottenere il file.
     */
    private fun onBrowserButtonClick() {
        val i = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        i.type = "*/*"
        i.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        pickFileActivityResult.launch(i.type)
    }


    /**
     * Funzione che fa il merge dei due pdf.
     */
    fun mergePdf(pdf1: InputStream?, pdf2: InputStream?) {
        PDFBoxResourceLoader.init(applicationContext)

        println("ciao sono nella funzione merge\n")
        val merger = PDFMergerUtility()
        merger.addSource(pdf1)
        merger.addSource(pdf2)

        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val destinationFile = File(folder, "risultato.pdf")
        if (!destinationFile.exists()) {
            destinationFile.createNewFile()
            Toast.makeText(this, "file creato con successo", Toast.LENGTH_LONG).show()
        }

        merger.destinationFileName = destinationFile.absolutePath
        merger.mergeDocuments(null)
    }


}

