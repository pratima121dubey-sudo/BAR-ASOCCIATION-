package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Advocate
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.LegalRed
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.NavyTertiary

@Composable
fun LoginScreen(
    advocates: List<Advocate>,
    onLoginAdvocate: (String, String, (Boolean, String) -> Unit) -> Unit,
    onLoginAdmin: (String, String) -> Boolean,
    onQuickSwitchAdvocate: (Advocate) -> Unit,
    onNavigateRegister: () -> Unit,
    onLoginSuccessAdvocate: () -> Unit,
    onLoginSuccessAdmin: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Advocate Login, 1: Admin Login
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("login_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Header Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NavySecondary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(NavyPrimary, NavySecondary)
                        )
                    )
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(GoldPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (selectedTab == 0) Icons.Default.Gavel else Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = NavyPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (selectedTab == 0) "ADVOCATE PORTAL LOGIN" else "ADMIN CONTROL LOGIN",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "CRIMINAL & REVENUE BAR ASSOCIATION, BALLIA",
                    color = GoldLight,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tab Selector (Advocate vs Admin)
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = NavySecondary,
            contentColor = GoldPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = {
                    selectedTab = 0
                    statusMessage = null
                    emailInput = "pandeysr@barballia.com"
                    passwordInput = "Password@123"
                },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Advocate Login", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = {
                    selectedTab = 1
                    statusMessage = null
                    emailInput = "admin@barballia.com"
                    passwordInput = "Admin@123"
                },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Admin Login", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input Form Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = NavySecondary),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NavyTertiary, RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = { emailInput = it },
                    label = { Text("Email Address", fontSize = 11.sp) },
                    placeholder = { Text(if (selectedTab == 0) "e.g. pandeysr@barballia.com" else "admin@barballia.com") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = NavyTertiary,
                        focusedLabelColor = GoldPrimary,
                        unfocusedLabelColor = Color.LightGray
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_email_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = { passwordInput = it },
                    label = { Text("Password", fontSize = 11.sp) },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = NavyTertiary,
                        focusedLabelColor = GoldPrimary,
                        unfocusedLabelColor = Color.LightGray
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_password_input")
                )

                statusMessage?.let { msg ->
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = msg,
                        color = if (isError) LegalRed else GoldLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (selectedTab == 0) {
                            onLoginAdvocate(emailInput, passwordInput) { success, msg ->
                                if (success) {
                                    isError = false
                                    statusMessage = msg
                                    onLoginSuccessAdvocate()
                                } else {
                                    isError = true
                                    statusMessage = msg
                                }
                            }
                        } else {
                            val ok = onLoginAdmin(emailInput, passwordInput)
                            if (ok) {
                                isError = false
                                onLoginSuccessAdmin()
                            } else {
                                isError = true
                                statusMessage = "Invalid Admin credentials (Use admin@barballia.com / Admin@123)"
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("login_submit_button")
                ) {
                    Text(
                        text = if (selectedTab == 0) "LOGIN TO ADVOCATE PORTAL" else "LOGIN TO ADMIN PANEL",
                        color = NavyPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Demo Accounts Box
        Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = NavyPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GoldPrimary.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "QUICK DEMO ACCOUNTS (One-Tap Login):",
                    color = GoldLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Admin Demo Button
                Button(
                    onClick = {
                        selectedTab = 1
                        emailInput = "admin@barballia.com"
                        passwordInput = "Admin@123"
                        val ok = onLoginAdmin("admin@barballia.com", "Admin@123")
                        if (ok) onLoginSuccessAdmin()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LegalRed),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Demo Admin (admin@barballia.com)", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Advocate Demo Buttons
                advocates.filter { it.status == "APPROVED" }.take(2).forEach { adv ->
                    OutlinedButton(
                        onClick = {
                            selectedTab = 0
                            emailInput = adv.email
                            passwordInput = adv.password
                            onQuickSwitchAdvocate(adv)
                            onLoginSuccessAdvocate()
                        },
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Advocate: ${adv.name} (${adv.barCouncilNo})", color = GoldLight, fontSize = 11.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Don't have an advocate account?", color = Color.Gray, fontSize = 11.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Register Here",
                color = GoldPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateRegister() }
            )
        }
    }
}
