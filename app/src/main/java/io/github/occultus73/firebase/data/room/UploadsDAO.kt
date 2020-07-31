package io.github.occultus73.firebase.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.occultus73.firebase.data.model.Upload

@Dao
interface UploadsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUploads(uploads: List<Upload>)

    @Query("SELECT * FROM uploads")
    fun getAllUploads() : LiveData<List<Upload>>

    @Delete
    fun deleteUpload(upload: Upload)

    @Query("DELETE FROM uploads")
    fun resetRoom()

}