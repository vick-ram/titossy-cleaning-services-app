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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.core.rememberImeState
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierAuthViewModel
import com.example.titossycleaningservicesapp.presentation.auth.utils.AuthCurve
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomButton
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomTextField
import com.example.titossycleaningservicesapp.presentation.auth.utils.PassWordTransformation
import com.example.titossycleaningservicesapp.presentation.utils.Authentication
import com.example.titossycleaningservicesapp.presentation.utils.NavigationIcon

@Composable
fun SupplierSignUpScreen(
    navController: NavHostController
) {
    val signUpViewModel: SupplierAuthViewModel = hiltViewModel()
    val context = LocalContext.current
    var passwordVisibility by remember { mutableStateOf(false) }
    val scrollSate = rememberScrollState()
    val imeState = rememberImeState()

    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }


    LaunchedEffect(imeState) {
        if (imeState.value) {
            scrollSate.animateScrollTo(
                scrollSate.maxValue,
                animationSpec = tween(300)
            )
        }
    }

    LaunchedEffect(signUpViewModel, context) {
        signUpViewModel.resultChannel.collect { result ->
            when (result) {
                is AuthEvent.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                }

                is AuthEvent.Loading -> signUpViewModel.isLoading

                is AuthEvent.Success -> {

                    navController.navigate(Authentication.SUPPLIER.route)
                }
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                NavigationIcon(icon = Icons.Outlined.ChevronLeft) {
                    navController.navigateUp()
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollSate)
                    .padding(18.dp),
                tonalElevation = 0.dp,
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
                        value = firstName,
                        onValueChange = { firstName = it },
                        modifier = Modifier,
                        label = "First Name",
                        leadingIcon = Icons.Filled.Person,
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    CustomTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        modifier = Modifier,
                        label = "Last Name",
                        leadingIcon = Icons.Filled.Person,
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    CustomTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        modifier = Modifier,
                        label = "Phone",
                        leadingIcon = Icons.Filled.Phone,
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    CustomTextField(
                        value = address,
                        onValueChange = { address = it },
                        modifier = Modifier,
                        label = "Address",
                        leadingIcon = Icons.Filled.LocationOn,
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    CustomTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier,
                        label = "Email",
                        leadingIcon = Icons.Filled.Email,
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    CustomTextField(
                        value = password,
                        onValueChange = { password = it },
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
                        onTrailingIconClicked = {passwordVisibility = !passwordVisibility},
                        visualTransformation = if (passwordVisibility) {
                            VisualTransformation.None
                        } else {
                            PassWordTransformation()
                        }
                    )

                    CustomButton(
                        text = "Sign Up",
                        onClick = {
                            signUpViewModel.signUp(
                                firstName, lastName, phone, address, email, password
                            )
                        },
                        modifier = Modifier,
                        enabled = true
                    )
                }
            }
        }
        CustomProgressIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            isLoading = signUpViewModel.isLoading
        )
    }
}

