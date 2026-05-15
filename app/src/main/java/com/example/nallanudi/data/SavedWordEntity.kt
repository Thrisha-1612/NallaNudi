package com.example.nallanudi.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nallanudi.data.SavedWordEntity

@Entity(tableName = "saved_words")
data class SavedWordEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val word: String,

    val meaning: String,

    val kannadaMeaning: String,

    val category: String
)