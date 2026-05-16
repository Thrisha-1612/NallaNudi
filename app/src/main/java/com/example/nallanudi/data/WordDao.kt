package com.example.nallanudi.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    // 🔹 Insert words (used for JSON load)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<WordEntity>)

    // 🔹 Get all words (HOME SCREEN)
    @Query("SELECT * FROM words")
    fun getAllWords(): Flow<List<WordEntity>>

    // 🔹 Get single word (DETAIL SCREEN)
    @Query("SELECT * FROM words WHERE word = :word LIMIT 1")
    suspend fun getWord(word: String): WordEntity?

    // 🔹 Search words (SEARCH SCREEN)
    @Query("SELECT * FROM words WHERE word LIKE '%' || :query || '%'")
    fun searchWords(query: String): Flow<List<WordEntity>>

    // 🔹 Filter by category (HOME FILTER)
    @Query("SELECT * FROM words WHERE category = :category")
    fun getWordsByCategory(category: String): Flow<List<WordEntity>>

    // 🔹 Saved words (FLASHCARDS + SAVED LIST)
    @Query("SELECT * FROM words WHERE isSaved = 1")
    fun getSavedWords(): Flow<List<WordEntity>>

    // 🔹 SAVE word
    @Query("UPDATE words SET isSaved = 1 WHERE word = :word")
    suspend fun saveWord(word: String)

    // 🔹 UNSAVE word
    @Query("UPDATE words SET isSaved = 0 WHERE word = :word")
    suspend fun unsaveWord(word: String)
}