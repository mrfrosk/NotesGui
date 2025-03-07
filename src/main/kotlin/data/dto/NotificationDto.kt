package data.dto

import data.utils.UUIDSerializer

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import java.util.UUID

@Serializable
data class NotificationDto(
    @SerialName("text")
    val text: String,
    @SerialName("date")
    val date: LocalDateTime,
    @SerialName("repeat")
    val repeat: Boolean,
    @SerialName("id")
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    @SerialName("note-id")
    @Serializable(with = UUIDSerializer::class)
    val noteId: UUID
)
