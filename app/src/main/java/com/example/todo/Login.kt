package com.example.todo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.todo.ui.theme.Black
import com.example.todo.ui.theme.BluePrimary
import com.example.todo.ui.theme.FacebookBlue
import com.example.todo.ui.theme.GoogleRed
import com.example.todo.ui.theme.PurpleDeep
import com.example.todo.ui.theme.ToDoTheme
import com.example.todo.ui.theme.White

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = this
            MaterialTheme(colorScheme = lightColorScheme()) {
                LoginScreen(
                    onLoginClick = {
                        val intent = Intent(context, Home::class.java)
                        context.startActivity(intent)
                    },
                    onSignInClick = {
                        val intent = Intent(context, SignIn::class.java)
                        context.startActivity(intent)
                    },
                    onForgotPasswordClick = {
                        val intent = Intent(context, RecoverPassword::class.java)
                        context.startActivity(intent)
                    },
                    onFacebookClick = {
                        val intent = Intent(context, Home::class.java)
                        context.startActivity(intent)
                    },
                    onGoogleClick = {
                        val intent = Intent(context, Home::class.java)
                        context.startActivity(intent)
                    }
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