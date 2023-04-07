package com.example.pdfunifier

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.multipdf.PDFMergerUtility
import java.io.File

/**
 * @author Leopoldo Todisco.
 *
 * TO DO: GET PDFS FROM PATH.
 */
class MainActivity : AppCompatActivity() {
    lateinit var buttonOne: Button
    lateinit var buttonTwo: Button
    lateinit var path: String

    /**
     * Siccome startForActivityResult è deprecato si usa registerForActivityResult.
     */
    private val pickFileActivityResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            if (result != null) {
                val selectedFileUri: Uri = result
                path = selectedFileUri.toString()
                Toast.makeText(this, selectedFileUri.toString(), Toast.LENGTH_LONG).show()
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
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.type = "*/*"
        pickFileActivityResult.launch(i.type)
    }


    /**
     * Funzione che fa il merge dei due pdf.
     */
    fun mergePdf(pdf1: File, pdf2: File, output: File) {
        PDFBoxResourceLoader.init(applicationContext)
        val merger = PDFMergerUtility()
        merger.addSource(pdf1)
        merger.addSource(pdf2)
        merger.destinationFileName = output.path
        merger.mergeDocuments(null)
    }


}

