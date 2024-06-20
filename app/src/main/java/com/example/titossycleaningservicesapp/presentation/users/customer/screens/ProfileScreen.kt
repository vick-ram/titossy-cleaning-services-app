package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import android.net.Uri
import android.view.ViewTreeObserver
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.domain.viewmodel.CustomerAuthViewModel
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomButton
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomTextField
import com.example.titossycleaningservicesapp.presentation.utils.UserRoutes

@Composable
fun SettingsScreen(navController: NavHostController, paddingValues: PaddingValues) {
    val viewModel: CustomerAuthViewModel = hiltViewModel()

    var enabled by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val imeState = rememberImeState()

    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.scrollTo(scrollState.maxValue)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .consumeWindowInsets(paddingValues)
            .padding(paddingValues)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        navController.popBackStack()
                        navController.navigate(UserRoutes.Customer.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }

                Text(text = "Edit Profile")
            }

            IconButton(
                onClick = { enabled = !enabled }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "edit"
                )
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            tonalElevation = 2.dp,
            shadowElevation = 1.dp
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )

                }

                CustomTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier,
                    label = "username",
                    leadingIcon = Icons.Filled.Person,
                    keyBoardOptions = KeyboardOptions(),
                    enabled = enabled
                )

                CustomTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier,
                    label = "username",
                    leadingIcon = Icons.Filled.Person,
                    keyBoardOptions = KeyboardOptions(),
                    enabled = enabled
                )

                CustomTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier,
                    label = "username",
                    leadingIcon = Icons.Filled.Person,
                    keyBoardOptions = KeyboardOptions(),
                    enabled = enabled
                )

                CustomTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier,
                    label = "username",
                    leadingIcon = Icons.Filled.Person,
                    keyBoardOptions = KeyboardOptions(),
                    enabled = enabled
                )

                CustomTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier,
                    label = "username",
                    leadingIcon = Icons.Filled.Person,
                    keyBoardOptions = KeyboardOptions(),
                    enabled = enabled
                )


                CustomTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier,
                    label = "username",
                    leadingIcon = Icons.Filled.Person,
                    keyBoardOptions = KeyboardOptions(),
                    enabled = enabled
                )
            }
        }

        CustomButton(
            text = "Update",
            onClick = { /*TODO*/ },
            modifier = Modifier,
            enabled = enabled
        )

    }
}


@Composable
fun rememberImeState(): State<Boolean> {
    val imeState = remember { mutableStateOf(false) }

    val view = LocalView.current
    DisposableEffect(key1 = view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
            imeState.value = isKeyboardOpen
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
    return imeState
}



















