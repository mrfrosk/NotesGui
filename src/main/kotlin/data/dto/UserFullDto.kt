package data.dto

import kotlinx.serialization.Serializable

@Serializable
data class  UserFullDto(
    var email: String="",
    var name: String="",
    var surname: String="",
    var patronymic: String="",
    var password: String=""
)
