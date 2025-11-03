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

class Lists : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = this
            MaterialTheme(colorScheme = lightColorScheme()) {
                ListsScreen(
                    onHomeClick = {
                        val intent = Intent(context, Home::class.java)
                        context.startActivity(intent)
                    },
                    onProfileClick = {
                        val intent = Intent(context, Profile::class.java)
                        context.startActivity(intent)
                    },
                    onAddListClick = {},
                    onNavigate = { destination ->
                        val intent = when (destination) {
                            "tasks" -> Intent(context, Tasks::class.java)
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

@Composable
fun ListsScreen(
    onHomeClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onAddListClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    val workLists = listOf(
        "Plan de desarrollo de app móvil",
        "Corrección de errores sprint actual",
        "Actualización de documentación técnica"
    )
    val studyLists = listOf(
        "Repasar estructuras de datos",
        "Preparar exposición de estadística",
        "Resolver ejercicios de álgebra"
    )
    val personalLists = listOf(
        "Comprar regalo de cumpleaños 🎁",
        "Cita médica general",
        "Actualizar portafolio personal"
    )
    val shoppingLists = listOf(
        "Verduras y frutas frescas 🥦🍎",
        "Artículos de limpieza",
        "Recarga del transporte"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar()
            TopBarSectionLists(onHomeClick, onProfileClick)
            Box(modifier = Modifier.weight(1f)) {
                ListsContentSection(
                    workLists = workLists,
                    studyLists = studyLists,
                    personalLists = personalLists,
                    shoppingLists = shoppingLists,
                    onAddListClick = onAddListClick
                )
            }
            BottomBarSectionLists(onNavigate)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarSectionLists(onHomeClick: () -> Unit, onProfileClick: () -> Unit) {
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
                text = stringResource(id = R.string.txtLists),
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
fun ListsContentSection(
    workLists: List<String>,
    studyLists: List<String>,
    personalLists: List<String>,
    shoppingLists: List<String>,
    onAddListClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item { ListCategory(title = stringResource(R.string.txtLists1), lists = workLists, color = Color(0xFFE6F9FF), onAddClick = onAddListClick) }
        item { Spacer(modifier = Modifier.height(32.dp)) }
        item { ListCategory(title = stringResource(R.string.txtLists2), lists = studyLists, color = Color(0xFFFFF3E0), onAddClick = onAddListClick) }
        item { Spacer(modifier = Modifier.height(32.dp)) }
        item { ListCategory(title = stringResource(R.string.txtLists3), lists = personalLists, color = Color(0xFFE8F5E9), onAddClick = onAddListClick) }
        item { Spacer(modifier = Modifier.height(32.dp)) }
        item { ListCategory(title = stringResource(R.string.txtLists4), lists = shoppingLists, color = Color(0xFFE3F2FD), onAddClick = onAddListClick) }
        item { Spacer(modifier = Modifier.height(100.dp)) }
    }
}

@Composable
fun ListCategory(title: String, lists: List<String>, color: Color, onAddClick: () -> Unit) {
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

        lists.forEach { list ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = color),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "• $list",
                    modifier = Modifier.padding(12.dp),
                    color = Color.Black,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun BottomBarSectionLists(onNavigate: (String) -> Unit) {
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
            BottomBarIcon(R.drawable.logo_calendar, R.string.txtCalendar) { onNavigate("calendar") }
            BottomBarIcon(R.drawable.logo_teams, R.string.txtTeams) { onNavigate("teams") }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewListsScreen() {
    MaterialTheme {
        ListsScreen()
    }
}