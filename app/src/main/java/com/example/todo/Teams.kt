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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextOverflow
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
                    onHomeClick = {
                        val intent = Intent(context, Home::class.java)
                        context.startActivity(intent)
                    },
                    onProfileClick = {
                        val intent = Intent(context, Profile::class.java)
                        context.startActivity(intent)
                    },
                    onAddTeamsClick = {},
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
    onHomeClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onAddTeamsClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    val pendingTasksTeams = listOf(
        Team("Registro con FireBase", listOf("Jeu", "Laura", "Carlos", "María")),
        Team("Agregar Tareas", listOf("Andrés", "Camila", "José")),
        Team("Vincular API", listOf("Jinneth", "David"))
    )
    val inProgressTeams = listOf(
        Team("Logica de Actividades", listOf("Jeu", "Valentina", "Sara")),
        Team("Semillero de Innovación", listOf("Jinneth", "Nicolás", "Juliana")),
        Team("Grupo de Programación", listOf("Jeu", "Felipe", "Paula"))
    )
    val doneTeams = listOf(
        Team("Definicion de Vistas", listOf("Jeu", "Ángela", "Camilo")),
        Team("Navegación de Vistas", listOf("Valeria", "Santiago", "Jinneth")),
        Team("Documento de Entrega", listOf("Jeu", "Miguel", "Karen"))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar()
            TopBarSectionTeams(onHomeClick, onProfileClick)
            Box(modifier = Modifier.weight(1f)) {
                TeamsContentSection(
                    pendingTasksTeams = pendingTasksTeams,
                    inProgressTeams = inProgressTeams,
                    doneTeams = doneTeams,
                    onAddTeamsClick = onAddTeamsClick
                )
            }
            BottomBarSectionTeams(onNavigate)
        }
    }
}

data class Team(val name: String, val members: List<String>)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarSectionTeams(onHomeClick: () -> Unit, onProfileClick: () -> Unit) {
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
            Button(
                onClick = onHomeClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icono),
                    contentDescription = null,
                    modifier = Modifier.size(46.dp)
                )
            }

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
fun TeamsContentSection(
    pendingTasksTeams: List<Team>,
    inProgressTeams: List<Team>,
    doneTeams: List<Team>,
    onAddTeamsClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            TeamsCategory(
                title = stringResource(R.string.txtTeams1),
                teams = pendingTasksTeams,
                color = Color(0xFFE6F9FF),
                onAddClick = onAddTeamsClick
            )
        }
        item { Spacer(modifier = Modifier.height(32.dp)) }
        item {
            TeamsCategory(
                title = stringResource(R.string.txtTeams2),
                teams = inProgressTeams,
                color = Color(0xFFFFF3E0),
                onAddClick = onAddTeamsClick
            )
        }
        item { Spacer(modifier = Modifier.height(32.dp)) }
        item {
            TeamsCategory(
                title = stringResource(R.string.txtTeams3),
                teams = doneTeams,
                color = Color(0xFFE8F5E9),
                onAddClick = onAddTeamsClick
            )
        }
        item { Spacer(modifier = Modifier.height(100.dp)) }
    }
}

@Composable
fun TeamsCategory(title: String, teams: List<Team>, color: Color, onAddClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(start = 8.dp)
            )
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(26.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icono_plus),
                    contentDescription = null,
                    modifier = Modifier.size(46.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        teams.forEach { team ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = color),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = team.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Miembros: ${team.members.joinToString(", ")}",
                        fontSize = 15.sp,
                        color = Color.DarkGray
                    )
                }
            }
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