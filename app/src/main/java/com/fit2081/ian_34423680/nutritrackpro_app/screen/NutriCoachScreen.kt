package com.fit2081.ian_34423680.nutritrackpro_app.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
//import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImagePainter
//import coil.compose.rememberAsyncImagePainterimport coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.fit2081.ian_34423680.nutritrackpro_app.screen.ui.AppBottomBar
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.NutriCoachViewModel

@Composable
fun NutriCoachScreen(
    navController: NavHostController,
    viewModel: NutriCoachViewModel,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val generatedTip by viewModel::generatedTip
    val motivationalMessageLoading by viewModel::motivationalMessageLoading
    val tipHistory by viewModel::tipHistory
    val showTipsDialog by viewModel::showTipsDialog

    LaunchedEffect(Unit) {
        viewModel.refreshAll()
        viewModel.refreshImage()
    }

    Scaffold(
        bottomBar = { AppBottomBar(navController = navController) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .height(screenHeight / 2)
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Nutricoach",
                            fontSize = 22.sp,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(Modifier.height(12.dp))

                        if (!viewModel.isUserOptimal) {
                            NonOptimalDisplay(viewModel)
                        } else {
                            OptimalDisplay(viewModel)
                        }
                    }
                }

                item {
                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray)
                }

                item {
                    Column(
                        modifier = Modifier
                            .height(screenHeight / 2)
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = { viewModel.generateTip() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8000FF)),
                            shape = RoundedCornerShape(50),
                            enabled = !motivationalMessageLoading
                        ) {
                            Icon(
                                Icons.Default.ChatBubble,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("Motivational Message (AI)", color = Color.White)
                        }

                        if (motivationalMessageLoading) {
                            CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
                        }

                        generatedTip?.let {
                            Spacer(Modifier.height(12.dp))
                            Text(it.content)
                        }
                    }
                }
            }

            Button(
                onClick = { viewModel.toggleTipDialog() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8000FF)),
                shape = RoundedCornerShape(50)
            ) {
                Icon(Icons.Default.History, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(6.dp))
                Text("Show All Tips", color = Color.White)
            }

            if (showTipsDialog) {
                AlertDialog(
                    onDismissRequest = { viewModel.toggleTipDialog() },
                    confirmButton = {
                        TextButton(onClick = { viewModel.toggleTipDialog() }) {
                            Text("Done")
                        }
                    },
                    title = { Text("AI Tips") },
                    text = {
                        if (tipHistory.isEmpty()) {
                            Text("No tips yet.")
                        } else {
                            LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                                itemsIndexed(tipHistory) { index, tip ->
                                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                                        Text("Tip ${index + 1}", fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(tip.content)
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun OptimalDisplay(viewModel: NutriCoachViewModel) {
    val painter = rememberAsyncImagePainter(viewModel.generateimagelink)
    val imageState = painter.state

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        // Use Image, not AsyncImage when you have a painter
        Image(
            painter = painter,
            contentDescription = "Motivational Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // optional, improves fit
        )

        // Show overlay text only after image loads
        AnimatedVisibility(
            visible = imageState is AsyncImagePainter.State.Success,
            enter = fadeIn(animationSpec = tween(600))
        ) {
            Text(
                "“Don't let the perfect future you give the current you a hard time.”",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(12.dp),
                fontStyle = FontStyle.Italic,
                color = Color.White,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(
                    shadow = Shadow(color = Color.Black, blurRadius = 6f)
                )
            )
        }
    }
}

@Composable
fun NonOptimalDisplay(viewModel: NutriCoachViewModel) {
    val fruit by viewModel::fruit
    val fruitData by viewModel::fruitData
    val isLoading by viewModel::isLoading

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = fruit,
            onValueChange = { viewModel.updateFruit(it) },
            label = { Text("Enter a Fruit name") },
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(8.dp))
        Button(
            onClick = { viewModel.findFruit() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8000FF)),
            shape = RoundedCornerShape(50),
            enabled = !isLoading
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(6.dp))
            Text("Details", color = Color.White)
        }
    }

    Spacer(Modifier.height(12.dp))

    fruitData?.let { data ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 2200.dp)
                .padding(vertical = 12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Fruit: ${data.name}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Family: ${data.family}", fontSize = 16.sp)

                Divider(modifier = Modifier.padding(vertical = 20.dp))

                Text("Nutrition Facts", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(Modifier.height(4.dp))
                Text("Calories: ${data.nutritions.calories}")
                Text("Fat: ${data.nutritions.fat}g")
                Text("Sugar: ${data.nutritions.sugar}g")
                Text("Carbohydrates: ${data.nutritions.carbohydrates}g")
                Text("Protein: ${data.nutritions.protein}g")
            }
        }
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
    }
}
