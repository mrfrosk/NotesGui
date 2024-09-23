package UiComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun clickableText(text: String, size: TextUnit = 20.sp, action: () -> Unit) = TextButton(
    onClick = { action() }
) {
    robotoText(text, size = size)
}

@Composable
fun robotoText(text: String, size: TextUnit = 20.sp, color: Color = Color.Black) = Text(
    text,
    fontSize = size,
    fontFamily = Fonts.robotoClassic,
    color = color
)

@Composable
fun datePicker(dateState: MutableState<kotlinx.datetime.LocalDate>) {
    val currentYear = Clock.System.now().toLocalDateTime(TimeZone.UTC).year
    var dayExpanded by remember { mutableStateOf(false) }
    var monthExpanded by remember { mutableStateOf(false) }
    var yearExpanded by remember { mutableStateOf(false) }
    var year by remember { mutableStateOf(currentYear.toString()) }
    var month by remember { mutableStateOf(Month.January.monthName) }
    var day by remember { mutableStateOf("1") }
    val dayToDate = if (day.toInt() < 10) "0$day" else day
    val monthDays = Month.entries.find { it.monthName == month }!!.days
    dateState.value = LocalDate.parse("$year-${Month.getByName(month)?.stringNumber}-$dayToDate")

    Column {
        Row {
            Column {
                robotoText("День")
                TextButton({ dayExpanded = !dayExpanded }) {
                    robotoText(day)
                }
            }
            Column {
                robotoText("Месяц")
                TextButton({ monthExpanded = !monthExpanded }) {
                    robotoText(month)
                }
            }
            Column {
                robotoText("Год")
                TextButton({ yearExpanded = !yearExpanded }) {
                    robotoText(year)
                }
            }
        }

        Row {
            DropdownMenu(dayExpanded, onDismissRequest = {
                dayExpanded = false
            }) {
                for (dayItem in 1..monthDays) {
                    DropdownMenuItem(onClick = {
                        day = "$dayItem"
                        dayExpanded = false
                    }, content = { Text("$dayItem") })
                }
            }

            DropdownMenu(monthExpanded, onDismissRequest = {
                monthExpanded = false
            }) {
                for (monthItem in Month.entries.map { it.monthName }) {
                    DropdownMenuItem(onClick = {
                        month = monthItem
                        monthExpanded = false
                    }, content = { Text(monthItem) })
                }
            }
            DropdownMenu(yearExpanded, onDismissRequest = {
                yearExpanded = false
            }) {
                for (yearItem in currentYear..2100) {
                    DropdownMenuItem(onClick = { year = yearItem.toString() }, content = { Text(yearItem.toString()) })
                }
            }
        }

    }

}


