package com.example.studentform

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentFormScreen()
        }
    }
}

val BackgroundColor = Color(0xFF0F0F1A)
val CardColor = Color(0xFF1C1C2E)
val AccentColor = Color(0xFF7C5CBF)
val AccentLight = Color(0xFFB39DDB)
val TextPrimary = Color(0xFFF0EEFF)
val TextSecondary = Color(0xFF9E9EB8)
val InputBackground = Color(0xFF252538)
val BorderColor = Color(0xFF3A3A5C)

@Composable
fun StudentFormScreen() {
    val context = LocalContext.current

    var nameState by remember { mutableStateOf("") }
    var surnameState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
    var dateState by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("") }
    var isAgreed by remember { mutableStateOf(false) }

    val directions = listOf("Android", "iOS", "Web")

    val calendar = Calendar.getInstance()
    val datePicker = DatePickerDialog(
        context,
        { _, year, month, day ->
            dateState = "%02d/%02d/%04d".format(day, month + 1, year)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "სტუდენტის ფორმა",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = AccentLight,
            letterSpacing = 1.sp
        )

        Text(
            text = "შეავსეთ ყველა ველი გაგრძელებამდე",
            fontSize = 13.sp,
            color = TextSecondary
        )

        FormCard {
            FormLabel("სახელი")
            StyledTextField(
                value = nameState,
                onValueChange = { nameState = it },
                placeholder = "შეიყვანეთ სახელი"
            )

            Spacer(modifier = Modifier.height(16.dp))

            FormLabel("გვარი")
            StyledTextField(
                value = surnameState,
                onValueChange = { surnameState = it },
                placeholder = "შეიყვანეთ გვარი"
            )

            Spacer(modifier = Modifier.height(16.dp))

            FormLabel("ელ-ფოსტა")
            StyledTextField(
                value = emailState,
                onValueChange = { emailState = it },
                placeholder = "example@mail.com"
            )
        }

        FormCard {
            FormLabel("დაბადების თარიღი")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(InputBackground)
                    .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                    .clickable { datePicker.show() }
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = if (dateState.isEmpty()) "DD/MM/YYYY" else dateState,
                    color = if (dateState.isEmpty()) TextSecondary else TextPrimary,
                    fontSize = 15.sp
                )
            }
        }

        FormCard {
            FormLabel("ფავორიტი მიმართულება")
            Spacer(modifier = Modifier.height(8.dp))
            directions.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (selectedOption == option) AccentColor.copy(alpha = 0.15f)
                            else Color.Transparent
                        )
                        .clickable { selectedOption = option }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    RadioButton(
                        selected = selectedOption == option,
                        onClick = { selectedOption = option },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = AccentLight,
                            unselectedColor = TextSecondary
                        )
                    )
                    Text(
                        text = option,
                        color = if (selectedOption == option) AccentLight else TextPrimary,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }

        FormCard {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "ვეთანხმები წესებს და პირობებს",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = isAgreed,
                    onCheckedChange = { isAgreed = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = AccentColor,
                        uncheckedThumbColor = TextSecondary,
                        uncheckedTrackColor = InputBackground
                    )
                )
            }
        }

        Button(
            onClick = {
                val allFilled = nameState.isNotBlank()
                        && surnameState.isNotBlank()
                        && emailState.isNotBlank()
                        && dateState.isNotBlank()
                val optionPicked = selectedOption.isNotBlank()

                if (!allFilled || !optionPicked || !isAgreed) {
                    Toast.makeText(context, "შეავსეთ ყველა ველი!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "მონაცემები გაიგზავნა!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentColor)
        ) {
            Text(
                text = "გაგზავნა",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun FormCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardColor)
            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
            .padding(20.dp),
        content = content
    )
}

@Composable
fun FormLabel(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = TextSecondary,
        letterSpacing = 0.8.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun StyledTextField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = placeholder, color = TextSecondary, fontSize = 14.sp)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedContainerColor = InputBackground,
            unfocusedContainerColor = InputBackground,
            focusedBorderColor = AccentColor,
            unfocusedBorderColor = BorderColor,
            cursorColor = AccentLight
        ),
        singleLine = true
    )
}
