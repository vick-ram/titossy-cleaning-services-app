package com.example.titossycleaningservicesapp.presentation.auth.signUp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.presentation.utils.Authentication

@Composable
fun SignUpSuccessful(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 16.dp),
            painter = painterResource(id = R.drawable.check),
            contentDescription = "sign in complete"
        )
        Text(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            text = "Your account was successfully created, you can now proceed to sign in!.",
            style = TextStyle(
                fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontSynthesis = FontSynthesis.Weight,
                textAlign = TextAlign.Center,
                fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
            )
        )

        Button(
            onClick = {
                navController.navigate(Authentication.LOGIN.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            shape = RectangleShape
        ) {
            Text(
                text = "proceed to sign in",
                fontSize = 14.sp
            )
        }

    }
}