package data.dto

import data.utils.UUIDSerializer

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime
import java.util.UUID

@Serializable
data class NotificationDto(
    val text: String,
    val date: LocalDateTime,
    val repeat: Boolean,
    @Serializable(with = UUIDSerializer::class)
    val noteId: UUID
)
