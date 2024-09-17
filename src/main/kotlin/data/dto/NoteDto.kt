package data.dto

import data.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class NoteDto(
    val title: String,
    val text: String,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID
)
