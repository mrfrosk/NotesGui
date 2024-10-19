package data.dto

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object Session {
    var accessToken: String? = null
    var refreshToken: String? = null
    var email: String? = null

    @OptIn(InternalAPI::class)
    suspend fun checkAccessToken() {
        val client = HttpClient(CIO)
        val serverAddress = "http://localhost:8080/api"
        val accessToken = client.post("$serverAddress/auth/request-access-token".encodeURLPath()) {
            body = Json.encodeToString(mapOf("token" to refreshToken, "email" to email))
        }
        Session.accessToken = Json.decodeFromString(accessToken.bodyAsText())

    }
}