package com.example.nallanudi

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.nallanudi.data.DatabaseInstance
import com.example.nallanudi.data.WordEntity
import com.example.nallanudi.data.WordJson
import com.example.nallanudi.ui.theme.NallaNudiTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            NallaNudiTheme {
                AppNavGraph(navController)
            }
        }

        // ✅ ADD DATABASE INITIALIZATION HERE
        lifecycleScope.launch(Dispatchers.IO) {

            val db = DatabaseInstance.getDatabase(applicationContext)
            val dao = db.wordDao()

            val count = dao.getAllWords().first().size

            println("TOTAL WORDS = $count")

            if (count == 0) {

                val words = loadWordsFromJson(applicationContext)

                println("JSON WORDS = ${words.size}")

                dao.insertWords(words)

                println("INSERT COMPLETED")
            }

            val sample = dao.getAllWords().first().firstOrNull()

            println("SAMPLE WORD = ${sample?.word}")
            println("SAMPLE CATEGORY = ${sample?.category}")
        }
    }

    // ✅ ADD THIS BELOW onCreate (INSIDE CLASS BUT OUTSIDE onCreate)

    private fun loadWordsFromJson(context: Context): List<WordEntity> {

        return try {

            val jsonString = context.assets
                .open("words.json")
                .bufferedReader()
                .use { it.readText() }

            val listType =
                object : TypeToken<List<WordJson>>() {}.type

            val jsonList: List<WordJson> =
                Gson().fromJson(jsonString, listType)

            jsonList.map {

                WordEntity(
                    word = it.word,
                    meaning = it.meaning,
                    kannadaMeaning = it.kannadaMeaning,
                    category = it.category.lowercase()
                )
            }

        } catch (e: Exception) {

            e.printStackTrace()
            emptyList()
        }
    }
}