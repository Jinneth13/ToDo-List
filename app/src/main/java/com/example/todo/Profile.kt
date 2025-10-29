package com.example.todo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Profile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = this
            MaterialTheme(colorScheme = lightColorScheme()) {
                ProfileScreen(
                    onBackClick = { finish() },
                    onLogoutClick = {
                        val intent = Intent(context, Login::class.java)
                        context.startActivity(intent)
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

@Composable
fun ProfileScreen(
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
        TopSection(
            onBackClick = onBackClick,
            onLogoutClick = onLogoutClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.iconop),
            contentDescription = ""/*stringResource(R.string.app_icon_desc)*/,
            modifier = Modifier.size(170.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Imagen de perfil
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .background(Color(0xFFCED8E4), CircleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.icono_usuario),
                contentDescription = ""/*stringResource(R.string.profile_picture_desc)*/,
                modifier = Modifier.size(70.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fondo inferior azul
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF51E4FF))
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Nombre
                ProfileInfoRow(
                    label = stringResource(R.string.txtUsername),
                    value = "",
                    onEditClick = onEditName
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Email
                ProfileInfoRow(
                    label = stringResource(R.string.enter_email),
                    value = stringResource(R.string.txtEmailHidden),
                    onEditClick = onEditEmail
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Contraseña
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
        // Botón de retroceso
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack/*painter = painterResource(id = R.drawable.ic_arrow_back)*/,
                contentDescription = stringResource(R.string.button_back_text),
                tint = Color.Black,
                modifier = Modifier.size(28.dp)
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
            Text(
                text = label,
                color = Color.White,
                fontSize = 16.sp
            )
            Text(
                text = value,
                color = Color.White,
                fontSize = 16.sp,
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
                contentDescription = ""/*stringResource(R.string.edit_icon_desc)*/,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}