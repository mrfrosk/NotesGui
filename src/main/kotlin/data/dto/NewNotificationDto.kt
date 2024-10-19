package data.dto

import data.utils.UUIDSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class NewNotificationDto(
    @SerialName("text")
    val text: String,
    @SerialName("date")
    val date: LocalDateTime,
    @SerialName("repeat")
    val repeat: Boolean,
    @SerialName("note-id")
    @Serializable(with = UUIDSerializer::class)
    val noteId: UUID
)
