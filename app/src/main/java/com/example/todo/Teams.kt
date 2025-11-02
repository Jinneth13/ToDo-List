package com.example.todo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Teams : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = this
            MaterialTheme(colorScheme = lightColorScheme()) {
                TeamsScreen(
                    onProfileClick = {
                        val intent = Intent(context, Profile::class.java)
                        context.startActivity(intent)
                    },
                    onAddTeamsClick = { /* Acción para añadir nueva tarea */ },
                    onNavigate = { destination ->
                        val intent = when (destination) {
                            "tasks" -> Intent(context, Tasks::class.java)
                            "lists" -> Intent(context, Lists::class.java)
                            "calendar" -> Intent(context, Calendar::class.java)
                            else -> null
                        }
                        intent?.let { context.startActivity(it) }
                    }
                )
            }
        }
    }
}

@Composable
fun TeamsScreen(
    onProfileClick: () -> Unit = {},
    onAddTeamsClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBarSectionTeams(onProfileClick)

            // Contenido principal desplazable
            Box(modifier = Modifier.weight(1f)) {
                TeamsContentSection(onAddTeamsClick)
            }

            // Barra inferior fija
            BottomBarSectionTeams(onNavigate)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarSectionTeams(onProfileClick: () -> Unit) {
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
                contentDescription = null,
                modifier = Modifier.size(46.dp)
            )

            Text(
                text = stringResource(id = R.string.txtTeams),
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
fun TeamsContentSection(onAddListClick: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item { TeamsCategoryItem(title = stringResource(R.string.txtTeams1), onAddClick = onAddListClick) }
        item { Spacer(modifier = Modifier.height(32.dp)) }
        item { TeamsCategoryItem(title = stringResource(R.string.txtTeams2), onAddClick = onAddListClick) }
        item { Spacer(modifier = Modifier.height(32.dp)) }
        item { TeamsCategoryItem(title = stringResource(R.string.txtTeams3), onAddClick = onAddListClick) }
        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
fun TeamsCategoryItem(title: String, onAddClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 8.dp)
        )

        IconButton(
            onClick = onAddClick,
            modifier = Modifier
                .size(40.dp)
                .background(Color.White, shape = CircleShape)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icono_plus),
                contentDescription = stringResource(id = R.string.enter_send1),
                tint = Color(0xFF51E4FF)
            )
        }
    }
}

@Composable
fun BottomBarSectionTeams(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .height(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomBarIcon(R.drawable.logo_tasks, R.string.txtTasks) { onNavigate("tasks") }
            BottomBarIcon(R.drawable.logo_lists, R.string.txtLists) { onNavigate("lists") }
            BottomBarIcon(R.drawable.logo_calendar, R.string.txtCalendar) { onNavigate("calendar") }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewTeamsScreen() {
    MaterialTheme {
        TeamsScreen()
    }
}