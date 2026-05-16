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
import java.io.IOException

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NallaNudiTheme {
                AppNavGraph(navController)
            }
        }

        // DB initialization code here
    }

    // 👇 ADD HERE (OUTSIDE onCreate)
    private fun loadWordsFromJson(context: Context): List<WordEntity> {

        return try {

            val jsonString = context.assets
                .open("words.json")
                .bufferedReader()
                .use { it.readText() }

            val listType = object : com.google.gson.reflect.TypeToken<List<WordJson>>() {}.type

            val jsonList: List<WordJson> =
                com.google.gson.Gson().fromJson(jsonString, listType)

            jsonList.map {
                WordEntity(
                    word = it.word,
                    meaning = it.meaning,
                    kannadaMeaning = it.kannadaMeaning,
                    category = it.category
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}