package com.example.titossycleaningservicesapp.presentation.auth.signIn

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.core.showToast
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierViewModel
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomButton
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomTextField
import com.example.titossycleaningservicesapp.presentation.auth.utils.PassWordTransformation
import com.example.titossycleaningservicesapp.presentation.auth.utils.ValidationState
import com.example.titossycleaningservicesapp.presentation.utils.Authentication
import com.example.titossycleaningservicesapp.presentation.utils.NavigationIcon
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes
import com.example.titossycleaningservicesapp.presentation.utils.UserRoutes

@Composable
fun SupplierSignInScreen(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val supplierViewModel: SupplierViewModel = hiltViewModel()
    val supplierUiState by supplierViewModel.supplierUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    val emailState by supplierViewModel.emailState.collectAsStateWithLifecycle()
    val passwordState by supplierViewModel.passwordState.collectAsStateWithLifecycle()

    val emailErrorMessage by supplierViewModel.emailErrorMessage.collectAsStateWithLifecycle()
    val passwordErrorMessage by supplierViewModel.passwordErrorMessage.collectAsStateWithLifecycle()

    LaunchedEffect(supplierUiState, supplierViewModel) {
        supplierViewModel.fetchSuppliers()
    }

    LaunchedEffect(supplierViewModel, context) {
        supplierViewModel.resultChannel.collect { result ->
            when (result) {
                is AuthEvent.Error -> {
                    showToast(
                        context = context,
                        message = result.message
                    )
                    supplierViewModel.clearToken()
                }

                is AuthEvent.Loading -> supplierViewModel.isLoading
                is AuthEvent.Success -> {
                    when (result.approvalStatus) {
                        ApprovalStatus.PENDING -> {
                            navController.navigate(Authentication.SUPPLIER_APPROVAL.route)
                        }

                        ApprovalStatus.APPROVED -> {
                            snackBarHostState.showSnackbar(result.message.toString())
                            navController.navigate(UserRoutes.Supplier.route) {
                                popUpTo(RootNavRoutes.AUTH.route) {
                                    inclusive = true
                                }
                            }
                        }

                        ApprovalStatus.REJECTED -> {}
                        null -> {
                            supplierViewModel.clearToken()
                        }
                    }
                }
            }
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationIcon(
                icon = Icons.Rounded.ChevronLeft,
                onClick = { navController.navigateUp() })
            Text(
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
            Image(
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 8.dp),
                painter = painterResource(id = R.drawable.titossy_logo),
                contentDescription = null
            )
            Text(
                text = "Sign In",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            CustomTextField(
                value = supplierViewModel.email,
                onValueChange = {
                    supplierViewModel.onFieldChange(
                        SupplierViewModel.FieldType.EMAIL, it
                    )
                },
                modifier = Modifier,
                label = "email",
                leadingIcon = Icons.Filled.Email,
                keyBoardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                errorMessage = emailErrorMessage,
                isError = emailState is ValidationState.Invalid
            )
            Spacer(modifier = Modifier.height(8.dp))
            CustomTextField(
                value = supplierViewModel.password,
                onValueChange = {
                    supplierViewModel.onFieldChange(
                        SupplierViewModel.FieldType.PASSWORD, it
                    )
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
                isError = passwordState is ValidationState.Invalid
            )
            Spacer(modifier = Modifier.height(8.dp))
            CustomButton(
                text = "Sign In",
                onClick = { supplierViewModel.signIn() },
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = emailState is ValidationState.Valid && passwordState is ValidationState.Valid
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(2.dp))
                TextButton(
                    onClick = {
                        navController.navigate(Authentication.SUPPLIER_SIGNUP.route)
                    }
                ) {
                    Text(
                        text = "Sign Up",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        CustomProgressIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            isLoading = supplierViewModel.isLoading
        )
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}