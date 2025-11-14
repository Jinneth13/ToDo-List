package com.example.todo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import androidx.core.content.edit
import com.facebook.login.LoginManager

class Profile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = getSharedPreferences(getString(R.string.title_home), Context.MODE_PRIVATE)
        val username = prefs.getString("username", "Usuario desconocido") ?: ""
        val email = prefs.getString("email", "") ?: ""
        val password = prefs.getString("password", "") ?: ""

        setContent {
            val context = this
            MaterialTheme(colorScheme = lightColorScheme()) {
                ProfileScreen(
                    username = username,
                    email = email,
                    password = password,
                    onBackClick = { finish() },
                    onLogoutClick = {
                        getSharedPreferences(
                            getString(R.string.title_home),
                            Context.MODE_PRIVATE
                        ).edit {
                            clear()
                        }

//                        com.facebook.login.LoginManager.getInstance().logOut()
                        FirebaseAuth.getInstance().signOut()

                        val intent = Intent(context, Login::class.java)
                        context.startActivity(intent)
                        finish()
                    },
                    onEditName = {
                        val intent = Intent(context, ChangeUsername::class.java)
                        context.startActivity(intent)
                    },
                    onEditEmail = {
                        val intent = Intent(context, ChangeEmail::class.java)
                        context.startActivity(intent)
                    },
                    onEditPassword = {
                        val intent = Intent(context, ChangePassword::class.java)
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

fun logoutUser(context: Context) {
    context.getSharedPreferences(
        context.getString(R.string.title_home),
        Context.MODE_PRIVATE
    ).edit {
        clear()
    }

    try {
        LoginManager.getInstance().logOut()
    } catch (_: Exception) {}
    FirebaseAuth.getInstance().signOut()

    val intent = Intent(context, Login::class.java)
    context.startActivity(intent)
}

@Composable
fun ProfileScreen(
    username: String,
    email: String,
    password: String,
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onEditName: () -> Unit = {},
    onEditEmail: () -> Unit = {},
    onEditPassword: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar()
        TopSection(onBackClick = onBackClick, onLogoutClick = onLogoutClick)
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.iconop),
            contentDescription = null,
            modifier = Modifier.size(170.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .background(Color(0xFFCED8E4), CircleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.icono_usuario),
                contentDescription = null,
                modifier = Modifier.size(70.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF51E4FF))
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ProfileInfoRow(
                    label = stringResource(R.string.txtUsername),
                    value = username.ifEmpty { "Usuario desconocido" },
                    onEditClick = onEditName
                )

                Spacer(modifier = Modifier.height(8.dp))

                ProfileInfoRow(
                    label = stringResource(R.string.enter_email),
                    value = email.ifEmpty { "Correo no disponible" },
                    onEditClick = onEditEmail
                )

                Spacer(modifier = Modifier.height(8.dp))

                ProfileInfoRow(
                    label = stringResource(R.string.enter_contraseñap_hint),
                    value = stringResource(R.string.txtPasswordHidden),
                    onEditClick = onEditPassword
                )
            }
        }
    }
}

@Composable
fun TopSection(
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onBackClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF51E4FF)),
            contentPadding = PaddingValues(10.dp),
            modifier = Modifier
                .size(48.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.icono_arrow),
                contentDescription = null,
                modifier = Modifier
                    .size(46.dp)
                    .graphicsLayer(
                        scaleX = -1f
                    )
            )
        }

        Button(
            onClick = onLogoutClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF51E4FF)),
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.btnLogout),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ProfileInfoRow(
    label: String,
    value: String,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = label, color = Color.White, fontSize = 18.sp)
            Text(
                text = value,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        IconButton(
            onClick = onEditClick,
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFF51E4FF), CircleShape)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icono_edit),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        username = "Usuario",
        email = "james.iredell@examplepetstore.com",
        password = "contraseña"
        )
}