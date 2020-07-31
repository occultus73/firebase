package io.github.occultus73.firebase.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.occultus73.firebase.R
import io.github.occultus73.firebase.data.model.Upload
import io.github.occultus73.firebase.ui.adapter.UploadAdapter
import io.github.occultus73.firebase.viewmodel.UploadedViewModel
import kotlinx.android.synthetic.main.activity_images.*


class ImagesActivity : AppCompatActivity() {
    private val viewModel: UploadedViewModel by lazy {
        ViewModelProvider(this).get(UploadedViewModel::class.java)
    }

    private val recyclerViewAdapter =
        UploadAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)

        // Set view model observers
        viewModel.liveUploads.observe(this, Observer { loadUploads(it) })

        // Set recycler view adapter observers
        recyclerViewAdapter.itemToDelete.observe(this, Observer { deleteItem(it) })

        // Setup the recycler view
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = recyclerViewAdapter
        recyclerViewAdapter.itemTouchHelper.attachToRecyclerView(recycler_view)
    }

    // liveUploads Observer
    private fun loadUploads(uploads: List<Upload>){
        recyclerViewAdapter.submitList(uploads)
    }

    // itemToDelete Observer
    private fun deleteItem(upload: Upload) = viewModel.delete(upload)
}