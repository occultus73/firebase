package io.github.occultus73.firebase.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.occultus73.firebase.data.model.Upload

@Database(version = 1, entities = [Upload::class])
abstract class UploadsRoomDB: RoomDatabase() {
    abstract fun getUploadsDAO(): UploadsDAO
}