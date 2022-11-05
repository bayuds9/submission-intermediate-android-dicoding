package com.flowerencee9.storyapp.screens.formuploader

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.flowerencee9.storyapp.R
import com.flowerencee9.storyapp.databinding.ActivityFormUploaderBinding
import com.flowerencee9.storyapp.models.request.ContentUploaderRequest
import com.flowerencee9.storyapp.support.*
import com.flowerencee9.storyapp.support.customs.CustomInput
import com.flowerencee9.storyapp.support.customs.CustomInput.TYPE.TEXT
import com.google.android.gms.maps.model.LatLng
import java.io.File


class FormUploaderActivity : AppCompatActivity() {

    private val latLangPosition by lazy {
        intent.parcelable(EXTRA_LATLNG) ?: LatLng(0.0, 0.0)
    }

    private lateinit var binding: ActivityFormUploaderBinding
    private lateinit var viewModel: FormUploaderViewModel
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormUploaderBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[FormUploaderViewModel::class.java]
        setContentView(binding.root)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        setupView()
        Log.d(TAG, "location $latLangPosition")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Permission not granted",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupView() {
        val descWatcher = object : CustomInput.InputListener {
            override fun afterTextChanged(input: String) {
                setupButtonStates()
            }

        }
        with(binding) {
            ivForm.setOnClickListener {
                openDialogSelector()
            }
            edtDescription.apply {
                setHint(getString(R.string.hint_description))
                setInpuType(TEXT)
                setLines(3)
                setListener(descWatcher)
            }
            btnUpload.setOnClickListener {
                uploadContent()
            }
            viewModel.loadingStates.observe(this@FormUploaderActivity) {
                loading.loadingContainer.showView(it)
                Log.d(TAG, "loading $it")
            }
        }
    }

    private fun uploadContent() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val description = binding.edtDescription.getText()
            val request = ContentUploaderRequest(
                latLangPosition.latitude, latLangPosition.longitude, file, description
            )
            viewModel.uploadContent(request)
            viewModel.basicResponse.observe(this) {
                binding.root.snackbar(it.message)
                if (!it.error) Handler(Looper.getMainLooper()).postDelayed(
                    { onBackPressedDispatcher.onBackPressed() },
                    1000
                )
            }
        }
    }

    private fun openDialogSelector() {
        val option = arrayOf(
            getString(R.string.gallery),
            getString(R.string.camera),
            getString(R.string.cancel)
        )
        val window: AlertDialog.Builder = AlertDialog.Builder(this)
        window.setTitle(getString(R.string.select_image_source))
        window.setItems(option) { _, which ->
            when (which) {
                0 -> openGallery()
                1 -> openCamera()
                else -> binding.root.snackbar("Canceled")
            }
        }

        window.show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.flowerencee9.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val result = BitmapFactory.decodeFile(myFile.path)
            getFile = myFile
            binding.ivForm.setImageBitmap(result)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding.ivForm.setImageURI(selectedImg)
            setupButtonStates()
        }
    }

    private fun setupButtonStates() {
        with(binding) {
            btnUpload.isEnabled = getFile != null && edtDescription.isValid()
        }
    }

    companion object {
        private val TAG = FormUploaderActivity::class.java.simpleName
        fun newIntent(context: Context, position: LatLng) =
            Intent(context, FormUploaderActivity::class.java).putExtra(
                EXTRA_LATLNG, position
            )

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val EXTRA_LATLNG = "EXTRA_LATLNG"
    }
}