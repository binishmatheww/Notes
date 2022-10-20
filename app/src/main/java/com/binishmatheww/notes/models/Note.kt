package com.binishmatheww.notes.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.JsonObject


@Entity(tableName = "notes")
data class Note(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String,
    val colorId: Int,
    val modifiedAt: Long
) {

    companion object {

        fun fromString( string: String ) = Gson().fromJson(string, Note::class.java)

    }

    fun toJsonObject() = JsonObject().apply {
        addProperty("id",id)
        addProperty("title",title)
        addProperty("description",description)
        addProperty("colorId",colorId)
        addProperty("modifiedAt",modifiedAt)
    }

    override fun toString() = toJsonObject().toString()

}