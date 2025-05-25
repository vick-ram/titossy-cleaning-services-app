package com.example.titossycleaningservicesapp.presentation.auth.signUp

import android.widget.Toast
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.ChevronLeft
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
import com.example.titossycleaningservicesapp.core.rememberImeState
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.viewmodel.CustomerViewModel
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomButton
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomTextField
import com.example.titossycleaningservicesapp.presentation.auth.utils.PassWordTransformation
import com.example.titossycleaningservicesapp.presentation.auth.utils.ValidationState
import com.example.titossycleaningservicesapp.presentation.utils.NavigationIcon

@Composable
fun CustomerSignUpScreen(
    backToLogin: () -> Unit,
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    var passwordVisibility by remember { mutableStateOf(false) }
    val signUpViewModel: CustomerViewModel = hiltViewModel()
    val context = LocalContext.current

    val passwordState by signUpViewModel.passwordState.collectAsStateWithLifecycle()
    val passwordErrorMessage by signUpViewModel.passwordErrorMessage.collectAsStateWithLifecycle()
    val emailState by signUpViewModel.emailState.collectAsStateWithLifecycle()
    val emailErrorMessage by signUpViewModel.emailErrorMessage.collectAsStateWithLifecycle()
    val phoneState by signUpViewModel.phoneState.collectAsStateWithLifecycle()
    val phoneErrorMessage by signUpViewModel.phoneErrorMessage.collectAsStateWithLifecycle()

    val firstNameState by signUpViewModel.firstnameState.collectAsStateWithLifecycle()
    val lastNameState by signUpViewModel.lastnameState.collectAsStateWithLifecycle()

    val firstNameErrorMessage by signUpViewModel.firstnameErrorMessage.collectAsStateWithLifecycle()
    val lastNameErrorMessage by signUpViewModel.lastnameErrorMessage.collectAsStateWithLifecycle()

    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(
                scrollState.maxValue,
                animationSpec = tween(300)
            )
        }
    }

    LaunchedEffect(signUpViewModel, context) {
        signUpViewModel.authEvent.collect { event ->
            when (event) {
                is AuthEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is AuthEvent.Loading -> signUpViewModel.isLoading

                is AuthEvent.Success -> backToLogin()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavigationIcon(
                    icon = Icons.Rounded.ChevronLeft,
                    onClick = { navController.navigateUp() }
                )
                Text(
                    text = "back",
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(state = scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 8.dp),
                    painter = painterResource(id = R.drawable.titossy_logo),
                    contentDescription = null
                )
                Text(
                    text = "Sign Up",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomTextField(
                    value = signUpViewModel.firstName,
                    onValueChange = {
                        signUpViewModel.onFieldChange(
                            CustomerViewModel.FieldType.FIRST_NAME, it
                        )
                    },
                    modifier = Modifier,
                    label = "First Name",
                    leadingIcon = Icons.Filled.Person,
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    errorMessage = firstNameErrorMessage,
                    isError = firstNameState is ValidationState.Invalid
                )
                Spacer(modifier = Modifier.height(4.dp))
                CustomTextField(
                    value = signUpViewModel.lastName,
                    onValueChange = {
                        signUpViewModel.onFieldChange(
                            CustomerViewModel.FieldType.LAST_NAME, it
                        )
                    },
                    modifier = Modifier,
                    label = "Last Name",
                    leadingIcon = Icons.Filled.Person,
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    errorMessage = lastNameErrorMessage,
                    isError = lastNameState is ValidationState.Invalid
                )
                Spacer(modifier = Modifier.height(4.dp))
                CustomTextField(
                    value = signUpViewModel.phone,
                    onValueChange = { newPhone ->
                        signUpViewModel.onFieldChange(
                            CustomerViewModel.FieldType.PHONE,
                            newPhone
                        )
                    },
                    modifier = Modifier,
                    label = "Phone",
                    leadingIcon = Icons.Filled.Call,
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    errorMessage = phoneErrorMessage,
                    isError = phoneState is ValidationState.Invalid
                )
                Spacer(modifier = Modifier.height(4.dp))
                CustomTextField(
                    value = signUpViewModel.email,
                    onValueChange = { newEmail ->
                        signUpViewModel.onFieldChange(
                            CustomerViewModel.FieldType.EMAIL,
                            newEmail
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
                Spacer(modifier = Modifier.height(4.dp))
                CustomTextField(
                    value = signUpViewModel.password,
                    onValueChange = { newPassword ->
                        signUpViewModel.onFieldChange(
                            CustomerViewModel.FieldType.PASSWORD,
                            newPassword
                        )
                    },
                    modifier = Modifier,
                    label = "Password",
                    leadingIcon = Icons.Filled.Lock,
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
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
                    isError = passwordState is ValidationState.Invalid,
                    errorMessage = passwordErrorMessage,
                )
                Spacer(modifier = Modifier.height(4.dp))
                CustomButton(
                    text = "Sign Up",
                    onClick = { signUpViewModel.signUp() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    enabled = firstNameState is ValidationState.Valid &&
                            lastNameState is ValidationState.Valid &&
                            phoneState is ValidationState.Valid &&
                            emailState is ValidationState.Valid &&
                            passwordState is ValidationState.Valid
                )
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
