package io.github.occultus73.firebase.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.github.occultus73.firebase.data.FirebaseRepository
import io.github.occultus73.firebase.data.model.Upload

class UploadedViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository: FirebaseRepository = FirebaseRepository.getInstance(application)

    //Local database store of Uploads
    val liveUploads: LiveData<List<Upload>> = firebaseRepository.getAllLocalUploads()

    //remove upload from firebase
    fun delete(upload: Upload) = firebaseRepository.delete(upload)

}