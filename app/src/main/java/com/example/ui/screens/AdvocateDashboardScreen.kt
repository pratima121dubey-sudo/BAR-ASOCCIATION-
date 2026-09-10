package com.example.ui.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Advocate
import com.example.data.model.ChatMessage
import com.example.data.model.Client
import com.example.data.model.FinanceEntry
import com.example.data.model.JuniorSeniorPost
import com.example.ui.components.IdCardDialog
import com.example.ui.components.QrCodeView
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.LegalGreen
import com.example.ui.theme.LegalRed
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.NavySurface
import com.example.ui.theme.NavyTertiary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvocateDashboardScreen(
    currentAdvocate: Advocate,
    allClients: List<Client>,
    allAdvocates: List<Advocate>,
    juniorSeniorPosts: List<JuniorSeniorPost>,
    chatMessages: List<ChatMessage>,
    onSaveClient: (Client, () -> Unit) -> Unit,
    onDeleteClient: (Client) -> Unit,
    onPayAnnualFee: (Advocate, (FinanceEntry) -> Unit) -> Unit,
    onCreateJuniorSeniorPost: (JuniorSeniorPost) -> Unit,
    onSendChatMessage: (String, String, String, String) -> Unit,
    onGenerateDocument: (Client) -> Unit,
    onShowReceipt: (FinanceEntry) -> Unit,
    onVerifyBarNo: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    // Tabs: 0: Case Diary & Clients, 1: Fees & Wallet, 2: Junior-Senior Connect, 3: Colleague Chat, 4: My QR ID Card

    val advocateClients = allClients.filter { it.advocateBarNo == currentAdvocate.barCouncilNo }

    // Client Add/Edit Modal
    var showClientFormModal by remember { mutableStateOf(false) }
    var editingClient by remember { mutableStateOf<Client?>(null) }
    var showIdCardModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
            .testTag("advocate_dashboard_screen")
    ) {
        // Advocate Profile Header Strip
        Card(
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = NavyPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(NavySecondary)
                            .border(1.5.dp, GoldPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = GoldLight, modifier = Modifier.size(26.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(currentAdvocate.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Verified, contentDescription = null, tint = LegalGreen, modifier = Modifier.size(14.dp))
                        }
                        Text("Bar No: ${currentAdvocate.barCouncilNo} | COP: ${currentAdvocate.copNo}", color = GoldPrimary, fontSize = 10.sp)
                    }
                }

                Button(
                    onClick = { showIdCardModal = true },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(Icons.Default.QrCode, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("QR ID Card", color = NavyPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Navigation Tabs Row
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = NavySecondary,
            contentColor = GoldPrimary,
            edgePadding = 8.dp
        ) {
            val tabs = listOf(
                "Case Diary (${advocateClients.size})",
                "Fees & Wallet",
                "Junior-Senior",
                "Advocate Chat",
                "My ID Card"
            )
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTab == index) GoldPrimary else Color.White.copy(alpha = 0.7f),
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        // Tab Body Content
        when (selectedTab) {
            0 -> CaseDiaryTab(
                clients = advocateClients,
                onAddClient = {
                    editingClient = null
                    showClientFormModal = true
                },
                onEditClient = {
                    editingClient = it
                    showClientFormModal = true
                },
                onDeleteClient = onDeleteClient,
                onGenerateDoc = onGenerateDocument
            )
            1 -> FeesAndWalletTab(
                advocate = currentAdvocate,
                clients = advocateClients,
                onPayAnnualFee = {
                    onPayAnnualFee(currentAdvocate) { entry ->
                        onShowReceipt(entry)
                    }
                }
            )
            2 -> JuniorSeniorTab(
                currentAdvocate = currentAdvocate,
                posts = juniorSeniorPosts,
                onCreatePost = onCreateJuniorSeniorPost
            )
            3 -> AdvocateChatTab(
                currentAdvocate = currentAdvocate,
                allAdvocates = allAdvocates,
                messages = chatMessages,
                onSendMessage = onSendChatMessage
            )
            4 -> MyIdCardTab(
                advocate = currentAdvocate,
                onVerifyClick = onVerifyBarNo
            )
        }
    }

    // Client Add / Edit Modal Dialog
    if (showClientFormModal) {
        ClientFormDialog(
            advocateBarNo = currentAdvocate.barCouncilNo,
            initialClient = editingClient,
            onDismiss = { showClientFormModal = false },
            onSave = { client ->
                onSaveClient(client) {
                    showClientFormModal = false
                }
            }
        )
    }

    // ID Card View Modal
    if (showIdCardModal) {
        IdCardDialog(
            advocate = currentAdvocate,
            onDismiss = { showIdCardModal = false },
            onVerifyClick = onVerifyBarNo
        )
    }
}

// -------------------------------------------------------------
// TAB 1: Case Diary & Clients
// -------------------------------------------------------------
@Composable
private fun CaseDiaryTab(
    clients: List<Client>,
    onAddClient: () -> Unit,
    onEditClient: (Client) -> Unit,
    onDeleteClient: (Client) -> Unit,
    onGenerateDoc: (Client) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedNature by remember { mutableStateOf("ALL") }
    var onlyPendingFees by remember { mutableStateOf(false) }

    val filteredClients = clients.filter { client ->
        val matchSearch = client.clientName.contains(searchQuery, ignoreCase = true) ||
                client.caseNo.contains(searchQuery, ignoreCase = true) ||
                client.mobile.contains(searchQuery)
        val matchNature = if (selectedNature == "ALL") true else client.caseNature == selectedNature
        val matchFees = if (onlyPendingFees) client.feesPending > 0 else true
        matchSearch && matchNature && matchFees
    }.sortedBy { it.nextHearingDate }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ADVOCATE CASE DIARY",
                    color = GoldLight,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = onAddClient,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("add_client_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New Client / Case", color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }

        // Search & Filters
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search client name, case number, mobile...", color = Color.Gray, fontSize = 11.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GoldPrimary) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = GoldPrimary,
                    unfocusedBorderColor = NavyTertiary,
                    focusedContainerColor = NavySecondary,
                    unfocusedContainerColor = NavySecondary
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Case Nature Filter Chips
        item {
            val natures = listOf("ALL", "CRIMINAL", "REVENUE", "CIVIL", "MATRIMONIAL", "MVACT", "JUVENILE")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(natures) { cat ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selectedNature == cat) GoldPrimary else NavySecondary)
                            .clickable { selectedNature = cat }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = cat,
                            color = if (selectedNature == cat) NavyPrimary else Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (filteredClients.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No client briefs found. Click '+ New Client / Case' to add.", color = Color.Gray, fontSize = 12.sp, textAlign = TextAlign.Center)
                }
            }
        } else {
            items(filteredClients) { client ->
                ClientCardItem(
                    client = client,
                    onEdit = { onEditClient(client) },
                    onDelete = { onDeleteClient(client) },
                    onGenerateDoc = { onGenerateDoc(client) }
                )
            }
        }
    }
}

