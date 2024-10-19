package data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateNoteDto(
    @SerialName("title")
    val title: String,
    @SerialName("text")
    val text: String
)
