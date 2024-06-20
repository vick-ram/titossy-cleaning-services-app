package com.example.titossycleaningservicesapp.presentation.auth.signIn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.core.LoadingScreen
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.viewmodel.CustomerAuthViewModel
import com.example.titossycleaningservicesapp.presentation.auth.utils.AuthCurve
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomButton
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomTextField
import com.example.titossycleaningservicesapp.presentation.auth.utils.PassWordTransformation
import com.example.titossycleaningservicesapp.presentation.auth.utils.ValidationState
import com.example.titossycleaningservicesapp.presentation.utils.Authentication
import com.example.titossycleaningservicesapp.presentation.utils.UserRoutes
import kotlinx.coroutines.launch

@Composable
fun CustomerSignInScreen(
    toSignUpScreen: (() -> Unit)? = null,
    navController: NavHostController,
    signInViewModel: CustomerAuthViewModel
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val passwordState by signInViewModel.passwordState.collectAsState()
    val passwordErrorMessage by signInViewModel.passwordErrorMessage.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }


    LaunchedEffect(signInViewModel, context) {
        signInViewModel.authEvent.collect { result ->
            when (result) {
                is AuthEvent.Success -> {
                    val customerStatus = result.approvalStatus
                    when (customerStatus) {
                        ApprovalStatus.PENDING -> {
                            navController.navigate(Authentication.APPROVAL.route)
                        }

                        ApprovalStatus.APPROVED -> {
                            snackBarHostState.showSnackbar(result.message.toString())
                            navController.popBackStack()
                            navController.navigate(UserRoutes.Customer.route)
                        }

                        ApprovalStatus.REJECTED -> {
                            snackBarHostState.showSnackbar("rejected")
                        }

                        null -> {}
                    }
                }

                is AuthEvent.Error -> {}

                AuthEvent.Loading -> signInViewModel.isLoading
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp,
                shape = RoundedCornerShape(15.dp),
                shadowElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AuthCurve(title = "Login")

                    Spacer(modifier = Modifier.height(10.dp))

                    CustomTextField(
                        value = signInViewModel.email,
                        onValueChange = {
                            signInViewModel.email = it
                        },
                        modifier = Modifier,
                        label = "username or email",
                        leadingIcon = Icons.Filled.Person,
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                    )

                    CustomTextField(
                        value = signInViewModel.password,
                        onValueChange = {
                            signInViewModel.password = it
                            signInViewModel.onPasswordChange(it)
                        },
                        modifier = Modifier,
                        label = "Password",
                        leadingIcon = Icons.Filled.Lock,
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        trailingIcon = if (passwordVisible) {
                            Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        },
                        onTrailingIconClicked = { passwordVisible = !passwordVisible },
                        visualTransformation = if (passwordVisible) {
                            VisualTransformation.None
                        } else {
                            PassWordTransformation()
                        },
                        errorMessage = passwordErrorMessage,
                        isError = passwordState is ValidationState.Invalid,
                    )

                    CustomButton(
                        text = "Sign In",
                        onClick = {
                            scope.launch {
                                signInViewModel.signIn()
                            }
                        },
                        modifier = Modifier.padding(16.dp),
                        enabled = passwordState is ValidationState.Valid
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
                text = "Sign Up",
                onClick = toSignUpScreen!!,
                modifier = Modifier.padding(horizontal = 32.dp),
                enabled = true
            )

            BottomSection(
                navigateToSupplier = { navController.navigate(Authentication.SUPPLIER.route) },
                navigateToEmployee = { navController.navigate(Authentication.EMPLOYEE.route) }
            )
        }
        LoadingScreen(isLoading = signInViewModel.isLoading)
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


@Composable
fun BottomSection(
    navigateToSupplier: () -> Unit,
    navigateToEmployee: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Are you a supplier?",
            style = MaterialTheme.typography.bodyLarge
        )
        TextButton(
            onClick = navigateToSupplier
        ) {
            Text(
                text = "Sign In/Sign Up",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontStyle = FontStyle.Italic
                )
            )
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Are you an employee/staff?",
            style = MaterialTheme.typography.bodyLarge
        )
        TextButton(
            onClick = navigateToEmployee
        ) {
            Text(
                text = "Sign In",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontStyle = FontStyle.Italic
                )
            )
        }
    }
}


