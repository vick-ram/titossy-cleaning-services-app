package com.example.titossycleaningservicesapp.presentation.auth.signUp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.domain.viewmodel.AuthViewModel
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomButton
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomTextField
import com.example.titossycleaningservicesapp.presentation.auth.utils.PassWordTransformation
import com.example.titossycleaningservicesapp.presentation.utils.CustomIndeterminateProgressIndicator
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    backToLogin: () -> Unit,
    navController: NavHostController
) {
    var passwordVisibility by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val signUpViewModel: AuthViewModel = hiltViewModel()
    val signUpState by signUpViewModel.signUpState.collectAsState()
    val userId by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(colorResource(id = R.color.gray_300)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(72.dp),
                    imageVector = Icons.Filled.Person,
                    contentDescription = "profile",
                    tint = Color.Blue
                )
            }

            CustomTextField(
                value = firstName,
                onValueChange = { firstName = it },
                modifier = Modifier,
                label = "first name",
                leadingIcon = Icons.Default.Person,
                keyBoardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            CustomTextField(
                value = lastName,
                onValueChange = { lastName = it },
                modifier = Modifier,
                label = "last name",
                leadingIcon = Icons.Default.Person,
                keyBoardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

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

            CustomTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier,
                label = "password",
                leadingIcon = Icons.Default.Lock,
                trailingIcon = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                onTrailingIconClicked = { passwordVisibility = !passwordVisibility },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PassWordTransformation(),
                keyBoardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Password
                )
            )

            CustomButton(
                modifier = Modifier,
                onClick = {
                    scope.launch {
                        signUpViewModel.signUp(userId, firstName, lastName, email, password)
                        firstName = ""
                        lastName = ""
                        email = ""
                        password = ""
                    }
                },
                text = "Sign up"
            )
            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Already have an account?")
                Text(
                    text = "Login",
                    modifier = Modifier
                        .clickable { backToLogin() },
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = MaterialTheme.typography.labelLarge.fontSize,
                    textDecoration = TextDecoration.Underline
                )
            }
            CustomIndeterminateProgressIndicator(isLoading = signUpState.isLoading)
        }
    }

    LaunchedEffect(key1 = signUpState.isSuccess) {
        scope.launch {
            if (signUpState.isSuccess?.isNotEmpty() == true) {
                navController.navigate("success") {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = signUpState.errorMessage) {
        scope.launch {
            if (signUpState.errorMessage?.isNotEmpty() == true) {
                Toast.makeText(context, signUpState.errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}