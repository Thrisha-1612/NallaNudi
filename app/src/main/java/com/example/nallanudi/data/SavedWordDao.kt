package com.example.nallanudi.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedWordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWord(word: SavedWordEntity)

    @Query("SELECT * FROM saved_words")
    suspend fun getSavedWords(): List<SavedWordEntity>

    @Query("DELETE FROM saved_words WHERE word = :word")
    suspend fun deleteWord(word: String)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_words WHERE word = :word)")
    suspend fun isWordSaved(word: String): Boolean
}