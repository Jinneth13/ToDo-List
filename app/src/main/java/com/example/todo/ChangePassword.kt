package com.example.todo

import android.content.Intent
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lint.kotlin.metadata.Visibility
import com.example.todo.R

class ChangePassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = this
            MaterialTheme(colorScheme = lightColorScheme()) {
                ChangePasswordScreen(
                    onBackClick = { finish() },
                    onContinueClick = {
                        val intent = Intent(context, Profile::class.java)
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun ChangePasswordScreen(
    onBackClick: () -> Unit = {},
    onContinueClick: (/*String, String, String*/) -> Unit = {/* _, _, _ -> */}
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Barra superior
        TopBarChangePassword(onBackClick = onBackClick)

        Spacer(modifier = Modifier.height(24.dp))

        // Imagen principal
        Image(
            painter = painterResource(id = R.drawable.iconop),
            contentDescription = stringResource(R.string.my_image_description),
            modifier = Modifier
                .size(230.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo contraseña actual
        PasswordTextField(
            value = currentPassword,
            onValueChange = { currentPassword = it },
            label = stringResource(R.string.enter_current_password),
            showPassword = showCurrentPassword,
            onVisibilityToggle = { showCurrentPassword = !showCurrentPassword }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo nueva contraseña
        PasswordTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = stringResource(R.string.enter_new_password),
            showPassword = showNewPassword,
            onVisibilityToggle = { showNewPassword = !showNewPassword }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirmar contraseña
        PasswordTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = stringResource(R.string.enter_confirm_password),
            showPassword = showConfirmPassword,
            onVisibilityToggle = { showConfirmPassword = !showConfirmPassword }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón continuar
        Button(
            onClick = { onContinueClick(/*currentPassword, newPassword, confirmPassword*/) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF51E4FF))
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
fun TopBarChangePassword(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF51E4FF)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.change_password_ToDo),
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

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    showPassword: Boolean,
    onVisibilityToggle: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        placeholder = { Text(text = label) },
        singleLine = true,
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(onClick = onVisibilityToggle) {
                Icon(
                    imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (showPassword)
                        stringResource(R.string.txtEmailHidden)
                    else
                        stringResource(R.string.txtPasswordHidden)
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF51E4FF),
            unfocusedBorderColor = Color.Gray,
            cursorColor = Color(0xFF51E4FF)
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChangePasswordScreenPreview() {
    ChangePasswordScreen()
}