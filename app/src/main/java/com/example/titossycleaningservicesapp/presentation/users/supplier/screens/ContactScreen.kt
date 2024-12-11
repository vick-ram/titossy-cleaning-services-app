package com.example.titossycleaningservicesapp.presentation.users.supplier.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.core.showToast
import com.example.titossycleaningservicesapp.domain.models.ui_models.ChatMessage
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierViewModel

@Composable
fun ContactScreen(navController: NavHostController, paddingValues: PaddingValues) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var userId by rememberSaveable { mutableStateOf<String?>(null) }
    val mainViewModel = hiltViewModel<MainViewModel>()
    val supplierViewModel = hiltViewModel<SupplierViewModel>()

    val messageUiState by supplierViewModel.messageUiState.collectAsStateWithLifecycle()

    LaunchedEffect(mainViewModel, userId) {
        userId = mainViewModel.readUserId()
    }
    Log.d("ContactScreen", messageUiState.messages.toString())

    LaunchedEffect(supplierViewModel) {
        supplierViewModel.fetchSupplierMessages()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            when {
                messageUiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomProgressIndicator(isLoading = true)
                    }
                }

                messageUiState.error.isNotBlank() -> {
                    showToast(context = context, message = messageUiState.error)
                }

                messageUiState.messages.isNotEmpty() -> {
                    ChatScreen(
                        messages = messageUiState.messages,
                        currentUserId = userId ?: ""
                    )
                }
            }
        }
        ChatInput(
            message = supplierViewModel.message,
            onMessageChange = { supplierViewModel.message = it },
            onSend = { supplierViewModel.sendMessage() },
            context = context,
            keyboardController = keyboardController
        )
    }

}

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    messages: List<ChatMessage>,
    currentUserId: String
) {
    LazyColumn(
        modifier = modifier,
        reverseLayout = true
    ) {
        items(messages) { chatMessage ->
            ChatMessageItem(
                message = chatMessage,
                currentUserId = currentUserId
            )
        }
    }
}

@Composable
fun ChatMessageItem(
    modifier: Modifier = Modifier,
    message: ChatMessage,
    currentUserId: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = if (message.senderId == currentUserId) {
            Arrangement.End
        } else {
            Arrangement.Start
        }
    ) {
        Surface(
            modifier = modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .widthIn(max = 250.dp),
            shape = MaterialTheme.shapes.medium,
        ) {

        }

    }
}

@Composable
fun ChatInput(
    modifier: Modifier = Modifier,
    message: String,
    onMessageChange: (String) -> Unit,
    onSend: (String) -> Unit,
    context: Context,
    keyboardController: SoftwareKeyboardController?
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = message,
            onValueChange = onMessageChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text(text = "Type a message") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (message.isNotBlank()) {
                        onSend(message)
                        onMessageChange("")
                        showToast(context = context, message = "Message sent")
                        keyboardController?.hide()
                    }
                }
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                if (message.isNotBlank()) {
                    onSend(message)
                    onMessageChange("")
                    showToast(context = context, message = "Message sent")
                    keyboardController?.hide()
                }
            }
        ) {
            Text(text = "Send")
        }
    }
}