@Composable
private fun ClientCardItem(
    client: Client,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onGenerateDoc: () -> Unit
) {
    // Check if next hearing is within 2 days or today
    val isUrgentHearing = try {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = sdf.parse(client.nextHearingDate)
        if (date != null) {
            val diff = date.time - System.currentTimeMillis()
            val days = diff / (1000 * 60 * 60 * 24)
            days in -1..2
        } else false
    } catch (e: Exception) {
        false
    }

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = NavySecondary),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.5.dp,
                if (isUrgentHearing) LegalRed else NavyTertiary,
                RoundedCornerShape(10.dp)
            )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header Row: Nature + Urgent Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(GoldPrimary)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(client.caseNature, color = NavyPrimary, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                }

                if (isUrgentHearing) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(LegalRed)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("🚨 HEARING IN ≤ 2 DAYS", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Client Name & Court
            Text(client.clientName, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("Case No: ${client.caseNo}", color = GoldLight, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Text("Court: ${client.courtName}", color = Color.LightGray, fontSize = 10.sp)
            Text("Address: ${client.village}, ${client.tahsil}, Ballia", color = Color.LightGray, fontSize = 10.sp)

            Spacer(modifier = Modifier.height(6.dp))

            // Hearing Dates & Stage Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyPrimary, RoundedCornerShape(6.dp))
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Next Hearing Date", color = Color.Gray, fontSize = 8.sp)
                        Text(
                            text = client.nextHearingDate,
                            color = if (isUrgentHearing) LegalRed else Color(0xFF4ADE80),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Prev Date", color = Color.Gray, fontSize = 8.sp)
                        Text(client.previousDate.ifBlank { "N/A" }, color = Color.White, fontSize = 10.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Case Stage", color = Color.Gray, fontSize = 8.sp)
                        Text(client.caseStage.ifBlank { "Hearing" }, color = GoldAccent, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Fees breakdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total: ₹${client.totalFees.toInt()}", color = Color.White, fontSize = 10.sp)
                Text("Received: ₹${client.feesReceived.toInt()}", color = Color(0xFF4ADE80), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "Pending: ₹${client.feesPending.toInt()}",
                    color = if (client.feesPending > 0) LegalRed else Color.LightGray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = onGenerateDoc,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                    modifier = Modifier.weight(1.3f).height(32.dp)
                ) {
                    Icon(Icons.Default.Description, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Gen Docs", color = NavyPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onEdit,
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                    modifier = Modifier.weight(0.8f).height(32.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = GoldLight, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("Edit", color = GoldLight, fontSize = 10.sp)
                }

                OutlinedButton(
                    onClick = onDelete,
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = LegalRed, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 2: Fees & Wallet Report
// -------------------------------------------------------------
@Composable
private fun FeesAndWalletTab(
    advocate: Advocate,
    clients: List<Client>,
    onPayAnnualFee: () -> Unit
) {
    val totalBilled = clients.sumOf { it.totalFees }
    val totalReceived = clients.sumOf { it.feesReceived }
    val totalPending = clients.sumOf { it.feesPending }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("ADVOCATE PRACTICE FINANCE & FEES", color = GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)

        // Practice Metrics Cards
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = NavySecondary),
                modifier = Modifier.weight(1f).border(1.dp, NavyTertiary, RoundedCornerShape(8.dp))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("TOTAL FEES", color = Color.Gray, fontSize = 9.sp)
                    Text("₹ ${totalBilled.toInt()}", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = NavySecondary),
                modifier = Modifier.weight(1f).border(1.dp, Color(0xFF16A34A).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("RECEIVED", color = Color(0xFF4ADE80), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("₹ ${totalReceived.toInt()}", color = Color(0xFF4ADE80), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = NavySecondary),
                modifier = Modifier.weight(1f).border(1.dp, LegalRed.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("PENDING", color = LegalRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("₹ ${totalPending.toInt()}", color = LegalRed, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Annual Bar Membership Fee Card (Razorpay Online Checkout Simulation)
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = NavySecondary),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, GoldPrimary, RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CreditCard, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ANNUAL BAR MEMBERSHIP FEE", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (advocate.membershipFeePaid) LegalGreen else LegalRed)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (advocate.membershipFeePaid) "PAID ACTIVE" else "FEE DUE",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Annual Association Fee: ₹ 1,200 / Year (Includes Bar Council Library, Wi-Fi access, and Legal Welfare coverage).",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 11.sp
                )

                if (advocate.membershipFeePaid) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Active Membership Valid Till: ${advocate.membershipExpiryDate.ifBlank { "31/12/2026" }}", color = GoldLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onPayAnnualFee,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Payment, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (advocate.membershipFeePaid) "Pay & Renew Next Year (₹1,200 via Razorpay)" else "Pay ₹ 1,200 Online (Razorpay)",
                        color = NavyPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 3: Junior-Senior Connect
// -------------------------------------------------------------
@Composable
private fun JuniorSeniorTab(
    currentAdvocate: Advocate,
    posts: List<JuniorSeniorPost>,
    onCreatePost: (JuniorSeniorPost) -> Unit
) {
    var showNewPostDialog by remember { mutableStateOf(false) }
    var postType by remember { mutableStateOf("NEED_SENIOR") }
    var caseNature by remember { mutableStateOf("Criminal Trial / Bail") }
    var details by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Civil Court Ballia") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("JUNIOR-SENIOR CONNECT BOARD", color = GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Purvanchal Legal Mentorship & Brief Briefing", color = Color.Gray, fontSize = 10.sp)
                }

                Button(
                    onClick = { showNewPostDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Post Request", color = NavyPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (posts.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(30.dp), contentAlignment = Alignment.Center) {
                    Text("No mentorship posts currently active.", color = Color.Gray, fontSize = 11.sp)
                }
            }
        } else {
            items(posts) { post ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = NavySecondary),
                    modifier = Modifier.fillMaxWidth().border(1.dp, NavyTertiary, RoundedCornerShape(10.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (post.type == "NEED_SENIOR") Color(0xFF3B82F6) else GoldPrimary)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (post.type == "NEED_SENIOR") "SEEKING SENIOR COUNSEL" else "INVITING JUNIORS",
                                    color = if (post.type == "NEED_SENIOR") Color.White else NavyPrimary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(post.caseNature, color = GoldLight, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(post.advocateName, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Bar No: ${post.barNo} • Location: ${post.location}", color = Color.LightGray, fontSize = 10.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(post.details, color = Color.White.copy(alpha = 0.9f), fontSize = 11.sp, lineHeight = 15.sp)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Direct Contact: +91 ${post.contact}", color = GoldLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (showNewPostDialog) {
        Dialog(onDismissRequest = { showNewPostDialog = false }) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NavyPrimary),
                modifier = Modifier.fillMaxWidth().border(2.dp, GoldPrimary, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("POST ON JUNIOR-SENIOR BOARD", color = GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { postType = "NEED_SENIOR" },
                            colors = ButtonDefaults.buttonColors(containerColor = if (postType == "NEED_SENIOR") GoldPrimary else NavySecondary),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Need Senior", color = if (postType == "NEED_SENIOR") NavyPrimary else Color.White, fontSize = 10.sp)
                        }
                        Button(
                            onClick = { postType = "NEED_JUNIOR" },
                            colors = ButtonDefaults.buttonColors(containerColor = if (postType == "NEED_JUNIOR") GoldPrimary else NavySecondary),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Need Junior", color = if (postType == "NEED_JUNIOR") NavyPrimary else Color.White, fontSize = 10.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = caseNature,
                        onValueChange = { caseNature = it },
                        label = { Text("Case Nature (e.g. Criminal Trial, Batwara)", fontSize = 11.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = NavyTertiary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = details,
                        onValueChange = { details = it },
                        label = { Text("Details / Requirements", fontSize = 11.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = NavyTertiary
                        ),
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (details.isNotBlank()) {
                                onCreatePost(
                                    JuniorSeniorPost(
                                        advocateName = currentAdvocate.name,
                                        barNo = currentAdvocate.barCouncilNo,
                                        type = postType,
                                        caseNature = caseNature,
                                        location = location,
                                        details = details,
                                        contact = currentAdvocate.contactNo
                                    )
                                )
                                showNewPostDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Post Now", color = NavyPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 4: Advocate Chat
// -------------------------------------------------------------
@Composable
private fun AdvocateChatTab(
    currentAdvocate: Advocate,
    allAdvocates: List<Advocate>,
    messages: List<ChatMessage>,
    onSendMessage: (String, String, String, String) -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    var selectedRecipient by remember { mutableStateOf("ALL") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Chat Channel Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("BAR ADVOCATE DISCUSSIONS", color = GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(NavySecondary)
                    .border(1.dp, GoldPrimary, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (selectedRecipient == "ALL") "Channel: Bar Association Common Board" else "Recipient: $selectedRecipient",
                    color = Color.White,
                    fontSize = 10.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Message List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(NavySecondary, RoundedCornerShape(8.dp))
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { msg ->
                val isMe = msg.senderBarNo == currentAdvocate.barCouncilNo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isMe) GoldPrimary else NavyPrimary
                        ),
                        modifier = Modifier.widthIn(max = 280.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            if (!isMe) {
                                Text(
                                    text = msg.senderName,
                                    color = GoldLight,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = msg.message,
                                color = if (isMe) NavyPrimary else Color.White,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Input bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Type message to Bar colleagues...", color = Color.Gray, fontSize = 11.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = GoldPrimary,
                    unfocusedBorderColor = NavyTertiary,
                    focusedContainerColor = NavySecondary,
                    unfocusedContainerColor = NavySecondary
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(6.dp))

            IconButton(
                onClick = {
                    if (messageText.isNotBlank()) {
                        onSendMessage(
                            currentAdvocate.barCouncilNo,
                            currentAdvocate.name,
                            selectedRecipient,
                            messageText
                        )
                        messageText = ""
                    }
                },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(GoldPrimary)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = NavyPrimary, modifier = Modifier.size(18.dp))
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 5: My ID Card
// -------------------------------------------------------------
@Composable
private fun MyIdCardTab(
    advocate: Advocate,
    onVerifyClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "OFFICIAL DIGITAL ADVOCATE QR ID CARD",
            color = GoldLight,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Large ID Card Graphic Container
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = NavySecondary),
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, GoldPrimary, RoundedCornerShape(14.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Gavel, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("CRIMINAL & REVENUE BAR ASSOCIATION", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                        Text("CIVIL COURT & COLLECTORATE, BALLIA", color = GoldLight, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(thickness = 1.dp, color = GoldAccent.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NavyPrimary)
                            .border(1.5.dp, GoldPrimary, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = GoldLight, modifier = Modifier.size(44.dp))
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(advocate.name, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Bar Enrolment No: ${advocate.barCouncilNo}", color = GoldPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("COP No: ${advocate.copNo}", color = GoldLight, fontSize = 11.sp)
                        Text("Tahsil: ${advocate.tahsil}, Ballia", color = Color.LightGray, fontSize = 10.sp)
                        Text("Valid Till: ${advocate.validTill.ifBlank { "31/12/2026" }}", color = Color(0xFF4ADE80), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Center QR Code
                val verifyUrl = "https://sahayogifoundation.in/verify/${advocate.barCouncilNo.replace("/", "-")}"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Status: VERIFIED & ACTIVE", color = Color(0xFF4ADE80), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("Official Purvanchal Bar Registry", color = Color.Gray, fontSize = 9.sp)
                    }
                    QrCodeView(dataString = verifyUrl, sizeDp = 74)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { onVerifyClick(advocate.barCouncilNo) },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Verified, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Verify Link", color = NavyPrimary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// -------------------------------------------------------------
// Client Add / Edit Form Dialog
// -------------------------------------------------------------
@Composable
private fun ClientFormDialog(
    advocateBarNo: String,
    initialClient: Client?,
    onDismiss: () -> Unit,
    onSave: (Client) -> Unit
) {
    val context = LocalContext.current
    var clientName by remember { mutableStateOf(initialClient?.clientName ?: "") }
    var mobile by remember { mutableStateOf(initialClient?.mobile ?: "") }
    var isOtpVerified by remember { mutableStateOf(initialClient?.isMobileVerified ?: true) }

    var village by remember { mutableStateOf(initialClient?.village ?: "") }
    var post by remember { mutableStateOf(initialClient?.post ?: "") }
    var tahsil by remember { mutableStateOf(initialClient?.tahsil ?: "Ballia Sadar") }
    var policeStation by remember { mutableStateOf(initialClient?.policeStation ?: "") }
    var district by remember { mutableStateOf(initialClient?.district ?: "Ballia") }
    var state by remember { mutableStateOf(initialClient?.state ?: "Uttar Pradesh") }
    var pincode by remember { mutableStateOf(initialClient?.pincode ?: "277001") }

    var caseNature by remember { mutableStateOf(initialClient?.caseNature ?: "CRIMINAL") }
    var caseNo by remember { mutableStateOf(initialClient?.caseNo ?: "") }
    var courtName by remember { mutableStateOf(initialClient?.courtName ?: "Sessions Court, Ballia") }
    var previousDate by remember { mutableStateOf(initialClient?.previousDate ?: "01/09/2026") }
    var nextHearingDate by remember { mutableStateOf(initialClient?.nextHearingDate ?: "15/09/2026") }
    var caseStage by remember { mutableStateOf(initialClient?.caseStage ?: "Hearing / Arguments") }

    var totalFeesStr by remember { mutableStateOf(initialClient?.totalFees?.toInt()?.toString() ?: "15000") }
    var feesReceivedStr by remember { mutableStateOf(initialClient?.feesReceived?.toInt()?.toString() ?: "10000") }
    var notes by remember { mutableStateOf(initialClient?.notes ?: "") }

    val nextDatePicker = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val dayStr = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                val monthStr = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"
                nextHearingDate = "$dayStr/$monthStr/$year"
            },
            2026, 8, 15
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NavyPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(640.dp)
                .border(2.dp, GoldPrimary, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (initialClient == null) "ADD NEW CLIENT & CASE" else "EDIT CLIENT DETAILS",
                        color = GoldLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = clientName,
                    onValueChange = { clientName = it },
                    label = { Text("Client Full Name *", fontSize = 11.sp) },
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = mobile,
                    onValueChange = { if (it.length <= 10) mobile = it },
                    label = { Text("Mobile Number (10 digits) *", fontSize = 11.sp) },
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text("Client Address (7 Fields)", color = GoldLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(value = village, onValueChange = { village = it }, label = { Text("Village", fontSize = 10.sp) }, colors = formFieldColors(), modifier = Modifier.weight(1f))
                    OutlinedTextField(value = post, onValueChange = { post = it }, label = { Text("Post", fontSize = 10.sp) }, colors = formFieldColors(), modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(value = tahsil, onValueChange = { tahsil = it }, label = { Text("Tahsil", fontSize = 10.sp) }, colors = formFieldColors(), modifier = Modifier.weight(1f))
                    OutlinedTextField(value = policeStation, onValueChange = { policeStation = it }, label = { Text("Thana", fontSize = 10.sp) }, colors = formFieldColors(), modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text("Case & Court Details", color = GoldLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))

                // Case Nature Options
                val natures = listOf("CRIMINAL", "REVENUE", "CIVIL", "MATRIMONIAL", "MVACT", "JUVENILE")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(natures) { cat ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (caseNature == cat) GoldPrimary else NavySecondary)
                                .clickable { caseNature = cat }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(cat, color = if (caseNature == cat) NavyPrimary else Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = caseNo,
                    onValueChange = { caseNo = it },
                    label = { Text("Case No (e.g. ST 412/2024 / Rev 18/2023) *", fontSize = 11.sp) },
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = courtName,
                    onValueChange = { courtName = it },
                    label = { Text("Court Name (e.g. Sessions Court, Ballia / SDM Sadar)", fontSize = 11.sp) },
                    colors = formFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(
                        value = nextHearingDate,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Next Hearing Date *", fontSize = 10.sp) },
                        trailingIcon = {
                            IconButton(onClick = { nextDatePicker.show() }) {
                                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = GoldPrimary)
                            }
                        },
                        colors = formFieldColors(),
                        modifier = Modifier.weight(1.2f).clickable { nextDatePicker.show() }
                    )
                    OutlinedTextField(
                        value = caseStage,
                        onValueChange = { caseStage = it },
                        label = { Text("Stage", fontSize = 10.sp) },
                        colors = formFieldColors(),
                        modifier = Modifier.weight(0.8f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text("Fees & Payment Accounting", color = GoldLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(
                        value = totalFeesStr,
                        onValueChange = { totalFeesStr = it },
                        label = { Text("Total Fees (₹)", fontSize = 10.sp) },
                        colors = formFieldColors(),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = feesReceivedStr,
                        onValueChange = { feesReceivedStr = it },
                        label = { Text("Received (₹)", fontSize = 10.sp) },
                        colors = formFieldColors(),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val total = totalFeesStr.toDoubleOrNull() ?: 0.0
                        val received = feesReceivedStr.toDoubleOrNull() ?: 0.0
                        val client = Client(
                            id = initialClient?.id ?: 0L,
                            advocateBarNo = advocateBarNo,
                            clientName = clientName.ifBlank { "Client Name" },
                            mobile = mobile.ifBlank { "9800000000" },
                            isMobileVerified = isOtpVerified,
                            village = village.ifBlank { "Bahadurpur" },
                            post = post.ifBlank { "Ballia" },
                            tahsil = tahsil,
                            policeStation = policeStation.ifBlank { "Kotwali" },
                            district = district,
                            state = state,
                            pincode = pincode,
                            caseNature = caseNature,
                            caseNo = caseNo.ifBlank { "Case / 2026" },
                            courtName = courtName,
                            previousDate = previousDate,
                            nextHearingDate = nextHearingDate,
                            caseStage = caseStage,
                            totalFees = total,
                            feesReceived = received,
                            feesPending = maxOf(0.0, total - received),
                            notes = notes
                        )
                        onSave(client)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save to Case Diary", color = NavyPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun formFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedBorderColor = GoldPrimary,
    unfocusedBorderColor = NavyTertiary,
    focusedLabelColor = GoldPrimary,
    unfocusedLabelColor = Color.LightGray,
    focusedContainerColor = NavySecondary,
    unfocusedContainerColor = NavySecondary
)
