package com.example.titossycleaningservicesapp.presentation.auth.signUp

import android.widget.Toast
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.core.rememberImeState
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierViewModel
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomButton
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomTextField
import com.example.titossycleaningservicesapp.presentation.auth.utils.PassWordTransformation
import com.example.titossycleaningservicesapp.presentation.auth.utils.ValidationState
import com.example.titossycleaningservicesapp.presentation.utils.Authentication
import com.example.titossycleaningservicesapp.presentation.utils.NavigationIcon

@Composable
fun SupplierSignUpScreen(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    val supplierViewModel: SupplierViewModel = hiltViewModel()
    val context = LocalContext.current
    var passwordVisibility by remember { mutableStateOf(false) }
    val scrollSate = rememberScrollState()
    val imeState = rememberImeState()

    val emailState by supplierViewModel.emailState.collectAsStateWithLifecycle()
    val phoneState by supplierViewModel.phoneState.collectAsStateWithLifecycle()
    val passwordState by supplierViewModel.passwordState.collectAsStateWithLifecycle()

    val emailErrorMessage by supplierViewModel.emailErrorMessage.collectAsStateWithLifecycle()
    val phoneErrorMessage by supplierViewModel.phoneErrorMessage.collectAsStateWithLifecycle()
    val passwordErrorMessage by supplierViewModel.passwordErrorMessage.collectAsStateWithLifecycle()

    val firstnameState by supplierViewModel.firstnameState.collectAsStateWithLifecycle()
    val lastnameState by supplierViewModel.lastnameState.collectAsStateWithLifecycle()
    val firstnameErrorMessage by supplierViewModel.firstnameErrorMessage.collectAsStateWithLifecycle()
    val lastnameErrorMessage by supplierViewModel.lastnameErrorMessage.collectAsStateWithLifecycle()
    val addressState by supplierViewModel.addressState.collectAsStateWithLifecycle()
    val addressErrorMessage by supplierViewModel.addressErrorMessage.collectAsStateWithLifecycle()


    LaunchedEffect(imeState) {
        if (imeState.value) {
            scrollSate.animateScrollTo(
                scrollSate.maxValue,
                animationSpec = tween(300)
            )
        }
    }

    LaunchedEffect(supplierViewModel, context) {
        supplierViewModel.resultChannel.collect { result ->
            when (result) {
                is AuthEvent.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                }

                is AuthEvent.Loading -> supplierViewModel.isLoading

                is AuthEvent.Success -> {

                    navController.navigate(Authentication.SUPPLIER.route)
                }
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
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavigationIcon(icon = Icons.Outlined.ChevronLeft) {
                    navController.navigateUp()
                }
                Text(
                    text = "back",
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(state = scrollSate),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomTextField(
                    value = supplierViewModel.firstName,
                    onValueChange = {
                        supplierViewModel.onFieldChange(
                            SupplierViewModel.FieldType.FIRST_NAME, it
                        )
                    },
                    modifier = Modifier,
                    label = "First Name",
                    leadingIcon = Icons.Filled.Person,
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    errorMessage = firstnameErrorMessage,
                    isError = firstnameState is ValidationState.Invalid
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomTextField(
                    value = supplierViewModel.lastName,
                    onValueChange = {
                        supplierViewModel.onFieldChange(
                            SupplierViewModel.FieldType.LAST_NAME, it
                        )
                    },
                    modifier = Modifier,
                    label = "Last Name",
                    leadingIcon = Icons.Filled.Person,
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    errorMessage = lastnameErrorMessage,
                    isError = lastnameState is ValidationState.Invalid
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomTextField(
                    value = supplierViewModel.phone,
                    onValueChange = {
                        supplierViewModel.onFieldChange(
                            SupplierViewModel.FieldType.PHONE, it
                        )
                    },
                    modifier = Modifier,
                    label = "Phone",
                    leadingIcon = Icons.Filled.Phone,
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    errorMessage = phoneErrorMessage,
                    isError = phoneState is ValidationState.Invalid
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomTextField(
                    value = supplierViewModel.address,
                    onValueChange = {
                        supplierViewModel.onFieldChange(
                            SupplierViewModel.FieldType.ADDRESS, it
                        )
                    },
                    modifier = Modifier,
                    label = "Address",
                    leadingIcon = Icons.Filled.LocationOn,
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    errorMessage = addressErrorMessage,
                    isError = addressState is ValidationState.Invalid
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomTextField(
                    value = supplierViewModel.email,
                    onValueChange = {
                        supplierViewModel.onFieldChange(
                            SupplierViewModel.FieldType.EMAIL, it
                        )
                    },
                    modifier = Modifier,
                    label = "Email",
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
                    trailingIcon = if (passwordVisibility) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    },
                    onTrailingIconClicked = { passwordVisibility = !passwordVisibility },
                    visualTransformation = if (passwordVisibility) {
                        VisualTransformation.None
                    } else {
                        PassWordTransformation()
                    },
                    errorMessage = passwordErrorMessage,
                    isError = passwordState is ValidationState.Invalid
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomButton(
                    text = "Sign Up",
                    onClick = { supplierViewModel.signUp() },
                    modifier = Modifier.fillMaxWidth(.8f),
                    shape = MaterialTheme.shapes.medium,
                    enabled = firstnameState is ValidationState.Valid &&
                            lastnameState is ValidationState.Valid &&
                            phoneState is ValidationState.Valid &&
                            emailState is ValidationState.Valid &&
                            passwordState is ValidationState.Valid &&
                            addressState is ValidationState.Valid
                )
            }
        }
        CustomProgressIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            isLoading = supplierViewModel.isLoading
        )
    }
}

