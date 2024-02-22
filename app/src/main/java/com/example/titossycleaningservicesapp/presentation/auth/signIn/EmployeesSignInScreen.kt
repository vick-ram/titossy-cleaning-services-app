package com.example.titossycleaningservicesapp.presentation.auth.signIn

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.domain.viewmodel.AuthViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.FirebaseViewModel
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomButton
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomTextField
import com.example.titossycleaningservicesapp.presentation.utils.Authentication
import com.example.titossycleaningservicesapp.presentation.utils.CustomIndeterminateProgressIndicator
import com.example.titossycleaningservicesapp.presentation.utils.UserRoutes
import kotlinx.coroutines.launch

@Composable
fun EmployeesSignIn(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var togglePasswordVisibility by remember { mutableStateOf(false) }
    val employeeViewModel: FirebaseViewModel = hiltViewModel()
    val employeeData by employeeViewModel.employeeData.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val signInViewModel: AuthViewModel = hiltViewModel()

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
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(72.dp),
                    imageVector = Icons.Filled.Person,
                    contentDescription = "employee account",
                    tint = Color.Blue
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier,
                label = "email",
                leadingIcon = Icons.Filled.Email,
                keyBoardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                )
            )
            CustomTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier,
                label = "password",
                leadingIcon = Icons.Filled.Lock,
                trailingIcon = if (togglePasswordVisibility) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                onTrailingIconClicked = { togglePasswordVisibility = !togglePasswordVisibility },
                visualTransformation = if (togglePasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyBoardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Password
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomButton(
                text = "sign in",
                onClick = {
                    scope.launch {
                        signInViewModel.signIn(email, password)
                        employeeViewModel.getEmployee()
                        email = ""
                        password = ""
                    }

                },
                modifier = Modifier
            )
            TextButton(
                onClick = { navController.popBackStack() }
            ) {
                Text(text = "Back")
            }
            CustomIndeterminateProgressIndicator(isLoading = employeeData.isLoading)
        }
    }

    LaunchedEffect(key1 = employeeData.employees, block = {
        scope.launch {
            employeeData.employee?.let { employee ->
                when {
                    employee.role == "manager" && employee.status == "approved" -> {
                        navController.navigate(UserRoutes.Manager.route) {
                            popUpTo(Authentication.EMPLOYEE.route) {
                                inclusive = true
                            }
                        }
                    }

                    employee.role == "finance" && employee.status == "approved" -> {
                        navController.navigate(UserRoutes.Finance.route) {
                            popUpTo(Authentication.EMPLOYEE.route) {
                                inclusive = true
                            }
                        }
                    }

                    employee.role == "supplier" && employee.status == "approved" -> {
                        navController.navigate(UserRoutes.Supplier.route) {
                            popUpTo(Authentication.EMPLOYEE.route) {
                                inclusive = true
                            }
                        }
                    }

                    employee.role == "supervisor" && employee.status == "approved" -> {
                        navController.navigate(UserRoutes.Supervisor.route) {
                            popUpTo(Authentication.EMPLOYEE.route) {
                                inclusive = true
                            }
                        }
                    }

                    employee.role == "inventory" && employee.status == "approved" -> {
                        navController.navigate(UserRoutes.Customer.route) {
                            popUpTo(Authentication.EMPLOYEE.route) {
                                inclusive = true
                            }
                        }
                    }

                    employee.role == "cleaner" && employee.status == "approved" -> {
                        navController.navigate(UserRoutes.Cleaner.route) {
                            popUpTo(Authentication.EMPLOYEE.route) {
                                inclusive = true
                            }
                        }
                    }

                    else -> {
                        navController.navigate(UserRoutes.Customer.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
        }
    })

    LaunchedEffect(key1 = employeeData.errorMessage, block = {
        scope.launch {
            if (employeeData.errorMessage?.isNotEmpty() == true) {
                Toast.makeText(context, "${employeeData.errorMessage}", Toast.LENGTH_LONG).show()
            }
        }
    })

    LaunchedEffect(key1 = employeeData.isLoading, block = {
        scope.launch {
            if (employeeData.isLoading) {
                Toast.makeText(context, "loading...", Toast.LENGTH_LONG).show()
            }
        }
    })
}
