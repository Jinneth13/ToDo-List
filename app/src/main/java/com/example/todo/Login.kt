package com.example.todo

import android.app.Activity
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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.todo.ui.theme.Black
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

class Login : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(application)
        enableEdgeToEdge()

        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val prefs = getSharedPreferences(getString(R.string.title_home), MODE_PRIVATE)
            val username = prefs.getString("username", currentUser.displayName)
            val email = prefs.getString("email", currentUser.email)
            val password = prefs.getString("password", "")
            showHome(this, username ?: "", email ?: "", password ?: "")
            finish()
            return
        }

        setContent {
            MaterialTheme(colorScheme = lightColorScheme()) {
                LoginScreen(
                    auth = auth, callbackManager = callbackManager
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    auth: FirebaseAuth, callbackManager: CallbackManager
) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    // Google launcher
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        successMessage = "Inicio de sesión con Google exitoso"
                        showHome(context, account.email ?: "", "")
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

        LoginContent(
            username = username,
            email = email,
            password = password,
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
            onLoginClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    errorMessage = "Por favor completa todos los campos"
                } else {
                    loginWithEmail(
                        auth,
                        context,
                        email,
                        password,
                        onError = { errorMessage = it },
                        onSuccess = { successMessage = "Inicio de sesión exitoso" })
                }
            },
            onSignInClick = { context.startActivity(Intent(context, SignIn::class.java)) },
            onForgotPasswordClick = {
                context.startActivity(Intent(context, RecoverPassword::class.java))
            },
            onFacebookClick = {
                LoginManager.getInstance().logInWithReadPermissions(
                    context as Activity, listOf("email", "public_profile")
                )
                LoginManager.getInstance()
                    .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                        override fun onSuccess(result: LoginResult) {
                            val token = result.accessToken
                            val credential = FacebookAuthProvider.getCredential(token.token)
                            auth.signInWithCredential(credential).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    successMessage = "Inicio de sesión con Facebook exitoso"
                                    showHome(context, email, password)
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

fun loginWithEmail(
    auth: FirebaseAuth,
    context: Context,
    email: String,
    password: String,
    onError: (String) -> Unit,
    onSuccess: () -> Unit
) {
    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val prefs = context.getSharedPreferences(
                context.getString(R.string.title_home), Context.MODE_PRIVATE
            )

            val savedUsername = prefs.getString("username", null)

            val username = savedUsername ?: "F"

            prefs.edit {
                putString("email", email)
                putString("password", password)
                putString("username", username)
            }

            showHome(context, email, password, username)
            onSuccess()
        } else {
            onError("Error autenticando el usuario")
        }
    }
}

fun showHome(context: Context, email: String, password: String, username: String? = null) {
    val intent = Intent(context, Home::class.java).apply {
        putExtra("email", email)
        putExtra("password", password)
        putExtra("username", username)
    }
    context.startActivity(intent)
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
    username: String,
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onSignInClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onGoogleClick: () -> Unit
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

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text(stringResource(id = R.string.enter_name_hint)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
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

        // Login button
        Button(
            onClick = onLoginClick,
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
        ) {
            Text(
                text = stringResource(id = R.string.enter_login1), color = White
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
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    ToDoTheme {
        LoginScreen(
            auth = FirebaseAuth.getInstance(), callbackManager = CallbackManager.Factory.create()
        )
    }
}