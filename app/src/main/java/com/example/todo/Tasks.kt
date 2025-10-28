package com.example.todo

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo.R

class Tasks : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    onProfileClick: () -> Unit = {},
    onAddTaskClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBarSection(onProfileClick)
            TaskContentSection()
            BottomBarSection()
        }

        FloatingActionButton(
            onClick = onAddTaskClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                painter = painterResource(id = R.drawable.bt_add_tasks),
                contentDescription = stringResource(R.string.enter_send1),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarSection(onProfileClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.txtTasks),
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.icono),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(32.dp)
            )
        },
        actions = {
            Button(
                onClick = onProfileClick,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(42.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = stringResource(R.string.btnProfile),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 10.sp
                )
            }
        }
    )
}

@Composable
fun TaskContentSection() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item { TaskCategoryTitle(stringResource(R.string.txtTasks1)) }
        item { Spacer(modifier = Modifier.height(40.dp)) }
        item { TaskCategoryTitle(stringResource(R.string.txtTasks2)) }
        item { Spacer(modifier = Modifier.height(40.dp)) }
        item { TaskCategoryTitle(stringResource(R.string.txtTasks3)) }
        item { Spacer(modifier = Modifier.height(40.dp)) }
        item { TaskCategoryTitle(stringResource(R.string.txtTasks4)) }
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun TaskCategoryTitle(title: String) {
    Text(
        text = title,
        fontSize = 22.sp,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(top = 24.dp)
    )
}

@Composable
fun BottomBarSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomBarIcon(R.drawable.logo_tasks, R.string.txtTasks)
            BottomBarIcon(R.drawable.logo_lists, R.string.txtLists)
            BottomBarIcon(R.drawable.logo_calendar, R.string.txtCalendar)
        }
    }
}

@Composable
fun BottomBarIcon(iconRes: Int, labelRes: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = stringResource(labelRes),
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .padding(10.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(id = labelRes),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
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