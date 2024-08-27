package com.aspark.janitriassign

import android.graphics.Color.parseColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aspark.janitriassign.firebase.FirestoreDataSource
import com.aspark.janitriassign.repository.ColorRepository
import com.aspark.janitriassign.room.ColorDatabase
import com.aspark.janitriassign.ui.screen.HomeScreen
import com.aspark.janitriassign.ui.theme.DarkPurple
import com.aspark.janitriassign.ui.theme.JanitriAssignTheme
import com.aspark.janitriassign.ui.theme.LightPurple
import com.aspark.janitriassign.viewmodel.HomeViewModel
import com.aspark.janitriassign.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

         val colorRepository = ColorRepository(
            colorDao = ColorDatabase.getDatabase(applicationContext).colorDao(),
            remoteDataSource = FirestoreDataSource()
        )
         val viewModel: HomeViewModel by viewModels {
            ViewModelFactory(colorRepository)
        }
        setContent {

            val unSyncedCount = viewModel.unSyncedCount.collectAsState()

            JanitriAssignTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        MyAppBar(
                            unSyncedCount = unSyncedCount.value,
                        onSyncClick = viewModel::syncColors
                    ) },
                    floatingActionButton = {
                        AddColorFAB(
                            onFabClick = { viewModel.addRandomColor() }
                        )
                    }
                ) { innerPadding ->
                    HomeScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MyAppBar(onSyncClick: () -> Unit, unSyncedCount: Int) {
        TopAppBar(
            title = {
                Text(
                    text = title.toString(),
                    color = Color.White,
                    fontSize = 24.sp
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = DarkPurple
            ),
            actions = { SyncButton(
                unSyncedCount = unSyncedCount,
                onClick = onSyncClick) }
        )
    }

    @Composable
    fun SyncButton(onClick: () -> Unit, unSyncedCount: Int) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(LightPurple, RoundedCornerShape(22.dp))
                .clickable { onClick() },
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 6.dp, horizontal = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$unSyncedCount",
                    fontSize = 20.sp,
                    color = Color.White
                )
                Icon(
                    painterResource(id = R.drawable.ic_sync), "",
                    tint = DarkPurple,
                    modifier = Modifier
                        .size(25.dp)
                        .clickable { }
                )
            }
        }

    }

    @Composable
    private fun AddColorFAB(onFabClick: () -> Unit) {
        FloatingActionButton(
            onClick = onFabClick,
            containerColor = Color(parseColor("#B6B9FF")),
            modifier = Modifier
                .height(45.dp),
            shape = RoundedCornerShape(28.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Add Color",
                    fontSize = 18.sp,
                    color = Color(parseColor("#5659A4"))
                )
                Icon(
                    Icons.Filled.Add, "",
                    tint = Color(parseColor("#B6B9FF")),
                    modifier = Modifier
                        .size(25.dp)
                        .background(
                            Color(parseColor("#5659A4")),
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JanitriAssignTheme {
        HomeScreen(viewModel = viewModel())
    }
}