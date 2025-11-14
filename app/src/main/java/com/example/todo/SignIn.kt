package com.example.todo

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import com.example.todo.ui.theme.BluePrimary
import com.example.todo.ui.theme.FacebookBlue
import com.example.todo.ui.theme.GoogleRed
import com.example.todo.ui.theme.PurpleDeep
import com.example.todo.ui.theme.ToDoTheme
import com.example.todo.ui.theme.White
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignIn : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()

        setContent {
            MaterialTheme(colorScheme = lightColorScheme()) {
                SignInScreen(
                    auth = auth, callbackManager = callbackManager
                )
            }
        }
    }
}

@Composable
fun SignInScreen(
    auth: FirebaseAuth, callbackManager: CallbackManager
) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        FacebookSdk.sdkInitialize(context)
        AppEventsLogger.activateApp(context.applicationContext as Application)
    }

    // Google launcher
    val googleLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            successMessage = "Registro con Google exitoso"

                            val userEmail = account.email ?: ""

                            context.getSharedPreferences(
                                context.getString(R.string.title_home), Context.MODE_PRIVATE
                            ).edit {
                                putString("username", "")
                                putString("email", userEmail)
                                putString("password", "")
                            }

                            showLogin(context, "", userEmail, "")
                        } else {
                            errorMessage = "Error al autenticar con Google"
                        }
                    }

                } catch (e: ApiException) {
                    errorMessage = "Error con Google Sign-In"
                }
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar()

        SignInContent(
            username = username,
            email = email,
            password = password,
            repeatPassword = repeatPassword,
            onUsernameChange = { username = it },
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
            onRepeatPasswordChange = { repeatPassword = it },
            onRegisterClick = {
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                    errorMessage = "Por favor completa todos los campos"
                } else if (password != repeatPassword) {
                    errorMessage = "Las contraseñas no coinciden"
                } else {
                    registerUser(auth, context, username, email, password, onError = {
                        errorMessage = it
                    }, onSuccess = {
                        successMessage = "Usuario registrado correctamente"
                    })
                }
            },
            onFacebookClick = {
                LoginManager.getInstance().logInWithReadPermissions(
                    context as Activity, listOf("email", "public_profile")
                )
                LoginManager.getInstance().registerCallback(
                    callbackManager, object : FacebookCallback<LoginResult> {
                        override fun onSuccess(result: LoginResult) {
                            val token = result.accessToken
                            val credential = FacebookAuthProvider.getCredential(token.token)
                            auth.signInWithCredential(credential).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    successMessage = "Registro con Facebook exitoso"
                                    showLogin(context, username, email, password)
                                } else {
                                    errorMessage = "Error al autenticar con Facebook"
                                }
                            }
                        }

                        override fun onCancel() {}
                        override fun onError(error: FacebookException) {
                            errorMessage = "Error al conectar con Facebook"
                        }
                    })
            },
            onGoogleClick = {
                val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail().build()
                val googleClient = GoogleSignIn.getClient(context, googleConf)
                googleClient.signOut()
                googleLauncher.launch(googleClient.signInIntent)
            },
            onBackClick = {
                context.startActivity(Intent(context, Login::class.java))
            })
    }

    // Snackbar de error o éxito
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        errorMessage?.let {
            Snackbar(
                modifier = Modifier.padding(16.dp), containerColor = Color.Red
            ) { Text(text = it, color = Color.White) }
        }
        successMessage?.let {
            Snackbar(
                modifier = Modifier.padding(16.dp), containerColor = Color(0xFF4CAF50)
            ) { Text(text = it, color = Color.White) }
        }
    }
}

fun registerUser(
    auth: FirebaseAuth,
    context: Context,
    username: String,
    email: String,
    password: String,
    onError: (String) -> Unit,
    onSuccess: () -> Unit
) {
    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            context.getSharedPreferences(
                context.getString(R.string.title_home), Context.MODE_PRIVATE
            ).edit {

                putString("username", username)
                putString("email", email)
                putString("password", password)
            }

            showLogin(context, username, email, password)
            onSuccess()
        } else {
            onError("Error registrando usuario")
        }
    }
}

fun showLogin(context: Context, username: String, email: String, password: String) {
    val intent = Intent(context, Login::class.java).apply {
        putExtra("username", username)
        putExtra("email", email)
        putExtra("password", password)
    }
    context.startActivity(intent)
}

@Composable
fun SignInContent(
    username: String,
    email: String,
    password: String,
    repeatPassword: String,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onBackClick: () -> Unit
) {
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
            onValueChange = onUsernameChange,
            label = { Text(stringResource(id = R.string.enter_username)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text(stringResource(id = R.string.enter_email)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text(stringResource(id = R.string.enter_contraseñap_hint)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )

        // Repeat password
        OutlinedTextField(
            value = repeatPassword,
            onValueChange = onRepeatPasswordChange,
            label = { Text(stringResource(id = R.string.enter_repeat_password)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )

        // Register button
        Button(
            onClick = onRegisterClick,
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
        ) {
            Text(
                text = stringResource(id = R.string.enter_login1), color = White
            )
        }

        // “Or”
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
                text = stringResource(id = R.string.button_login_facebook), color = White
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
                text = stringResource(id = R.string.button_login_google), color = White
            )
        }

        // Back
        TextButton(onClick = onBackClick) {
            Text(text = "Return to login", color = PurpleDeep)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInScreenPreview() {
    ToDoTheme {
        SignInScreen(
            auth = FirebaseAuth.getInstance(), callbackManager = CallbackManager.Factory.create()
        )
    }
}