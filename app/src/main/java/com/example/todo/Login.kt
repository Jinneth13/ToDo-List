package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo.ui.theme.*

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(colorScheme = lightColorScheme()) {
                LoginScreen(
                    onLoginClick = { /* lógica de login */ },
                    onSignInClick = { /* ir a registro */ },
                    onForgotPasswordClick = { /* recuperar contraseña */ },
                    onFacebookClick = { /* login con Facebook */ },
                    onGoogleClick = { /* login con Google */ }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onFacebookClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar()
        LoginContent(
            onLoginClick = onLoginClick,
            onSignInClick = onSignInClick,
            onForgotPasswordClick = onForgotPasswordClick,
            onFacebookClick = onFacebookClick,
            onGoogleClick = onGoogleClick
        )
    }
}

@Composable
fun TopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(39.dp)
            .background(BluePrimary)
    )
}

@Composable
fun LoginContent(
    onLoginClick: () -> Unit,
    onSignInClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onGoogleClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.iconop),
            contentDescription = stringResource(id = R.string.my_image_description),
            modifier = Modifier
                .width(200.dp)
                .height(120.dp)
        )

        // Username
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(id = R.string.enter_name_hint)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id = R.string.enter_contraseñap_hint)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )

        // Login button
        Button(
            onClick = onLoginClick,
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
        ) {
            Text(
                text = stringResource(id = R.string.enter_login1),
                color = White
            )
        }

        // Links
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.not_account1),
                style = MaterialTheme.typography.bodyMedium,
                color = Black
            )
            TextButton(onClick = onSignInClick) {
                Text(text = stringResource(id = R.string.sign_in), color = PurpleDeep)
            }
            TextButton(onClick = onForgotPasswordClick) {
                Text(text = stringResource(id = R.string.forgot_password), color = PurpleDeep)
            }
        }

        // "Or"
        Text(
            text = stringResource(id = R.string.or_p),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        // Facebook
        Button(
            onClick = onFacebookClick,
            colors = ButtonDefaults.buttonColors(containerColor = FacebookBlue),
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
        ) {
            Text(
                text = stringResource(id = R.string.button_login_facebook),
                color = White
            )
        }

        // Google
        Button(
            onClick = onGoogleClick,
            colors = ButtonDefaults.buttonColors(containerColor = GoogleRed),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(
                text = stringResource(id = R.string.button_login_google),
                color = White
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    ToDoTheme {
        LoginScreen()
    }
}