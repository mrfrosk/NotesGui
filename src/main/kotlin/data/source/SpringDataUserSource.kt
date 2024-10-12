package data.source

import data.dto.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import io.ktor.client.engine.cio.*
import io.ktor.util.*
import kotlinx.serialization.encodeToString

class SpringDataUserSource : IUserSource {
    private val client = HttpClient(CIO)
    private val serverAddress = "http://localhost:8080/api"


    override suspend fun getUser(email: String): UserInfoDto {
        Session.checkAccessToken()
        val user = client.get("$serverAddress/users/user/$email".encodeURLPath()) {
            headers.append("Authorization", "Bearer ${Session.accessToken}")
        }

        return Json.decodeFromString<UserInfoDto>(user.bodyAsText())
    }

    override suspend fun getUsers(): List<String> {
        val users = client.get("http://localhost:8080/api/users/all".encodeURLPath())
        return Json.decodeFromString<List<UserInfoDto>>(users.bodyAsText()).map { it.email }
    }

    @OptIn(InternalAPI::class)
    override suspend fun createUser(fullDto: UserFullDto) {
        Session.checkAccessToken()
        val user = UserFullDto(fullDto.email, fullDto.name, fullDto.surname, fullDto.patronymic, fullDto.password)
        val registration = client.post("$serverAddress/users/new".encodeURLPath()) {
            body = Json.encodeToString(user)
        }

        println(registration.status)
        if (Json.decodeFromString<Boolean>(registration.bodyAsText())) {
            authUser(fullDto.email, fullDto.password)
        }

    }


    @OptIn(InternalAPI::class)
    override suspend fun authUser(email: String, password: String): Boolean {
        val response = client.post("$serverAddress/auth/login".encodeURLPath()) {
            body = Json.encodeToString(mapOf("email" to email, "password" to password))
        }.bodyAsText()
        val tokens = Json.decodeFromString<JwtDto>(response)
        Session.accessToken = tokens.accessToken
        Session.refreshToken = tokens.refreshToken
        Session.email = email
        return tokens.accessToken.isNotEmpty()

    }
}