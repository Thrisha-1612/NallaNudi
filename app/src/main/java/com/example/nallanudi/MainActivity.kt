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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            NallaNudiTheme {
                AppNavGraph(navController)
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {

            val db = DatabaseInstance.getDatabase(applicationContext)
            val dao = db.wordDao()

            val count = withContext(Dispatchers.IO) {
                dao.getWordCount()
            }

            if (count == 0) {
                val words = loadWordsFromJson(applicationContext)

                if (words.isNotEmpty()) {
                    dao.insertWords(words)
                }
            }
        }
    }

    private fun loadWordsFromJson(context: Context): List<WordEntity> {

        return try {

            val jsonString = context.assets
                .open("words.json")
                .bufferedReader()
                .use { it.readText() }

            val listType = object : TypeToken<List<WordJson>>() {}.type

            val jsonList: List<WordJson> =
                Gson().fromJson(jsonString, listType)

            jsonList.map {
                WordEntity(
                    word = it.word,
                    meaning = it.meaning,
                    kannadaMeaning = it.kannadaMeaning,
                    category = it.category
                )
            }

        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}