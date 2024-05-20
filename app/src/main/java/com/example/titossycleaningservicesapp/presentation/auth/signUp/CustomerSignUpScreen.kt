package com.example.titossycleaningservicesapp.presentation.auth.signUp

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.core.LoadingScreen
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.viewmodel.CustomerAuthViewModel
import com.example.titossycleaningservicesapp.presentation.auth.utils.AuthCurve
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomButton
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomTextField
import com.example.titossycleaningservicesapp.presentation.auth.utils.PassWordTransformation
import com.example.titossycleaningservicesapp.presentation.auth.utils.ValidationState
import com.example.titossycleaningservicesapp.presentation.users.customer.screens.rememberImeState
import com.example.titossycleaningservicesapp.presentation.utils.Authentication
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes
import com.example.titossycleaningservicesapp.presentation.utils.UserRoutes
import kotlinx.coroutines.launch

@Composable
fun CustomerSignUpScreen(
    backToLogin: () -> Unit,
    navController: NavHostController
) {
    val passwordVisibility by remember { mutableStateOf(false) }
    val signUpViewModel: CustomerAuthViewModel = hiltViewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val passwordState by signUpViewModel.passwordState.collectAsState()
    val passwordErrorMessage by signUpViewModel.passwordErrorMessage.collectAsState()
    val emailState by signUpViewModel.emailState.collectAsState()
    val emailErrorMessage by signUpViewModel.emailErrorMessage.collectAsState()
    val phoneState by signUpViewModel.phoneState.collectAsState()
    val phoneErrorMessage by signUpViewModel.phoneErrorMessage.collectAsState()

    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.maxValue, animationSpec = tween(300))
        }
    }

    LaunchedEffect(signUpViewModel, context) {
        signUpViewModel.authEvent.collect { event ->
            when (event) {
                is AuthEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is AuthEvent.Loading -> signUpViewModel.isLoading

                is AuthEvent.Success -> backToLogin()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = {
                    navController.navigate(RootNavRoutes.AUTH.route)
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = null
                    )
                }

                Text(
                    text = "Back to login",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(18.dp),
                tonalElevation = 4.dp,
                shadowElevation = 1.dp,
                color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AuthCurve(title = "Register")

                    Spacer(modifier = Modifier.height(8.dp))

                    CustomTextField(
                        value = signUpViewModel.username,
                        onValueChange = {
                            signUpViewModel.username = it
                        },
                        modifier = Modifier,
                        label = "Username",
                        leadingIcon = Icons.Filled.Person,
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    CustomTextField(
                        value = signUpViewModel.firstName,
                        onValueChange = { signUpViewModel.firstName = it },
                        modifier = Modifier,
                        label = "First Name",
                        leadingIcon = Icons.Filled.Person,
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    CustomTextField(
                        value = signUpViewModel.lastName,
                        onValueChange = { signUpViewModel.lastName = it },
                        modifier = Modifier,
                        label = "Last Name",
                        leadingIcon = Icons.Filled.Person,
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        /*isError = signUpViewModel.email.trim().isEmpty(),
                        errorMessage = "required*",
                        trailingIcon = if (signUpViewModel.email.trim()
                                .isNotEmpty()
                        ) Icons.Outlined.Info else null*/
                    )

                    CustomTextField(
                        value = signUpViewModel.phone,
                        onValueChange = {
                            signUpViewModel.phone = it
                            signUpViewModel.onPhoneChange(it)
                        },
                        modifier = Modifier,
                        label = "Phone",
                        leadingIcon = Icons.Filled.Person,
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        errorMessage = phoneErrorMessage,
                        isError = phoneState is ValidationState.Invalid,
                        trailingIcon = if (phoneState is ValidationState.Valid) Icons.Outlined.Info else null
                    )

                    CustomTextField(
                        value = signUpViewModel.email,
                        onValueChange = {
                            signUpViewModel.email = it
                            signUpViewModel.onEmailChange(it)
                        },
                        modifier = Modifier,
                        label = "Email",
                        leadingIcon = Icons.Filled.Person,
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        errorMessage = emailErrorMessage,
                        isError = emailState is ValidationState.Invalid,
                        trailingIcon = if (emailState is ValidationState.Valid) Icons.Outlined.Info else null
                    )

                    CustomTextField(
                        value = signUpViewModel.password,
                        onValueChange = {
                            signUpViewModel.password = it
                            signUpViewModel.onPasswordChange(it)
                        },
                        modifier = Modifier,
                        label = "Password",
                        leadingIcon = Icons.Filled.Person,
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        trailingIcon = if (passwordVisibility) {
                            Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        },
                        visualTransformation = if (passwordVisibility) {
                            VisualTransformation.None
                        } else {
                            PassWordTransformation()
                        },
                        isError = passwordState is ValidationState.Invalid,
                        errorMessage = passwordErrorMessage,
                    )

                    CustomButton(
                        text = "Register",
                        onClick = {
                            scope.launch {
                                signUpViewModel.signUp()
                            }
                        },
                        modifier = Modifier,
                        enabled = true
                    )
                }
            }
        }
        LoadingScreen(isLoading = signUpViewModel.isLoading)
    }
}
