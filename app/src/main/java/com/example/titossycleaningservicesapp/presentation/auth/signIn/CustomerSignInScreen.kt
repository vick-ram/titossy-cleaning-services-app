package com.example.titossycleaningservicesapp.presentation.auth.signIn

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.viewmodel.CustomerViewModel
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomButton
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomTextField
import com.example.titossycleaningservicesapp.presentation.auth.utils.PassWordTransformation
import com.example.titossycleaningservicesapp.presentation.auth.utils.ValidationState
import com.example.titossycleaningservicesapp.presentation.utils.Authentication
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes
import com.example.titossycleaningservicesapp.presentation.utils.UserRoutes

@Composable
fun CustomerSignInScreen(
    toSignUpScreen: () -> Unit,
    navController: NavHostController,
    customerViewModel: CustomerViewModel,
    paddingValues: PaddingValues
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val passwordState by customerViewModel.passwordState.collectAsState()
    val passwordErrorMessage by customerViewModel.passwordErrorMessage.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(customerViewModel, context) {
        customerViewModel.authEvent.collect { result ->
            when (result) {
                is AuthEvent.Success -> {
                    val customerStatus = result.approvalStatus
                    when (customerStatus) {
                        ApprovalStatus.PENDING -> {
                            navController.navigate(Authentication.APPROVAL.route)
                        }

                        ApprovalStatus.APPROVED -> {
                            snackBarHostState.showSnackbar(result.message.toString())
                            navController.navigate(UserRoutes.Customer.route) {
                                popUpTo(RootNavRoutes.AUTH.route) {
                                    inclusive = true
                                }
                            }
                        }

                        ApprovalStatus.REJECTED -> {
                            snackBarHostState.showSnackbar("rejected")
                        }

                        null -> {}
                    }
                }

                is AuthEvent.Error -> {
                    Toast.makeText(
                        context,
                        result.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                AuthEvent.Loading -> customerViewModel.isLoading
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(state = rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 16.dp),
                painter = painterResource(id = R.drawable.titossy_logo),
                contentDescription = null
            )
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = customerViewModel.email,
                onValueChange = { newEmail ->
                    customerViewModel.onFieldChange(
                        CustomerViewModel.FieldType.EMAIL,
                        newEmail
                    )
                },
                label = "email",
                leadingIcon = Icons.Filled.Person,
                keyBoardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            CustomTextField(
                value = customerViewModel.password,
                onValueChange = { newPassword ->
                    customerViewModel.onFieldChange(
                        CustomerViewModel.FieldType.PASSWORD,
                        newPassword
                    )
                },
                modifier = Modifier.fillMaxWidth(),
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
                modifier = Modifier.fillMaxWidth(),
                text = "Sign In",
                onClick = { customerViewModel.signIn() },
                shape = MaterialTheme.shapes.extraLarge,
                enabled = passwordState is ValidationState.Valid
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    modifier = Modifier.padding(end = 2.dp)
                )
                TextButton(
                    onClick = toSignUpScreen
                ) {
                    Text(text = "Sign Up")
                }
            }

            BottomSection(
                modifier = Modifier.padding(start = 16.dp),
                navigateToSupplier = {
                    navController.navigate(Authentication.SUPPLIER.route)
                },
                navigateToEmployee = {
                    navController.navigate(Authentication.EMPLOYEE.route)
                }
            )
        }
        CustomProgressIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            isLoading = customerViewModel.isLoading
        )
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


@Composable
fun BottomSection(
    modifier: Modifier = Modifier,
    navigateToSupplier: () -> Unit,
    navigateToEmployee: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "Supplier?")
        TextButton(onClick = navigateToSupplier) {
            Text(text = "Sign In")
        }
    }
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "Employee/Staff?")
        TextButton(onClick = navigateToEmployee) {
            Text(text = "Sign In")
        }
    }
}
