package com.example.titossycleaningservicesapp.presentation.auth.signIn

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.domain.viewmodel.AuthViewModel
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomButton
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomTextField
import com.example.titossycleaningservicesapp.presentation.auth.utils.PassWordTransformation
import com.example.titossycleaningservicesapp.presentation.utils.Authentication
import com.example.titossycleaningservicesapp.presentation.utils.UserRoutes
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    toSignUpScreen: () -> Unit,
    navController: NavHostController,
    onboardingCompleted: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val signInViewModel: AuthViewModel = hiltViewModel()
    val signInState by signInViewModel.signInState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(colorResource(id = R.color.gray_300)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(60.dp),
                imageVector = Icons.Filled.Person,
                contentDescription = "profile",
                tint = Color.Blue
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier,
            label = "email",
            leadingIcon = Icons.Default.Email,
            keyBoardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier,
            label = "password",
            leadingIcon = Icons.Default.Lock,
            trailingIcon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
            onTrailingIconClicked = { passwordVisible = !passwordVisible },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PassWordTransformation(),
            keyBoardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomButton(
            onClick = {
                scope.launch {
                    signInViewModel.signIn(email, password).isCompleted
                }
            },
            modifier = Modifier,
            text = "Sign in"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Employee?",
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodyMedium
            )
            TextButton(
                onClick = {
                    onboardingCompleted()
                    navController.navigate(Authentication.EMPLOYEE.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                },
                content = {
                    Text(
                        text = "sign in", fontSize = 18.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = TextDecoration.Underline,
                        fontStyle = FontStyle.Italic
                    )
                },
                contentPadding = PaddingValues(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = "Don't have an account? ",
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "Sign up",
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Italic,
                style = TextStyle(color = Color.Blue),
                fontSize = 18.sp,
                modifier = Modifier.clickable { toSignUpScreen() },
            )
        }

        LaunchedEffect(key1 = signInState.isSuccess) {
            scope.launch {
                if (signInState.isSuccess?.isNotEmpty() == true) {
                    navController.navigate(UserRoutes.Customer.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }
        }

        LaunchedEffect(key1 = signInState.errorMessage) {
            scope.launch {
                if (signInState.errorMessage?.isNotEmpty() == true) {
                    Toast.makeText(context, signInState.errorMessage, Toast.LENGTH_LONG).show()
                }
            }
            !signInState.isLoading
        }

        LaunchedEffect(key1 = signInState.isLoading, block = {
            scope.launch {
                if (!signInState.isLoading) {
                    Toast.makeText(context, "", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

}
