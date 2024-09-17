package UiComponents

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.onClick
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun clickableText(text: String, size: TextUnit = 20.sp, action: () -> Unit) = TextButton(
    onClick = {action()}
){
    robotoText(text, size = size)
}

@Composable
fun robotoText(text: String, size: TextUnit = 20.sp, color: Color = Color.Black) = Text(
    text,
    fontSize = size,
    fontFamily = Fonts.robotoClassic,
    color = color
)