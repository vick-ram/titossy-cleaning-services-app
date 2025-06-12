package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.titossycleaningservicesapp.domain.models.Roles
import com.example.titossycleaningservicesapp.domain.viewmodel.EmployeeViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.MessageViewModel

@Composable
fun ChatScreen(modifier: Modifier = Modifier) {
    val mainViewModel: MainViewModel = hiltViewModel()
    val messageViewModel: MessageViewModel = hiltViewModel()
    val messageUiState by messageViewModel.messageUiState.collectAsStateWithLifecycle()

    val employeeViewModel: EmployeeViewModel = hiltViewModel()
    val employeeUiState by employeeViewModel.employeeUiState.collectAsStateWithLifecycle()

//    Messages are sent to the admin, so get the receiverId when employee role is admin
    val admin = employeeUiState.employees?.find { it.role == Roles.ADMIN }

    var currentUserId by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(currentUserId) {
        currentUserId = mainViewModel.readUserId()
    }

    LaunchedEffect(messageViewModel) {
        admin?.let { messageViewModel.fetchMessages(currentUserId.toString(), it.id) }
    }

    if (messageUiState.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    if (messageUiState.error.isNotEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = messageUiState.error, color = MaterialTheme.colorScheme.error)
        }
    }

    if (messageUiState.messages.isNotEmpty()) {
        val messages = messageUiState.messages.map { currentUserId?.let { id -> it.toChatMessage(id) } }
        ChatUi(
            messages = messages.filterNotNull(),
            onSendMessage = { messageViewModel.sendMessage(it) },
            modifier = modifier
        )
    } else {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No messages yet", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun ChatUi(
    messages: List<ChatMessageUiModel>,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Messages List
        MessagesList(modifier = modifier.weight(1f), messages = messages)

        // Message Input
        MessageInput(onSendMessage = onSendMessage)
    }
}

@Composable
private fun MessagesList(modifier: Modifier = Modifier, messages: List<ChatMessageUiModel>) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(messages) { message ->
            ChatBubble(
                message = message,
                modifier = modifier.fillParentMaxWidth(0.8f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MessageInput(onSendMessage: (String) -> Unit) {
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = textState,
            onValueChange = { textState = it },
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(24.dp)),
            placeholder = { Text("Type a message...") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent
            ),
            singleLine = false,
            maxLines = 3,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (textState.text.isNotBlank()) {
                        onSendMessage(textState.text)
                        textState = TextFieldValue("")
                        focusManager.clearFocus()
                    }
                }
            )
        )

        if (textState.text.isNotBlank()) {
            IconButton(
                onClick = {
                    if (textState.text.isNotBlank()) {
                        onSendMessage(textState.text)
                        textState = TextFieldValue("")
                        focusManager.clearFocus()
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send message",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun ChatBubble(
    message: ChatMessageUiModel,
    modifier: Modifier = Modifier
) {
    val bubbleColor = if (message.isFromMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val textColor = if (message.isFromMe) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = modifier
            .wrapContentWidth(),
        contentAlignment = (if (message.isFromMe) Alignment.End else Alignment.Start) as Alignment
    ) {
        Column(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = if (message.isFromMe) 12.dp else 0.dp,
                        bottomEnd = if (message.isFromMe) 0.dp else 12.dp
                    )
                )
                .background(bubbleColor)
                .padding(12.dp),
            horizontalAlignment = if (message.isFromMe) Alignment.End else Alignment.Start
        ) {
            Text(
                text = message.text,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = message.timestamp,
                color = textColor.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

// Data Model
data class ChatMessageUiModel(
    val id: String,
    val text: String,
    val timestamp: String,
    val isFromMe: Boolean
)