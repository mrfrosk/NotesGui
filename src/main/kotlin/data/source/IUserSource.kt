package data.source

import data.dto.UserFullDto
import data.dto.UserInfoDto

interface IUserSource {

    suspend fun getUser(email: String): UserInfoDto

    suspend fun getUsers(): List<String>

    suspend fun createUser(fullDto: UserFullDto)

    suspend fun authUser(email: String, password: String): Boolean

}