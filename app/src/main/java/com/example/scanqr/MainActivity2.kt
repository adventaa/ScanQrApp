package com.example.scanqr

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.scanqr.databinding.ActivityMain2Binding
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class MainActivity2 : AppCompatActivity() {
    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC
        )
        .build()
    lateinit var binding: ActivityMain2Binding
    private val REQUEST_IMAGE_CAPTURE = 1
    private var imageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main2)
        binding.apply {
            captureImage.setOnClickListener {
                takeImage()
                textView.text = ""
            }
            detectTextImageBtn.setOnClickListener {
                processImage()
            }
        }
    }

    // untuk tombol CAPTURE
    private fun takeImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        } catch (e: Exception) {
        }
    }

    // untuk hasil dari scan qr
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val extras: Bundle? = data?.extras
            imageBitmap = extras?.get("data") as Bitmap
            if (imageBitmap != null) {
                binding.imageView.setImageBitmap(imageBitmap)
            }
        }
    }

    // untuk tombol SCAN
    private fun processImage() {
        if (imageBitmap != null) {
            val image = InputImage.fromBitmap(imageBitmap!!, 0)
            val scanner = BarcodeScanning.getClient(options)
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.toString() == "[]") {
                        Toast.makeText(this, "Nothing to scan", Toast.LENGTH_SHORT).show()
                    }
                    for (barcode in barcodes) {
                        val valueType = barcode.valueType
                        // Lihat referensi API untuk daftar lengkap tipe yang didukung
                        when (valueType) {
                            Barcode.TYPE_WIFI -> {
                                val ssid = barcode.wifi!!.ssid
                                val password = barcode.wifi!!.password
                                val type = barcode.wifi!!.encryptionType

                                binding.textView.text = ssid + "\n" + password + "\n" + type
                            }
                            Barcode.TYPE_URL -> {
                                val title = barcode.url!!.title
                                val url = barcode.url!!.url
                                binding.textView.text = title + "\n" + url
                            }
                        }
                    }
                }
                .addOnFailureListener {
                }
        } else {
            Toast.makeText(this, "Please select photo", Toast.LENGTH_SHORT).show()
        }
    }
}

