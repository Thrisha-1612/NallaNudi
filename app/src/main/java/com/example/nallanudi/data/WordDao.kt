package com.example.nallanudi.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    // 🔹 Insert words from JSON
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<WordEntity>)

    // 🔹 Get all words
    @Query("SELECT * FROM words")
    fun getAllWords(): Flow<List<WordEntity>>

    // 🔹 Get single word
    @Query("SELECT * FROM words WHERE word = :word LIMIT 1")
    suspend fun getWord(word: String): WordEntity?

    // 🔹 Smart search
    @Query("""
        SELECT * FROM words
        WHERE word LIKE '%' || :query || '%'
        OR meaning LIKE '%' || :query || '%'
        OR kannadaMeaning LIKE '%' || :query || '%'
        OR category LIKE '%' || :query || '%'
        ORDER BY word ASC
    """)
    fun searchWords(query: String): Flow<List<WordEntity>>

    // 🔹 Filter by category
    @Query("""
        SELECT * FROM words
        WHERE TRIM(LOWER(category)) = TRIM(LOWER(:category))
    """)
    fun getWordsByCategory(category: String): Flow<List<WordEntity>>

    // 🔹 Saved words
    @Query("SELECT * FROM words WHERE isSaved = 1")
    fun getSavedWords(): Flow<List<WordEntity>>

    // 🔹 Save word
    @Query("UPDATE words SET isSaved = 1 WHERE word = :word")
    suspend fun saveWord(word: String)

    // 🔹 Unsave word
    @Query("UPDATE words SET isSaved = 0 WHERE word = :word")
    suspend fun unsaveWord(word: String)
}