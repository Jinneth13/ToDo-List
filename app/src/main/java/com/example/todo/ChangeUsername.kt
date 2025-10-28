package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo.R

class ChangeUsername : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        }
    }
}

@Composable
fun ChangeUsernameScreen(
    onBackClick: () -> Unit = {},
    onContinueClick: (String) -> Unit = {}
) {
    var username by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Barra superior reutilizable
        TopBarChangeUsername(onBackClick = onBackClick)

        Spacer(modifier = Modifier.height(24.dp))

        // Logo principal
        Image(
            painter = painterResource(id = R.drawable.iconop),
            contentDescription = stringResource(R.string.my_image_description),
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo para cambiar nombre de usuario
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            placeholder = { Text(text = stringResource(R.string.enter_username)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF51E4FF),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color(0xFF51E4FF)
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón continuar
        Button(
            onClick = { onContinueClick(username) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF51E4FF)),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = stringResource(R.string.button_continue),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TopBarChangeUsername(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF51E4FF)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.change_username_ToDo),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack/*painter = painterResource(id = R.drawable.ic_arrow_back)*/,
                contentDescription = stringResource(R.string.button_back_text),
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChangeUsernameScreenPreview() {
    ChangeUsernameScreen()
}