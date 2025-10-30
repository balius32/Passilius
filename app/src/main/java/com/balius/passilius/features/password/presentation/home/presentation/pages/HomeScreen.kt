package com.balius.passilius.features.password.presentation.home.presentation.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.balius.passilius.features.password.domain.model.Password
import com.balius.passilius.features.password.presentation.home.presentation.HomeEvent
import com.balius.passilius.features.password.presentation.home.presentation.HomeViewModel
import org.koin.androidx.compose.getViewModel
import com.balius.passilius.R
import com.balius.passilius.core.presentation.navigation.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController, viewModel: HomeViewModel = getViewModel()
) {

    val state = viewModel.state.value

    LaunchedEffect(Unit) {
        viewModel.onEvent(HomeEvent.GetPassword)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = Color.Black
                    )
                },
                actions = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = {
                            navController.navigate(Screen.Crud.route)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Password"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.error != null -> {
                    Text(
                        text = "Error: ${state.error}",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> {
                    PasswordList(
                        passwords = state.passwords,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

    }

}

@Composable
fun PasswordList(passwords: List<Password>, modifier: Modifier = Modifier) {
    val visiblePasswords = remember(passwords) { mutableStateMapOf<Int, Boolean>() }

    LazyColumn(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(passwords, key = { it.id ?: it.hashCode() }) { password ->
            val id = password.id ?: password.hashCode()
            val isVisible = visiblePasswords[id] ?: false
            PasswordItem(
                password = password,
                isVisible = isVisible,
                onVisibilityChange = { visiblePasswords[id] = it }
            )
        }
    }
}


@Composable
fun PasswordItem(
    password: Password,
    isVisible: Boolean,
    onVisibilityChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = password.url,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = password.username ?: "(بدون نام کاربری)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (isVisible) password.password else "******",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(onClick = { onVisibilityChange(!isVisible) }) {
                Icon(
                    painter = painterResource(
                        id = if (isVisible) R.drawable.unhide_icon else R.drawable.hide_icon
                    ),
                    contentDescription = if (isVisible) "Hide password" else "Show password"
                )
            }
            IconButton(onClick = {

                clipboardManager.setText(AnnotatedString(password.username.toString()))
                clipboardManager.setText(AnnotatedString(password.password))
                Toast.makeText(context,"user and pass copied to clipboard",Toast.LENGTH_SHORT).show()

            }) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.copy_icon
                    ),
                    contentDescription = "copy password"
                )
            }
        }
    }
}
