package io.github.occultus73.firebase.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import io.github.occultus73.firebase.data.FirebaseRepository
import io.github.occultus73.firebase.data.model.Upload

class UploaderViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository: FirebaseRepository = FirebaseRepository.getInstance(application)

    //The Upload Image File task
    private lateinit var mUploadTask: Task<Uri>

    //Toast message channel
    val liveToast: MutableLiveData<String> = firebaseRepository.liveToast.also { it.value = "" }
    //Progress update channel
    val liveProgress: MutableLiveData<Int> = firebaseRepository.liveProgress

    fun uploadFile(mImageUri: Uri, requestedFileName: String) {
        if (this::mUploadTask.isInitialized && !mUploadTask.isComplete){
            liveToast.value = "an upload is already in progress..."
        }
        else {
            mUploadTask = firebaseRepository.uploadImageFileToFirebaseStorage(mImageUri, requestedFileName)
        }
    }


}