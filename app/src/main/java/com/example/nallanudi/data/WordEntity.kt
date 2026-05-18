package com.example.nallanudi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val word: String,
    val meaning: String,
    val kannadaMeaning: String,
    val category: String,

    val isSaved: Boolean = false,

    // 🧠 NEW LEARNING SYSTEM
    val learned: Boolean = false,
    val practiceCount: Int = 0
)