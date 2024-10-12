package data.source

import data.dto.NoteDto
import data.dto.NotificationDto
import java.util.*

interface INoteSource {

    suspend fun createNote(userId: UUID, title: String, text: String)

    suspend fun getNote(title: String): NoteDto

    suspend fun getNotes(userId: UUID): Set<NoteDto>

    suspend fun deleteNote(title: String)

    suspend fun updateNote(title: String, text: String)

    suspend fun createNotification(notificationDto: NotificationDto)

    suspend fun getNotifications(noteId: UUID): Set<NotificationDto>

    suspend fun deleteNotification(notificationId: UUID)

    suspend fun deleteNotifications(noteId: UUID)
}