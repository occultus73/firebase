package io.github.occultus73.firebase.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import io.github.occultus73.firebase.R
import io.github.occultus73.firebase.utils.Constants
import io.github.occultus73.firebase.viewmodel.UploaderViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.image_item.*


class HomeActivity : AppCompatActivity() {
    private val viewModel: UploaderViewModel by lazy {
        ViewModelProvider(this).get(UploaderViewModel::class.java)
    }

    //Firebase Email-Username
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val emailUsername by lazy { firebaseAuth.currentUser?.email.toString() }

    //location of the selected local image file
    private lateinit var mImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Display email-username
        file_name_edit_text.text = emailUsername

        // Set view model observers
        viewModel.liveToast.observe(this, Observer { makeToast(it) })
        viewModel.liveProgress.observe(this, Observer { displayProgress(it) })

        // Set button listeners
        choose_image_button.setOnClickListener { openFileChooser() }
        upload_button.setOnClickListener { uploadSelectedImage() }
        text_view_show_uploads.setOnClickListener { openImagesActivity() }
        text_view_logout.setOnClickListener { logout() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Response to openFileChooser()
        if (requestCode == Constants.PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            data?.data?.let {
                mImageUri = it
                Picasso.get().load(mImageUri).into(image_view)
            }
        }
    }

    // Action of the "Choose File" button
    private fun openFileChooser() {
        val intent: Intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_PICK
        }
        startActivityForResult(intent, Constants.PICK_IMAGE_REQUEST)
    }

    // Action of the "Upload" button
    private fun uploadSelectedImage() {
        if (this::mImageUri.isInitialized)
            viewModel.uploadFile(mImageUri, emailUsername)
        else makeToast("Image required.")
    }

    // Action of the "logout" button
    private fun logout() {
        firebaseAuth.signOut()
        val returnToLoginActivityIntent = Intent(this, LoginActivity::class.java).apply {
            flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(returnToLoginActivityIntent)
    }

    // Action of the "Show Uploads" button
    private fun openImagesActivity() {
        val openImagesActivityIntent = Intent(this, ImagesActivity::class.java)
        startActivity(openImagesActivityIntent)
    }

    // liveToast Observer
    private fun makeToast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    // liveProgress Observer
    private fun displayProgress(progress: Int) {
        progress_bar.progress = progress
    }
}