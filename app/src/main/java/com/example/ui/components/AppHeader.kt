package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppNotification
import com.example.ui.UserRole
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
fun AppHeader(
    currentRole: UserRole,
    notifications: List<AppNotification>,
    onNavigateHome: () -> Unit,
    onNavigateLogin: () -> Unit,
    onNavigateRegister: () -> Unit,
    onNavigateDashboard: () -> Unit,
    onNavigateAdmin: () -> Unit,
    onNavigateDirectory: () -> Unit,
    onNavigateELibrary: () -> Unit,
    onNavigateCauseList: () -> Unit,
    onOpenAiChat: () -> Unit,
    onLogout: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var showNotifMenu by remember { mutableStateOf(false) }
    val unreadCount = notifications.count { !it.isRead }

    Surface(
        color = NavyPrimary,
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("app_header")
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(NavyPrimary, NavySecondary)
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Brand Logo & Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onNavigateHome() }
                        .weight(1f, fill = false)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(GoldPrimary, GoldSecondary)
                                )
                            )
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(CircleShape)
                                .background(NavyPrimary)
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Gavel,
                                contentDescription = "Bar Logo",
                                tint = GoldPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "CRIMINAL & REVENUE",
                            color = GoldLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "BAR ASSOCIATION, BALLIA",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Civil Court & Collectorate • Reg. Purvanchal",
                            color = GoldAccent,
                            fontSize = 9.sp
                        )
                    }
                }

                // Action icons & Role controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // AI Assistant Trigger
                    IconButton(
                        onClick = onOpenAiChat,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(GoldPrimary.copy(alpha = 0.2f))
                            .testTag("ai_assistant_header_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SmartToy,
                            contentDescription = "AI Legal Assistant",
                            tint = GoldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Notification Bell
                    Box {
                        IconButton(
                            onClick = { showNotifMenu = !showNotifMenu },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(NavyTertiary.copy(alpha = 0.4f))
                                .testTag("notification_bell_button")
                        ) {
                            BadgedBox(
                                badge = {
                                    if (unreadCount > 0) {
                                        Badge(containerColor = LegalRed) {
                                            Text(unreadCount.toString(), color = Color.White, fontSize = 9.sp)
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = if (unreadCount > 0) GoldPrimary else Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showNotifMenu,
                            onDismissRequest = { showNotifMenu = false },
                            modifier = Modifier
                                .background(NavySecondary)
                                .width(300.dp)
                        ) {
                            Text(
                                text = "Association Notifications",
                                color = GoldPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                            if (notifications.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("No active notices", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp) },
                                    onClick = { showNotifMenu = false }
                                )
                            } else {
                                notifications.take(5).forEach { notif ->
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text(notif.title, color = GoldLight, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                                Text(notif.message, color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                            }
                                        },
                                        onClick = {
                                            showNotifMenu = false
                                            when (notif.link) {
                                                "CAUSE_LIST" -> onNavigateCauseList()
                                                "LIBRARY" -> onNavigateELibrary()
                                                "CLIENTS" -> onNavigateDashboard()
                                                else -> onNavigateHome()
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Navigation Dropdown Menu
                    Box {
                        IconButton(
                            onClick = { showMenu = !showMenu },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(NavyTertiary.copy(alpha = 0.4f))
                                .testTag("header_menu_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier
                                .background(NavySecondary)
                                .width(220.dp)
                        ) {
                            DropdownMenuItem(
                                leadingIcon = { Icon(Icons.Default.AccountBalance, null, tint = GoldPrimary) },
                                text = { Text("Home & Overview", color = Color.White, fontSize = 13.sp) },
                                onClick = {
                                    showMenu = false
                                    onNavigateHome()
                                }
                            )
                            DropdownMenuItem(
                                leadingIcon = { Icon(Icons.Default.Person, null, tint = GoldPrimary) },
                                text = { Text("Advocate Directory", color = Color.White, fontSize = 13.sp) },
                                onClick = {
                                    showMenu = false
                                    onNavigateDirectory()
                                }
                            )
                            DropdownMenuItem(
                                leadingIcon = { Icon(Icons.Default.Gavel, null, tint = GoldPrimary) },
                                text = { Text("Daily Cause List", color = Color.White, fontSize = 13.sp) },
                                onClick = {
                                    showMenu = false
                                    onNavigateCauseList()
                                }
                            )
                            DropdownMenuItem(
                                leadingIcon = { Icon(Icons.Default.AccountBalance, null, tint = GoldPrimary) },
                                text = { Text("e-Library Bare Acts", color = Color.White, fontSize = 13.sp) },
                                onClick = {
                                    showMenu = false
                                    onNavigateELibrary()
                                }
                            )

                            when (currentRole) {
                                is UserRole.Public -> {
                                    DropdownMenuItem(
                                        leadingIcon = { Icon(Icons.Default.Person, null, tint = GoldAccent) },
                                        text = { Text("Advocate Registration", color = GoldLight, fontSize = 13.sp, fontWeight = FontWeight.Bold) },
                                        onClick = {
                                            showMenu = false
                                            onNavigateRegister()
                                        }
                                    )
                                    DropdownMenuItem(
                                        leadingIcon = { Icon(Icons.Default.AdminPanelSettings, null, tint = GoldAccent) },
                                        text = { Text("Portal Login (Advocate/Admin)", color = GoldLight, fontSize = 13.sp, fontWeight = FontWeight.Bold) },
                                        onClick = {
                                            showMenu = false
                                            onNavigateLogin()
                                        }
                                    )
                                }
                                is UserRole.AdvocateUser -> {
                                    DropdownMenuItem(
                                        leadingIcon = { Icon(Icons.Default.Person, null, tint = GoldPrimary) },
                                        text = { Text("My Advocate Dashboard", color = GoldPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold) },
                                        onClick = {
                                            showMenu = false
                                            onNavigateDashboard()
                                        }
                                    )
                                    DropdownMenuItem(
                                        leadingIcon = { Icon(Icons.Default.Logout, null, tint = LegalRed) },
                                        text = { Text("Logout (${currentRole.advocate.name.take(12)}...)", color = LegalRed, fontSize = 13.sp) },
                                        onClick = {
                                            showMenu = false
                                            onLogout()
                                        }
                                    )
                                }
                                is UserRole.Admin -> {
                                    DropdownMenuItem(
                                        leadingIcon = { Icon(Icons.Default.AdminPanelSettings, null, tint = GoldPrimary) },
                                        text = { Text("Admin Control Panel", color = GoldPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold) },
                                        onClick = {
                                            showMenu = false
                                            onNavigateAdmin()
                                        }
                                    )
                                    DropdownMenuItem(
                                        leadingIcon = { Icon(Icons.Default.Logout, null, tint = LegalRed) },
                                        text = { Text("Logout Admin", color = LegalRed, fontSize = 13.sp) },
                                        onClick = {
                                            showMenu = false
                                            onLogout()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Quick Role Ribbon
            when (currentRole) {
                is UserRole.AdvocateUser -> {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(GoldPrimary.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Advocate: ${currentRole.advocate.name} (Bar No: ${currentRole.advocate.barCouncilNo})",
                            color = GoldLight,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = if (currentRole.advocate.membershipFeePaid) "✓ Fee Paid" else "⚠️ Fee Due",
                            color = if (currentRole.advocate.membershipFeePaid) Color(0xFF4ADE80) else LegalRed,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                is UserRole.Admin -> {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(LegalRed.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🛡️ ADMIN CONTROL MODE: admin@barballia.com",
                            color = Color(0xFFFCA5A5),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                is UserRole.Public -> {}
            }
        }
    }
}

@Composable
fun AppFooter(
    onEmergencyClick: () -> Unit
) {
    Surface(
        color = NavyBackground,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CRIMINAL & REVENUE BAR ASSOCIATION, BALLIA",
                color = GoldPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Civil Court Campus, Bahadurpur, Ballia, Uttar Pradesh - 277001",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Helpline: +91 9415988800 | Official Portal: sahayogifoundation.in",
                color = GoldLight,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Dedicated to Professional Excellence, Speedy Justice & Legal Aid in Purvanchal",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "© 2026 Criminal & Revenue Bar Association Ballia. All Rights Reserved.",
                color = Color.White.copy(alpha = 0.4f),
                fontSize = 9.sp
            )
        }
    }
}
