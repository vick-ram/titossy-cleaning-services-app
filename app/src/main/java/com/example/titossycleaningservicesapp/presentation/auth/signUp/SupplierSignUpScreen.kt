package com.example.titossycleaningservicesapp.presentation.auth.signUp

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocalPostOffice
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.titossycleaningservicesapp.presentation.auth.utils.AuthCurve
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomButton
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomTextField
import com.example.titossycleaningservicesapp.presentation.auth.utils.PassWordTransformation

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SupplierSignUpScreen() {
    /*val signUpViewModel: SupplierAuthViewModel = hiltViewModel()
    val context = LocalContext.current*/

    var passwordVisibility by remember { mutableStateOf(false) }

    /*    val passwordState by signUpViewModel.passwordState.collectAsState()
        val emailState by signUpViewModel.usernameOrEmailState.collectAsState()
        val emailErrorMessage by signUpViewModel.emailErrorMessage.collectAsState()
        val passwordErrorMessage by signUpViewModel.passwordErrorMessage.collectAsState()
        val phoneState by signUpViewModel.phoneState.collectAsState()
        val phoneErrorMessage by signUpViewModel.phoneErrorMessage.collectAsState()*/


    /*LaunchedEffect(signUpViewModel, context) {
        signUpViewModel.resultChannel.collect { result ->
            when (result) {
                is AuthState.Authenticated -> TODO()
                is AuthState.PendingApproval -> TODO()
                is AuthState.UnAuthenticated -> TODO()
                is AuthState.UnKnownError -> TODO()
            }
        }
    }*/

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = {
                //navController.navigate(RootNavRoutes.AUTH.route)
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = null
                )
            }

            Text(
                text = "Back to login",
                color = MaterialTheme.colorScheme.primary
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp, top = 18.dp, bottom = 0.dp),
            tonalElevation = 4.dp,
            shadowElevation = 1.dp,
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(
                        enabled = true,
                        state = rememberScrollState()
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AuthCurve(title = "Register")

                Spacer(modifier = Modifier.height(8.dp))

                CustomTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier,
                    label = "Username",
                    leadingIcon = Icons.Filled.Person,
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                CustomTextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier,
                    label = "First Name",
                    leadingIcon = Icons.Filled.Person,
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                CustomTextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier,
                    label = "Last Name",
                    leadingIcon = Icons.Filled.Person,
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    /*isError = signUpViewModel.email.trim().isEmpty(),
                    errorMessage = "required*",
                    trailingIcon = if (signUpViewModel.email.trim()
                            .isNotEmpty()
                    ) Icons.Outlined.Info else null*/
                )

                CustomTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier,
                    label = "Phone",
                    leadingIcon = Icons.Filled.Phone,
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    /*errorMessage = phoneErrorMessage,
                    isError = phoneState is ValidationState.Invalid,
                    trailingIcon = if (phoneState is ValidationState.Valid) Icons.Outlined.Info else null*/
                )

                CustomTextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier,
                    label = "Company",
                    leadingIcon = Icons.Filled.Business,
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                CustomTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier,
                    label = "County",
                    leadingIcon = Icons.Filled.LocationCity,
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                CustomTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier,
                    label = "Region",
                    leadingIcon = Icons.Filled.Place,
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                CustomTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier,
                    label = "Postal Code",
                    leadingIcon = Icons.Filled.LocalPostOffice,
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                CustomTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier,
                    label = "Email",
                    leadingIcon = Icons.Filled.Email,
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    errorMessage = "",
                    /*isError = emailState is ValidationState.Invalid,
                    trailingIcon = if (emailState is ValidationState.Valid) Icons.Outlined.Info else null*/
                )

                CustomTextField(
                    value = "",
                    onValueChange = {},
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
                    visualTransformation = if (passwordVisibility) {
                        VisualTransformation.None
                    } else {
                        PassWordTransformation()
                    },
                    /*isError = passwordState is ValidationState.Invalid,
                    errorMessage = passwordErrorMessage,*/
                )

                CustomButton(
                    text = "Register",
                    onClick = { /*TODO*/ },
                    modifier = Modifier,
                    enabled = true/*passwordState is ValidationState.Valid
                            && emailState is ValidationState.Valid
                            && phoneState is ValidationState.Valid*/
                )
            }
        }
    }

}

