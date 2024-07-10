package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.core.splitFullName
import com.example.titossycleaningservicesapp.domain.models.ui_models.Customer
import com.example.titossycleaningservicesapp.domain.viewmodel.CustomerViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    val mainViewModel: MainViewModel = hiltViewModel()
    var customerId by rememberSaveable { mutableStateOf<String?>(null) }
    val viewModel: CustomerViewModel = hiltViewModel()
    val customerState by viewModel.customerUiState.collectAsStateWithLifecycle()
    val customer = customerState.customers?.find { it.id.toString() == customerId }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var showDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = customerId) {
        customerId = mainViewModel.readUserId()
    }

    when {
        customerState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CustomProgressIndicator(isLoading = true)
            }
        }

        customerState.customers != null -> {
            customer?.let { cust ->
                CustomerSettings(
                    customer = cust,
                    paddingValues = paddingValues,
                    showDialog = showDialog,
                    onShowDialog = { showDialog = it },
                    username = viewModel.username,
                    firstName = viewModel.firstName,
                    lastName = viewModel.lastName,
                    email = viewModel.email,
                    password = viewModel.password,
                    splitFullName = splitFullName(cust.fullName),
                    onValueChange = { field, value ->
                        when (field) {
                            "username" -> viewModel.username = value
                            "firstName" -> viewModel.firstName = value
                            "lastName" -> viewModel.lastName = value
                            "email" -> viewModel.email = value
                            "password" -> viewModel.password = value
                        }
                    },
                    focusRequester = focusRequester,
                    focusManager = focusManager,
                    onSubmit = {
                        viewModel.update(
                            customerId = cust.id
                        )
                    }
                )
            }
        }

        customerState.error != null -> {
            Toast.makeText(
                context,
                customerState.error,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}


@Composable
fun CustomerSettings(
    customer: Customer,
    paddingValues: PaddingValues,
    showDialog: Boolean = false,
    onShowDialog: (Boolean) -> Unit,
    username: String,
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    splitFullName: Pair<String, String>,
    onValueChange: (String, String) -> Unit,
    focusRequester: FocusRequester = FocusRequester.Default,
    focusManager: FocusManager = LocalFocusManager.current,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.user),
                contentDescription = null
            )

            ProfileItem(
                title = "Username",
                value = customer.username,
            )

            ProfileItem(
                title = "Name",
                value = customer.fullName,
            )
            ProfileItem(
                title = "Email",
                value = customer.email
            )
            ProfileItem(
                title = "Phone",
                value = customer.phone
            )

            OutlinedButton(
                modifier = Modifier.align(Alignment.End),
                onClick = { onShowDialog(true) }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null
                )
                Text(text = "Edit Profile")
            }
        }
    }

    if (showDialog) {
        ProfileDialog(
            onShowDialog = onShowDialog,
            customer = customer,
            username = username,
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            splitFullName = splitFullName,
            onValueChange = onValueChange,
            focusRequester = focusRequester,
            focusManager = focusManager,
            onSubmit = onSubmit
        )
    }

}

@Composable
fun ProfileItem(
    title: String,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        TextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )
        )
    }
}

@Composable
fun ProfileDialog(
    modifier: Modifier = Modifier,
    onShowDialog: (Boolean) -> Unit,
    customer: Customer,
    username: String,
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    splitFullName: Pair<String, String>,
    onValueChange: (String, String) -> Unit,
    focusRequester: FocusRequester = FocusRequester.Default,
    focusManager: FocusManager = LocalFocusManager.current,
    onSubmit: () -> Unit
) {
    Dialog(
        onDismissRequest = { onShowDialog(false) }
    ) {
        ProfileFields(
            modifier = modifier,
            customer = customer,
            username = username,
            firstName = firstName,
            lastName = lastName,
            email = email,
            splitFullName = splitFullName,
            onValueChange = onValueChange,
            focusRequester = focusRequester,
            focusManager = focusManager,
            password = password,
            onSubmit = onSubmit
        )
    }
}

@Composable
fun ProfileFields(
    modifier: Modifier = Modifier,
    customer: Customer,
    username: String,
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    splitFullName: Pair<String, String>,
    onValueChange: (String, String) -> Unit,
    focusRequester: FocusRequester = FocusRequester.Default,
    focusManager: FocusManager = LocalFocusManager.current,
    onSubmit: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(Color.LightGray)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Update your Account",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W600,
                    fontSize = 20.sp
                )
            )
            CustomProfileTextField(
                modifier = modifier,
                value = username,
                placeholder = customer.username,
                icon = Icons.Rounded.AccountCircle,
                onValueChange = { onValueChange("username", it) },
                focusRequester = focusRequester,
                focusManager = focusManager
            )
            CustomProfileTextField(
                modifier = modifier,
                value = firstName,
                placeholder = splitFullName.first,
                icon = Icons.Rounded.Person,
                onValueChange = { onValueChange("firstName", it) },
                focusRequester = focusRequester,
                focusManager = focusManager
            )
            CustomProfileTextField(
                modifier = modifier,
                value = lastName,
                placeholder = splitFullName.second,
                icon = Icons.Rounded.Person,
                onValueChange = { onValueChange("lastName", it) },
                focusRequester = focusRequester,
                focusManager = focusManager
            )
            CustomProfileTextField(
                modifier = modifier,
                value = email,
                placeholder = customer.email,
                icon = Icons.Rounded.Email,
                onValueChange = { onValueChange("email", it) },
                focusRequester = focusRequester,
                focusManager = focusManager
            )
            CustomProfileTextField(
                modifier = modifier,
                value = password,
                placeholder = "Password",
                icon = Icons.Rounded.Lock,
                onValueChange = { onValueChange("password", it) },
                focusRequester = focusRequester,
                focusManager = focusManager
            )
            Button(
                modifier = modifier
                    .fillMaxWidth(.7f)
                    .align(Alignment.CenterHorizontally),
                onClick = onSubmit,
                contentPadding = PaddingValues(12.dp)
            ) {
                Text(text = "Update")
            }
        }
    }
}

@Composable
fun CustomProfileTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    icon: ImageVector,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester = FocusRequester.Default,
    focusManager: FocusManager = LocalFocusManager.current
) {
    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(MaterialTheme.shapes.small)
            .background(
                MaterialTheme.colorScheme
                    .surface, MaterialTheme.shapes.small
            )
            .focusRequester(focusRequester),
        value = value,
        onValueChange = onValueChange,
        decorationBox = { innerTextField ->
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = modifier
                        .size(32.dp)
                        .padding(start = 4.dp, end = 8.dp),
                    imageVector = icon,
                    contentDescription = null
                )
                Box(modifier = modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                            )
                        )
                    }
                    innerTextField()
                }
                if (value.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            if (value.isNotEmpty()) {
                                onValueChange.invoke("")
                                focusManager.clearFocus()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null
                        )
                    }
                }

            }
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        )
    )
}

