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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierAuthViewModel
import com.example.titossycleaningservicesapp.presentation.auth.utils.AuthCurve
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomButton
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomTextField
import com.example.titossycleaningservicesapp.presentation.auth.utils.PassWordTransformation
import com.example.titossycleaningservicesapp.presentation.utils.Authentication
import com.example.titossycleaningservicesapp.presentation.utils.CustomIndeterminateProgressIndicator

@Composable
fun SupplierSignInScreen(
    navController: NavHostController,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val supplierViewModel: SupplierAuthViewModel = hiltViewModel()
    val context = LocalContext.current
    val snackBarHostState = remember{ SnackbarHostState()}

    LaunchedEffect(supplierViewModel, context) {
        supplierViewModel.resultChannel.collect { result ->
            when (result) {
                is AuthEvent.Error -> {}
                is AuthEvent.Loading -> supplierViewModel.isLoading
                is AuthEvent.Success -> {
                    when(result.approvalStatus) {
                        ApprovalStatus.PENDING -> {
                            /*snackBarHostState.showSnackbar("Your account is under review")
                            navController.navigate(Authentication.APPROVAL.route)
                            supplierViewModel.clearToken()*/
                        }
                        ApprovalStatus.APPROVED -> {
                            /*snackBarHostState.showSnackbar(result.message.toString())
                            navController.popBackStack()
                            navController.navigate(UserRoutes.Supplier.route)*/
                        }
                        ApprovalStatus.REJECTED -> {}
                        null -> {}
                    }
                }
            }
        }
    }


    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.nav_back),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "supplier",
                    color = MaterialTheme.colorScheme.onSurface
                )
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
                        value = supplierViewModel.email,
                        onValueChange = { supplierViewModel.onEmailChange(it) },
                        modifier = Modifier,
                        label = "email",
                        leadingIcon = Icons.Filled.Person,
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    CustomTextField(
                        value = supplierViewModel.password,
                        onValueChange = { supplierViewModel.onPasswordChange(it) },
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
                        }
                    )

                    CustomButton(
                        text = "Sign In",
                        onClick = {
                            supplierViewModel.signIn()
                        },
                        modifier = Modifier.padding(16.dp),
                        enabled = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(
                    onClick = {
                        navController.navigate(Authentication.SUPPLIER_SIGNUP.route)
                    }
                ) {
                    Text(
                        text = "Sign up",
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        CustomIndeterminateProgressIndicator(isLoading = supplierViewModel.isLoading)
    }
}