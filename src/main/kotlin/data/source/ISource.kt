package data.source

import data.dto.NoteDto
import data.dto.UserInfoDto
import java.util.*

interface ISource {

    suspend fun getNote(title: String): NoteDto

    suspend fun getNotes(userId: UUID): List<NoteDto>

    suspend fun getUsers(): List<UserInfoDto>

    suspend fun authUser(email: String, password: String): Boolean

    suspend fun createNote(userId: UUID, title: String, text: String)

    suspend fun updateNote(oldTitle: String, newTitle: String, text: String)

    suspend fun deleteNote(title: String)
}