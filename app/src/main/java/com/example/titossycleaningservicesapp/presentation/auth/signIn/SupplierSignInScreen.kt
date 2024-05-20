package com.example.titossycleaningservicesapp.presentation.auth.signIn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierAuthViewModel
import com.example.titossycleaningservicesapp.presentation.auth.utils.AuthCurve
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomButton
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomTextField
import com.example.titossycleaningservicesapp.presentation.auth.utils.PassWordTransformation
import com.example.titossycleaningservicesapp.presentation.auth.utils.ValidationState
import com.example.titossycleaningservicesapp.presentation.utils.UserRoutes

@Composable
fun SupplierSignInScreen(
    navController: NavHostController = rememberNavController(),
) {

    var passwordVisible by remember { mutableStateOf(false) }
    val supplierViewModel: SupplierAuthViewModel = hiltViewModel()
    val context = LocalContext.current

    var usernameOrEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val passwordState by supplierViewModel.passwordState.collectAsState()
    val usernameOrEmailState by supplierViewModel.usernameOrEmailState.collectAsState()
    val usernameOrEmailErrorMessage by supplierViewModel.usernameOrEmailErrorMessage.collectAsState()
    val passwordErrorMessage by supplierViewModel.passwordErrorMessage.collectAsState()
    val isUsernameOrEmailError = usernameOrEmailState is ValidationState.Invalid


    LaunchedEffect(supplierViewModel, context) {
        supplierViewModel.resultChannel.collect { result ->
            when (result) {
                is AuthEvent.Error -> TODO()
                is AuthEvent.Loading -> TODO()
                is AuthEvent.Success -> TODO()
            }
        }
    }



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
                    value = usernameOrEmail,
                    onValueChange = {
                        usernameOrEmail = it
                    },
                    modifier = Modifier,
                    label = "username or email",
                    leadingIcon = Icons.Filled.Person,
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    trailingIcon = if (isUsernameOrEmailError) Icons.Outlined.Info else null,
                    isError = isUsernameOrEmailError,
                    errorMessage = usernameOrEmailErrorMessage
                )

                CustomTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        supplierViewModel.onPasswordChange(it)
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
                    onClick = { navController.navigate(UserRoutes.Supplier.route) },
                    modifier = Modifier.padding(16.dp),
                    enabled = passwordState is ValidationState.Valid
                )
            }
        }
    }
}