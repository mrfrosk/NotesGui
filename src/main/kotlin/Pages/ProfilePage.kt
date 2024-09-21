package Pages

import Pages.enums.GlobalPages
import UiComponents.robotoText
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import data.dto.UserFullDto
import data.source.SpringDataSource
import data.source.ISource
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ProfilePage(private val navigateState: MutableState<GlobalPages>, val userState: MutableState<String?>) {
    private val padding = 5.dp
    private val user = UserFullDto()

    @Composable
    fun draw() {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var createUser by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val source = SpringDataSource()

        Column {


            OutlinedTextField(
                email,
                onValueChange = {
                    email = it
                    user.email = email
                },
                placeholder = { robotoText("email") }
            )
            if (createUser) {
                createUser()
            }
            OutlinedTextField(
                password,
                onValueChange = {
                    password = it
                    user.password = password
                },
                placeholder = { robotoText("Пароль") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.padding(top = padding)
            )

            Row(Modifier.padding(top = padding)) {
                Button(
                    {
                        scope.launch {
                            val authStatus = source.authUser(email, password)
                            if (authStatus) {
                                userState.value = email
                                navigateState.value = GlobalPages.Note
                            }
                        }
                    }
                ) {
                    robotoText("Войти", color = Color.White)
                }
                Button(
                    {
                        if (createUser) {
                            scope.launch {
                                source.createUser(user)
                                userState.value = email
                                navigateState.value = GlobalPages.Note
                            }
                        }
                        createUser = !createUser
                    },
                    modifier = Modifier.absolutePadding(left = padding)
                ) {
                    robotoText("зарегистрироваться", color = Color.White)
                }
            }

        }
    }

    @Composable
    fun createUser() {
        var name by remember { mutableStateOf("") }
        var surname by remember { mutableStateOf("") }
        var patronymic by remember { mutableStateOf("") }
        Column {
            OutlinedTextField(
                name,
                onValueChange = {
                    name = it
                    user.name = name
                },
                placeholder = { robotoText("имя") },
                modifier = Modifier.padding(top = padding)
            )
            OutlinedTextField(
                surname,
                onValueChange = {
                    surname = it
                    user.surname = surname
                },
                placeholder = { robotoText("фамилия") },
                modifier = Modifier.padding(top = padding)
            )
            OutlinedTextField(
                patronymic,
                onValueChange = {
                    patronymic = it
                    user.patronymic = patronymic
                },
                placeholder = { robotoText("отчество") },
                modifier = Modifier.padding(top = padding)
            )
        }
    }

}