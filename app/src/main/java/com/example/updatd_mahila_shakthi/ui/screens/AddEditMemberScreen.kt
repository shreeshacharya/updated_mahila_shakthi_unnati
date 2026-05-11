package com.example.updatd_mahila_shakthi.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.updatd_mahila_shakthi.ui.components.*
import com.example.updatd_mahila_shakthi.ui.theme.PrimaryPurple
import com.example.updatd_mahila_shakthi.viewmodel.MemberViewModel
import java.io.File

@Composable
fun AddEditMemberScreen(
    memberId: Long,
    viewModel: MemberViewModel,
    onNavigateBack: () -> Unit
) {
    val isEdit = memberId > 0
    val member by viewModel.selectedMember.collectAsState()
    val message by viewModel.operationMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var savedImagePath by remember { mutableStateOf("") }

    // Gallery image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Copy image to app's internal storage for persistence
            try {
                val inputStream = context.contentResolver.openInputStream(it)
                val fileName = "member_${System.currentTimeMillis()}.jpg"
                val file = File(context.filesDir, fileName)
                inputStream?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                savedImagePath = file.absolutePath
                imageUri = Uri.fromFile(file)
            } catch (e: Exception) {
                imageUri = it
                savedImagePath = it.toString()
            }
        }
    }

    LaunchedEffect(memberId) { if (isEdit) viewModel.loadMember(memberId) }
    LaunchedEffect(member) {
        member?.let {
            name = it.name
            phone = it.phone
            address = it.address
            if (it.profileImagePath.isNotBlank()) {
                savedImagePath = it.profileImagePath
                imageUri = Uri.parse(it.profileImagePath)
            }
        }
    }
    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
            if (it.contains("success")) onNavigateBack()
        }
    }

    Scaffold(
        topBar = { AppTopBar(if (isEdit) "Edit Member" else "Add Member", canNavigateBack = true, onNavigateBack = onNavigateBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile image picker
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null || savedImagePath.isNotBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(if (savedImagePath.startsWith("/")) File(savedImagePath) else imageUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Profile Photo",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.CameraAlt, "Add Photo",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Tap to add photo (optional)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            FormTextField(
                value = name, onValueChange = { name = it }, label = "Full Name",
                leadingIcon = { Icon(Icons.Default.Person, null) }
            )
            Spacer(modifier = Modifier.height(12.dp))

            FormTextField(
                value = phone, onValueChange = { 
                    if (it.length <= 10 && it.all { char -> char.isDigit() }) phone = it 
                }, label = "Phone Number",
                leadingIcon = { Icon(Icons.Default.Phone, null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            Spacer(modifier = Modifier.height(12.dp))

            FormTextField(
                value = address, onValueChange = { address = it }, label = "Address",
                leadingIcon = { Icon(Icons.Default.Home, null) }, singleLine = false
            )

            Spacer(modifier = Modifier.height(24.dp))

            GradientButton(
                text = if (isEdit) "Update Member" else "Add Member",
                onClick = {
                    viewModel.saveMember(name, phone, address, savedImagePath, 150.0, if (isEdit) memberId else -1L)
                },
                enabled = name.isNotBlank() && phone.length == 10
            )
        }
    }
}
