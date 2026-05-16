package com.example.nallanudi.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    // INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: WordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<WordEntity>)

    @Delete
    suspend fun deleteWord(word: WordEntity)

    @Query("SELECT * FROM words")
    fun getAllWordsFlow(): Flow<List<WordEntity>>

    @Query("SELECT * FROM words")
    suspend fun getAllWords(): List<WordEntity>

    @Query("SELECT COUNT(*) FROM words")
    suspend fun getWordCount(): Int

    @Query("SELECT * FROM words WHERE word LIKE '%' || :query || '%'")
    suspend fun searchWords(query: String): List<WordEntity>

    @Query("SELECT * FROM words WHERE LOWER(category) = LOWER(:category)")
    fun getWordsByCategory(category: String): Flow<List<WordEntity>>

    @Query("SELECT * FROM words WHERE word = :word LIMIT 1")
    suspend fun getWord(word: String): WordEntity?

    @Query("SELECT * FROM words WHERE isSaved = 1")
    fun getSavedWords(): Flow<List<WordEntity>>

    @Query("SELECT EXISTS(SELECT * FROM words WHERE word = :word AND isSaved = 1)")
    suspend fun isWordSaved(word: String): Boolean

    @Query("UPDATE words SET isSaved = 1 WHERE word = :word")
    suspend fun saveWord(word: String)

    @Query("UPDATE words SET isSaved = 0 WHERE word = :word")
    suspend fun unsaveWord(word: String)

    @Query("SELECT * FROM words WHERE isSaved = 1")
    fun getSavedWords(): Flow<List<WordEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM words WHERE word = :word AND isSaved = 1)")
    suspend fun isWordSaved(word: String): Boolean
}