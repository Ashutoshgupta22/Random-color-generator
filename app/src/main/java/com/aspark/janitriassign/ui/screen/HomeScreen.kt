package com.aspark.janitriassign.ui.screen

import android.graphics.Color.parseColor
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aspark.janitriassign.model.ColorListData
import com.aspark.janitriassign.ui.UiState
import com.aspark.janitriassign.ui.theme.JanitriAssignTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aspark.janitriassign.model.ColorModel
import com.aspark.janitriassign.viewmodel.HomeViewModel

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel) {

    val uiState = viewModel.uiState.collectAsState()

    when (uiState.value) {
        is UiState.Success -> {
            val data = (uiState.value as UiState.Success).data
            HomeContent(data, modifier)
        }

        is UiState.Error -> {
            ErrorScreen()
        }

        is UiState.Loading -> {
            LoadingScreen()
        }

        is UiState.Message -> TODO()
    }
}

@Composable
fun HomeContent(data: ColorListData, modifier: Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(data.colors) { color ->
            ColorItem(color = color)
        }
    }
}

@Composable
fun ColorItem(color: ColorModel) {
    Card(
        modifier = Modifier
            .height(120.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(parseColor(color.color))
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                text = color.color,
                fontSize = 18.sp,
                color = Color.White
            )
            HorizontalDivider(
                color = Color.White,
                modifier = Modifier
                    .width(90.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                Text(text = "Created at", color = Color.White)
                Text(text = color.createdAt, color = Color.White)
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    CircularProgressIndicator()
}

@Composable
fun ErrorScreen() {

}

@Preview
@Composable
private fun HomeScreenPreview() {
    JanitriAssignTheme {
        HomeScreen(viewModel = viewModel())
    }
}