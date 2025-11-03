package com.example.todo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

class Tasks : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = this
            MaterialTheme(colorScheme = lightColorScheme()) {
                TasksScreen(
                    onHomeClick = {
                        val intent = Intent(context, Home::class.java)
                        context.startActivity(intent)
                    },
                    onProfileClick = {
                        val intent = Intent(context, Profile::class.java)
                        context.startActivity(intent)
                    },
                    onAddTaskClick = {},
                    onNavigate = { destination ->
                        val intent = when (destination) {
                            "lists" -> Intent(context, Lists::class.java)
                            "calendar" -> Intent(context, Calendar::class.java)
                            "teams" -> Intent(context, Teams::class.java)
                            else -> null
                        }
                        intent?.let { context.startActivity(it) }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    onHomeClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onAddTaskClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    val todayTasks = listOf("Revisar correos", "Enviar informe semanal", "Reunión con el equipo")
    val tomorrowTasks = listOf("Actualizar presentación", "Llamar a proveedor")
    val weekTasks = listOf("Completar módulo Compose", "Revisar documentación", "Diseñar logo del proyecto")
    val monthTasks = listOf("Preparar entrega final", "Evaluar retroalimentación", "Publicar versión beta")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopBar()

            TopBarSectionTasks(onHomeClick, onProfileClick)

            Box(modifier = Modifier.weight(1f)) {
                TaskContentSection(
                    todayTasks = todayTasks,
                    tomorrowTasks = tomorrowTasks,
                    weekTasks = weekTasks,
                    monthTasks = monthTasks
                )
            }

            BottomBarSectionTasks(onNavigate)
        }

        FloatingActionButton(
            onClick = onAddTaskClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 120.dp),
            containerColor = Color(0xFF51E4FF)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.bt_add_tasks),
                contentDescription = stringResource(R.string.enter_send1),
                tint = Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarSectionTasks(onHomeClick: () -> Unit, onProfileClick: () -> Unit) {
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
                text = stringResource(id = R.string.txtTasks),
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
fun TaskContentSection(
    todayTasks: List<String>,
    tomorrowTasks: List<String>,
    weekTasks: List<String>,
    monthTasks: List<String>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item { TaskCategoryTitle(stringResource(R.string.txtTasks1)) }
        items(todayTasks) { task -> TaskItem(task) }

        item { Spacer(modifier = Modifier.height(32.dp)) }

        item { TaskCategoryTitle(stringResource(R.string.txtTasks2)) }
        items(tomorrowTasks) { task -> TaskItem(task) }

        item { Spacer(modifier = Modifier.height(32.dp)) }

        item { TaskCategoryTitle(stringResource(R.string.txtTasks3)) }
        items(weekTasks) { task -> TaskItem(task) }

        item { Spacer(modifier = Modifier.height(32.dp)) }

        item { TaskCategoryTitle(stringResource(R.string.txtTasks4)) }
        items(monthTasks) { task -> TaskItem(task) }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun TaskCategoryTitle(title: String) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun TaskItem(task: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F9FF))
    ) {
        Text(
            text = "• $task",
            modifier = Modifier.padding(12.dp),
            color = Color.Black,
            fontSize = 16.sp
        )
    }
}

@Composable
fun BottomBarSectionTasks(onNavigate: (String) -> Unit) {
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
            BottomBarIcon(R.drawable.logo_lists, R.string.txtLists) { onNavigate("lists") }
            BottomBarIcon(R.drawable.logo_calendar, R.string.txtCalendar) { onNavigate("calendar") }
            BottomBarIcon(R.drawable.logo_teams, R.string.txtTeams) { onNavigate("teams") }
        }
    }
}

@Composable
fun BottomBarIcon(iconRes: Int, labelRes: Int, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = stringResource(labelRes),
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color(0xFF51E4FF))
                .padding(5.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(id = labelRes),
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewTasksScreen() {
    MaterialTheme {
        TasksScreen()
    }
}