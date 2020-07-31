package io.github.occultus73.firebase.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "uploads")
data class Upload (
    @PrimaryKey
    @ColumnInfo(name = "student_id")
    var mID: String = "",

    @ColumnInfo(name = "student_name")
    var mName: String = "",

    @ColumnInfo(name = "student_image")
    var mImageUrl: String = ""
)