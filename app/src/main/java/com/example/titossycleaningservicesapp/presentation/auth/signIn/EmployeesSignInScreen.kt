package com.example.titossycleaningservicesapp.presentation.auth.signIn

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.Roles
import com.example.titossycleaningservicesapp.domain.viewmodel.EmployeeViewModel
import com.example.titossycleaningservicesapp.presentation.auth.utils.AuthCurve
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomButton
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomTextField
import com.example.titossycleaningservicesapp.presentation.auth.utils.PassWordTransformation
import com.example.titossycleaningservicesapp.presentation.utils.CustomIndeterminateProgressIndicator
import com.example.titossycleaningservicesapp.presentation.utils.UserRoutes

@Composable
fun EmployeesSignIn(navController: NavHostController) {

    var togglePasswordVisibility by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val signInViewModel: EmployeeViewModel = hiltViewModel()

    LaunchedEffect(signInViewModel, context) {
        signInViewModel.resultChannel.collect { result ->
            when (result) {
                is AuthEvent.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                }

                is AuthEvent.Loading -> signInViewModel.isLoading
                is AuthEvent.Success -> {
                    val role = signInViewModel.employeeRole()
                    when (role) {
                        Roles.MANAGER.name -> {
                            navController.popBackStack()
                            navController.navigate(UserRoutes.Manager.route)
                        }

                        Roles.FINANCE.name -> {
                            navController.popBackStack()
                            navController.navigate(UserRoutes.Finance.route)
                        }

                        Roles.SUPERVISOR.name -> {
                            navController.popBackStack()
                            navController.navigate(UserRoutes.Supervisor.route)
                        }

                        Roles.INVENTORY.name -> {
                            navController.popBackStack()
                            navController.navigate(UserRoutes.Inventory.route)
                        }

                        Roles.CLEANER.name -> {
                            navController.popBackStack()
                            navController.navigate(UserRoutes.Cleaner.route)
                        }

                        else -> {
                            Toast.makeText(context, "Unauthorized", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .padding(top = 8.dp, start = 8.dp),
                        painter = painterResource(id = R.drawable.baseline_chevron_left_24),
                        contentDescription = null
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
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
                        onValueChange = { signInViewModel.onEmailChange(it) },
                        modifier = Modifier,
                        label = "username or email",
                        leadingIcon = Icons.Filled.Person,
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    CustomTextField(
                        value = signInViewModel.password,
                        onValueChange = { signInViewModel.onPasswordChange(it) },
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
                        }
                    )

                    CustomButton(
                        text = "Sign In",
                        onClick = {
                            signInViewModel.signIn()
                        },
                        modifier = Modifier.padding(16.dp),
                        enabled = true
                    )
                }
            }
        }
        CustomIndeterminateProgressIndicator(isLoading = signInViewModel.isLoading)
    }
}
