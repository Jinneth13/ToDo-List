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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Calendar : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = this
            MaterialTheme(colorScheme = lightColorScheme()) {
                CalendarScreen(
                    onProfileClick = {
                        val intent = Intent(context, Profile::class.java)
                        context.startActivity(intent)
                    },
                    onNavigate = { destination ->
                        val intent = when (destination) {
                            "tasks" -> Intent(context, Tasks::class.java)
                            "lists" -> Intent(context, Lists::class.java)
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
fun CalendarScreen(
    onProfileClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    val todayEvents = listOf(
        Event("Reunión con el equipo de desarrollo", "10:00 AM - 11:00 AM", Color(0xFF29760B)),
        Event("Revisión médica", "2:00 PM - 3:00 PM", Color(0xFF3B5998))
    )

    val tomorrowEvents = listOf(
        Event("Clase de Taekwondo", "7:00 AM - 9:00 AM", Color(0xFFFF9800)),
        Event("Entrega de informe semanal", "11:00 AM - 12:00 PM", Color(0xFF0097A7))
    )

    val weekEvents = listOf(
        Event("Cumpleaños de David 🎂", "Todo el día", Color(0xFF8E24AA)),
        Event("Reunión de seguimiento del proyecto", "9:00 AM - 10:30 AM", Color(0xFF4CAF50)),
        Event("Presentación de avances", "4:00 PM - 5:00 PM", Color(0xFF2196F3))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBarSectionCalendar(onProfileClick)

            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    item { CalendarSectionTitle("Hoy") }
                    items(todayEvents) { event -> EventCard(event) }

                    item { Spacer(modifier = Modifier.height(24.dp)) }

                    item { CalendarSectionTitle("Mañana") }
                    items(tomorrowEvents) { event -> EventCard(event) }

                    item { Spacer(modifier = Modifier.height(24.dp)) }

                    item { CalendarSectionTitle("Esta Semana") }
                    items(weekEvents) { event -> EventCard(event) }

                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }

            BottomBarSectionCalendar(onNavigate)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarSectionCalendar(onProfileClick: () -> Unit) {
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
                text = stringResource(R.string.txtCalendar),
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
                    text = stringResource(R.string.btnProfile),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CalendarSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

data class Event(
    val title: String,
    val time: String,
    val color: Color
)

@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = event.color.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(event.color)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = event.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = event.time,
                    fontSize = 15.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun BottomBarSectionCalendar(onNavigate: (String) -> Unit) {
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
            BottomBarIcon(R.drawable.logo_teams, R.string.txtTeams) { onNavigate("teams") }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewCalendarScreen() {
    MaterialTheme {
        CalendarScreen()
    }
}