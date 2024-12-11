package com.example.titossycleaningservicesapp.presentation.auth.signIn

import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.Roles
import com.example.titossycleaningservicesapp.domain.viewmodel.EmployeeViewModel
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomButton
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomTextField
import com.example.titossycleaningservicesapp.presentation.auth.utils.PassWordTransformation
import com.example.titossycleaningservicesapp.presentation.auth.utils.ValidationState
import com.example.titossycleaningservicesapp.presentation.utils.NavigationIcon
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes
import com.example.titossycleaningservicesapp.presentation.utils.UserRoutes

@Composable
fun EmployeesSignIn(navController: NavHostController, paddingValues: PaddingValues) {

    var togglePasswordVisibility by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val signInViewModel: EmployeeViewModel = hiltViewModel()

    val emailState by signInViewModel.emailState.collectAsStateWithLifecycle()
    val passwordState by signInViewModel.passwordState.collectAsStateWithLifecycle()

    val emailError by signInViewModel.emailError.collectAsStateWithLifecycle()
    val passwordError by signInViewModel.passwordError.collectAsStateWithLifecycle()

    LaunchedEffect(signInViewModel, context) {
        signInViewModel.resultChannel.collect { result ->
            when (result) {
                is AuthEvent.Error -> {
                    Toast.makeText(
                        context,
                        result.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

                is AuthEvent.Loading -> signInViewModel.isLoading
                is AuthEvent.Success -> {
                    val role = signInViewModel.employeeRole()
                    when (role) {
                        Roles.MANAGER.name -> {
                            navController.navigate(UserRoutes.Manager.route) {
                                popUpTo(RootNavRoutes.AUTH.route) {
                                    inclusive = true
                                }
                            }
                        }

                        Roles.FINANCE.name -> {
                            navController.navigate(UserRoutes.Finance.route) {
                                popUpTo(RootNavRoutes.AUTH.route) {
                                    inclusive = true
                                }
                            }
                        }

                        Roles.SUPERVISOR.name -> {
                            navController.navigate(UserRoutes.Supervisor.route) {
                                popUpTo(RootNavRoutes.AUTH.route) {
                                    inclusive = true
                                }
                            }
                        }

                        Roles.INVENTORY.name -> {
                            navController.navigate(UserRoutes.Inventory.route) {
                                popUpTo(RootNavRoutes.AUTH.route) {
                                    inclusive = true
                                }
                            }
                        }

                        Roles.CLEANER.name -> {
                            navController.navigate(UserRoutes.Cleaner.route) {
                                popUpTo(RootNavRoutes.AUTH.route) {
                                    inclusive = true
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationIcon(icon = Icons.Outlined.ChevronLeft) {
                navController.navigateUp()
            }
            Text(
                modifier = Modifier,
                text = "back",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Employee Login",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            CustomTextField(
                value = signInViewModel.email,
                onValueChange = {
                    signInViewModel.onFieldChange(
                        EmployeeViewModel.FieldType.EMAIL, it
                    )
                },
                modifier = Modifier,
                label = "email",
                leadingIcon = Icons.Filled.Person,
                keyBoardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                errorMessage = emailError,
                isError = emailState is ValidationState.Invalid
            )

            CustomTextField(
                value = signInViewModel.password,
                onValueChange = {
                    signInViewModel.onFieldChange(
                        EmployeeViewModel.FieldType.PASSWORD, it
                    )
                },
                modifier = Modifier,
                label = "Password",
                leadingIcon = Icons.Filled.Lock,
                keyBoardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = if (togglePasswordVisibility) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                },
                onTrailingIconClicked = {
                    togglePasswordVisibility = !togglePasswordVisibility
                },
                visualTransformation = if (togglePasswordVisibility) {
                    VisualTransformation.None
                } else {
                    PassWordTransformation()
                },
                errorMessage = passwordError,
                isError = passwordState is ValidationState.Invalid
            )
            Spacer(modifier = Modifier.height(8.dp))
            CustomButton(
                text = "Sign In",
                onClick = { signInViewModel.signIn() },
                modifier = Modifier.fillMaxWidth(),
                enabled = emailState is ValidationState.Valid && passwordState is ValidationState.Valid
            )
        }
        CustomProgressIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            isLoading = signInViewModel.isLoading
        )
    }
}
