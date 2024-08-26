package com.aspark.janitriassign

import android.graphics.Color.parseColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aspark.janitriassign.ui.screen.HomeScreen
import com.aspark.janitriassign.ui.theme.DarkPurple
import com.aspark.janitriassign.ui.theme.JanitriAssignTheme
import com.aspark.janitriassign.ui.theme.LightPurple

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JanitriAssignTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { MyAppBar() },
                    floatingActionButton = { AddColorFAB()}
                ) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MyAppBar() {
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
            actions = { SyncButton() {} }
        )
    }

     @Composable
    fun SyncButton(onClick: () -> Unit) {

        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(LightPurple, RoundedCornerShape(22.dp)),
        ) {
         Row(
             modifier = Modifier
                 .padding(vertical = 6.dp, horizontal = 10.dp),
             horizontalArrangement = Arrangement.spacedBy(4.dp),
             verticalAlignment = Alignment.CenterVertically
         ) {
                 Text(
                     text = "12",
                     fontSize = 20.sp,
                     color = Color.White
                 )
                 Icon(
                     painterResource(id = R.drawable.ic_sync), "",
                     tint = DarkPurple,
                     modifier = Modifier
                         .size(25.dp)
                         .clickable { onClick() }
                 )
             }
        }

    }

    @Composable
    private fun AddColorFAB() {
        FloatingActionButton(
            onClick = { /*TODO*/ },
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
        HomeScreen()
    }
}