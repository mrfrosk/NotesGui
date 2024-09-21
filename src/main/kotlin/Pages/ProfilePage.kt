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
import data.source.SpringDataSource
import data.source.ISource
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ProfilePage(private val navigateState: MutableState<GlobalPages>, val userState: MutableState<String?>) {
    private val source: ISource = SpringDataSource()
    private val padding = 5.dp

    @Composable
    fun draw() {
        val scope = rememberCoroutineScope()
        val source = SpringDataSource()
        Column {
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            OutlinedTextField(
                email,
                onValueChange = { email = it },
                placeholder = { robotoText("email") }
            )
            OutlinedTextField(
                password,
                onValueChange = { password = it },
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
                            if (authStatus){
                                userState.value = email
                                navigateState.value = GlobalPages.Note
                            }
                        }
                    }
                ) {
                    robotoText("Войти", color = Color.White)
                }
                Button(
                    {},
                    modifier = Modifier.absolutePadding(left = padding)
                ) {
                    robotoText("зарегистрироваться", color = Color.White)
                }
            }

        }
    }

}