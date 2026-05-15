package com.example.nallanudi.data

data class SampleDataItem(
    val word: String,
    val meaning: String,
    val kannadaMeaning: String,
    val category: String
)

object SampleData {

    val words = listOf(
        SampleDataItem("Gravity", "Force that pulls objects", "ಗುರುತ್ವಾಕರ್ಷಣೆ", "Science"),
        SampleDataItem("Atom", "Smallest unit of matter", "ಪರಮಾಣು", "Science"),
        SampleDataItem("Cell", "Basic unit of life", "ಕೋಶ", "Biology"),
        SampleDataItem("Photosynthesis", "Process of food making in plants", "ದ್ಯುತಿಸಂಶ್ಲೇಷಣೆ", "Science"),
        SampleDataItem("Addition", "Math operation", "ಸೇರಿಕೆ", "Math")
    )
}