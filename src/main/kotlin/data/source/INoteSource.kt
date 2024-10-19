package data.source

import data.dto.NewNotificationDto
import data.dto.NoteDto
import data.dto.NotificationDto
import java.util.*

interface INoteSource {

    suspend fun createNote(userId: UUID, title: String, text: String)

    suspend fun getNote(title: String): NoteDto

    suspend fun getNotes(userId: UUID): Set<NoteDto>

    suspend fun deleteNote(title: String)

    suspend fun updateNote(oldTitle: String, title: String, text: String)

    suspend fun createNotification(notificationDto: NewNotificationDto)

    suspend fun getNotifications(noteId: UUID): Set<NotificationDto>

    suspend fun deleteNotification(notificationId: UUID)

    suspend fun deleteNotifications(noteId: UUID)
}