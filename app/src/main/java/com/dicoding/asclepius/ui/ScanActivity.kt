package com.dicoding.asclepius.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.dicoding.asclepius.R
import com.dicoding.asclepius.database.entity.ScanHistory
import com.dicoding.asclepius.databinding.ActivityScanBinding
import com.dicoding.asclepius.ml.CancerClassification
import com.dicoding.asclepius.model.HistoryModel
import com.dicoding.asclepius.util.Time
import org.tensorflow.lite.support.image.TensorImage
import java.text.NumberFormat


class ScanActivity : AppCompatActivity() {

    private val binding by lazy { ActivityScanBinding.inflate(layoutInflater) }
    private var imageUri: Uri? = null
    private var cancerAnalyzeResult: String? = null
    private lateinit var cancerClassifier: CancerClassification
    private val viewModel by viewModels<HistoryModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        cancerClassifier = CancerClassification.newInstance(this)

        with(binding) {
            galleryButton.setOnClickListener {
//                pickImage()
                startCrop()
            }
            analyzeButton.setOnClickListener {
                imageUri?.let {
                    saveToLocalDB()
                    moveToResult()
                } ?: run {
                    showToast(getString(R.string.empty_image_warning))
                }
            }
            popupMenu.setOnClickListener { showPopupMenu() }
            toolBar.setNavigationOnClickListener { finish() }
        }
    }

    private fun analyzeImage(imageResult: Uri) {
        imageUri = imageResult
        binding.previewImageView.setImageURI(imageUri)
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)

        val image = TensorImage.fromBitmap(bitmap)

        val outputs = cancerClassifier.process(image)
        val probability = outputs.probabilityAsCategoryList
        val maxProbability = probability.maxByOrNull { it.score }

        if (maxProbability != null) {
            cancerAnalyzeResult = "Terdeteksi: ${maxProbability.label} ${
                NumberFormat.getPercentInstance().format(maxProbability.score)
            }"
        }
    }

    private fun moveToResult() {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_IMAGE_URI, imageUri.toString())
            putExtra(ResultActivity.EXTRA_RESULT, cancerAnalyzeResult)
        }
        startActivity(intent)
    }

    private fun saveToLocalDB() {
        viewModel.insert(
            ScanHistory(
                image = imageUri.toString(), time = Time.getSecond(), result = cancerAnalyzeResult!!
            )
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showPopupMenu() {
        PopupMenu(this, binding.popupMenu).apply {
            menuInflater.inflate(R.menu.popupmenu_scan, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.clearImage -> {
                        binding.previewImageView.setImageResource(R.drawable.ic_place_holder)
                        imageUri = null
                    }

                    R.id.riwayat -> startActivity(
                        Intent(
                            this@ScanActivity, RiwayatActivity::class.java
                        )
                    )
                }
                true
            }
            show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancerClassifier.close()
    }

    private val cropImage = registerForActivityResult(
        CropImageContract()
    ) { result ->
        if (result.isSuccessful) {
            imageUri = result.uriContent
            analyzeImage(imageUri!!)
        } else {
            imageUri = null
        }
    }

    private fun startCrop() {
        val pickUri: Uri? = null
        val cropImageOptions = CropImageOptions().apply {
            imageSourceIncludeGallery = true
            imageSourceIncludeCamera = false
            guidelines
        }
        val cropImageContractOptions = CropImageContractOptions(pickUri, cropImageOptions)
        cropImage.launch(cropImageContractOptions)
    }
}