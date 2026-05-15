package com.example.nallanudi.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    // ---------------- WORDS ----------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: WordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<WordEntity>)

    @Query("SELECT * FROM words")
    fun getAllWords(): Flow<List<WordEntity>>

    @Query("""
        SELECT * FROM words
        WHERE LOWER(word) LIKE LOWER('%' || :query || '%')
    """)
    fun searchWords(query: String): Flow<List<WordEntity>>

    @Query("""
        SELECT * FROM words
        WHERE word = :word
        LIMIT 1
    """)
    suspend fun getWord(word: String): WordEntity?

    @Query("""
        SELECT * FROM words
        WHERE LOWER(category) = LOWER(:category)
    """)
    fun getWordsByCategory(category: String): Flow<List<WordEntity>>

    @Query("SELECT COUNT(*) FROM words")
    suspend fun getWordCount(): Int


    // ---------------- SAVED WORDS ----------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWord(word: SavedWordEntity)

    @Query("SELECT * FROM saved_words")
    fun getSavedWords(): Flow<List<SavedWordEntity>>

    @Query("DELETE FROM saved_words WHERE word = :word")
    suspend fun deleteWord(word: String)

    @Query("SELECT * FROM saved_words WHERE word = :word LIMIT 1")
    suspend fun isWordSaved(word: String): SavedWordEntity?
    @Query("""
    SELECT * FROM words
    WHERE LOWER(word) LIKE '%' || LOWER(:query) || '%'
""")
    suspend fun searchWordsOnce(query: String): List<WordEntity>

    @Query("SELECT * FROM saved_words WHERE word = :word LIMIT 1")
    fun isWordSavedFlow(word: String): Flow<SavedWordEntity?>
    fun getAllWordsFlow()
}