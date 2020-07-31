package io.github.occultus73.firebase.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import io.github.occultus73.firebase.R
import io.github.occultus73.firebase.data.model.Upload
import kotlinx.android.synthetic.main.image_item.view.*
import kotlin.random.Random.Default.nextInt

const val CAN_DELETE = 4321

private val DIFF_CALLBACK: DiffUtil.ItemCallback<Upload> = object :
    DiffUtil.ItemCallback<Upload>() {
    override fun areItemsTheSame(oldItem: Upload, newItem: Upload): Boolean {
        return oldItem.mID == newItem.mID
    }

    override fun areContentsTheSame(oldItem: Upload, newItem: Upload): Boolean {
        return (oldItem.mImageUrl == newItem.mImageUrl) && (oldItem.mName == newItem.mName)
    }

}

class UploadAdapter : ListAdapter<Upload, UploadAdapter.UploadViewHolder>(
    DIFF_CALLBACK
) {
    private val emailUsername by lazy { FirebaseAuth.getInstance().currentUser?.email.toString() }

    inner class UploadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            with(itemView) {
                val listItemToBind: Upload = getItem(position)

                // Set the image username
                text_view_name.text = listItemToBind.mName

                // Check if current user owns this post
                if (listItemToBind.mName == emailUsername) itemView.id = CAN_DELETE

                // Set the image itself
                Picasso.get()
                    .load(listItemToBind.mImageUrl)
                    .fit()
                    .centerCrop()
                    .into(image_view_upload)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadViewHolder {
        val rvItem: View =
            LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return UploadViewHolder(rvItem)
    }

    override fun onBindViewHolder(holder: UploadViewHolder, position: Int) = holder.bind(position)

    //setup swipe functionality
    val itemTouchHelper =
        object : ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, LEFT or RIGHT) {

            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return if (viewHolder.itemView.id == CAN_DELETE) {
                    super.getSwipeDirs(recyclerView, viewHolder)
                } else {
                    //disable swipe for posts not made by this account.
                    0
                }

            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                itemToDelete.value = getItem(position)
            }
        }) {}

    val itemToDelete: MutableLiveData<Upload> = MutableLiveData()
}