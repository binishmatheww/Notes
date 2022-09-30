package com.binishmatheww.notes.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(@PrimaryKey val id : Long, val title : String, val description : String, val colorId : Int, val createdAt : Long)