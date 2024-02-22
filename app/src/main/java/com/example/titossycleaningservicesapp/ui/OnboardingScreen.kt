package com.example.titossycleaningservicesapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.domain.viewmodel.AuthViewModel
import com.example.titossycleaningservicesapp.presentation.utils.CustomIndeterminateProgressIndicator
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes
import kotlinx.coroutines.launch

@Composable
fun OnBoardingScreen(navController: NavHostController) {
    val viewModel: AuthViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()
    val isLoading = viewModel.isLoading.value
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(id = R.drawable.download_house_cleaning_services___cleaning_service_cartoon_png_png_image_with_no_backgroud___pngkey_com),
                    contentDescription = "onBoarding image",
                    contentScale = ContentScale.FillWidth
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 16.dp),
                        text = "Welcome to Titossy cleaning services!",
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = "We offer top-notch, eco-friendly cleaning services for homes and businesses. Start your journey to a cleaner space with us today! Click the button below to get started",
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Normal,
                            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                        )
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.setOnboardingCompleted()
                        }
                        navController.navigate(RootNavRoutes.AUTH.route) {
                            popUpTo(RootNavRoutes.ONBOARDING.route) {
                                inclusive = true
                            }
                        }
                    },
                    shape = RectangleShape
                ) {
                    Text(text = "Getting started")
                }
            }

        }
        CustomIndeterminateProgressIndicator(isLoading = isLoading)
    }
}