package net.husnilkamil.dicodingstory.ui.add

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import net.husnilkamil.dicodingstory.data.request.StoryRequest
import net.husnilkamil.dicodingstory.databinding.ActivityAddStoryBinding
import net.husnilkamil.dicodingstory.ui.ViewModelFactory
import net.husnilkamil.dicodingstory.ui.main.MainActivity
import net.husnilkamil.dicodingstory.utils.createCustomTempFile
import net.husnilkamil.dicodingstory.utils.getToken
import net.husnilkamil.dicodingstory.utils.uriToFile
import net.husnilkamil.dicodingstory.data.Result
import java.io.File
import java.io.IOException

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var currentPhotoPath: String? = null
    private var uploadFile: File? = null
    private val addStoryViewModel: AddStoryViewModel by viewModels { ViewModelFactory(this) }

    var launcherGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImage = result.data!!.data
                binding.ivPhoto.setImageURI(selectedImage)
                try {
                    val myFile = selectedImage?.let { uriToFile(it, this@AddStoryActivity) }
                    uploadFile = myFile
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    var launcherCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val myFile = File(currentPhotoPath!!)
                uploadFile = myFile
                val imageBitmap = BitmapFactory.decodeFile(myFile.path)
                binding.ivPhoto.setImageBitmap(imageBitmap)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)
        binding.progressUpload.visibility = View.GONE
        binding.buttonAdd.setOnClickListener { uploadStory() }
        binding.buttonCamera.setOnClickListener { takePicture() }
        binding.buttonGalery.setOnClickListener { pickImage() }
    }

    private fun pickImage() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih gambar")
        launcherGallery.launch(chooser)
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(this.packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "net.husnilkamil.dicodingstory",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherCamera.launch(intent)
        }

    }

    private fun uploadStory() {

        if (uploadFile != null) {
            val description = binding.edAddDescription.text.toString()

            binding.progressUpload.visibility = View.VISIBLE
            val storyRequest = StoryRequest(getToken(this@AddStoryActivity), description, uploadFile)
            addStoryViewModel.uploadStory(storyRequest).observe(this){ result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressUpload.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.progressUpload.visibility = View.GONE
                            Toast.makeText(this, "Berhasil tambah story", Toast.LENGTH_SHORT).show()
                            val homeIntent = Intent(this@AddStoryActivity, MainActivity::class.java)
                            startActivity(homeIntent)
                        }
                        is Result.Error -> {
                            binding.progressUpload.visibility = View.GONE
                        }
                    }
                }
            }
        } else {
            Toast.makeText(
                this@AddStoryActivity,
                "Silahkan tambahkan/pilih photo terlebih dahulu",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}