package data.dto

import data.utils.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class NewNoteDto(
    @SerialName("title")
    val title: String,
    @SerialName("text")
    val text: String,
    @SerialName("user-id")
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
)
