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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
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
import androidx.core.content.edit

class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        /*val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        val username = intent.getStringExtra("username")

        getSharedPreferences(getString(R.string.title_home), MODE_PRIVATE).edit {
            putString("username", username)
            putString("email", email)
            putString("password", password)
            apply()
        }*/

//        saveData(username, email, password)

        setContent {
            MaterialTheme(colorScheme = lightColorScheme()) {
                HomeScreen(
                    onProfileClick = { navigateTo(Profile::class.java) },
                    onTasksClick = { navigateTo(Tasks::class.java) },
                    onListsClick = { navigateTo(Lists::class.java) },
                    onCalendarClick = { navigateTo(Calendar::class.java) },
                    onTeamsClick = { navigateTo(Teams::class.java) }
                )
            }
        }
    }

    /*private fun saveData(username: String?, email: String?, password: String?) {
        getSharedPreferences(getString(R.string.title_home), MODE_PRIVATE).edit {
            putString("username", username)
            putString("email", email)
            putString("password", password)
            apply()
        }
    }*/


    private fun navigateTo(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }
}

@Composable
fun HomeScreen(
    onProfileClick: () -> Unit = {},
    onTasksClick: () -> Unit = {},
    onListsClick: () -> Unit = {},
    onCalendarClick: () -> Unit = {},
    onTeamsClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar()

        TopBarHome(onProfileClick = onProfileClick)

        Spacer(modifier = Modifier.height(150.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FeatureButton(
                icon = R.drawable.logo_tasks,
                label = stringResource(R.string.txtTasks),
                onClick = onTasksClick
            )
            FeatureButton(
                icon = R.drawable.logo_lists,
                label = stringResource(R.string.txtLists),
                onClick = onListsClick
            )
        }

        Spacer(modifier = Modifier.height(150.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FeatureButton(
                icon = R.drawable.logo_calendar,
                label = stringResource(R.string.txtCalendar),
                onClick = onCalendarClick
            )
            FeatureButton(
                icon = R.drawable.logo_teams,
                label = stringResource(R.string.txtTeams),
                onClick = onTeamsClick
            )
        }
    }
}

@Composable
fun TopBarHome(onProfileClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(73.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.icono),
                contentDescription = "",
                modifier = Modifier.size(46.dp)
            )

            Text(
                text = stringResource(id = R.string.title_home),
                color = Color.Black,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = onProfileClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF51E4FF)),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.btnProfile),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun FeatureButton(
    icon: Int,
    label: String,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF51E4FF)),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(120.dp)
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier.size(85.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}