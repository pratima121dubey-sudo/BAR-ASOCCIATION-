package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Advocate
import com.example.data.model.AssociationSettings
import com.example.data.model.BareAct
import com.example.data.model.BarProject
import com.example.data.model.CauseListEntry
import com.example.data.model.Client
import com.example.data.model.CommitteeMember
import com.example.data.model.EmergencyRequest
import com.example.data.model.FinanceEntry
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.LegalGreen
import com.example.ui.theme.LegalRed
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.NavySurface
import com.example.ui.theme.NavyTertiary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminDashboardScreen(
    advocates: List<Advocate>,
    clients: List<Client>,
    financeList: List<FinanceEntry>,
    causeList: List<CauseListEntry>,
    emergencyRequests: List<EmergencyRequest>,
    settings: AssociationSettings?,
    onApproveAdvocate: (Long) -> Unit,
    onRejectAdvocate: (Long) -> Unit,
    onAddFinanceEntry: (FinanceEntry) -> Unit,
    onDeleteFinanceEntry: (FinanceEntry) -> Unit,
    onAddCauseListEntry: (CauseListEntry) -> Unit,
    onDeleteCauseListEntry: (CauseListEntry) -> Unit,
    onSendNotification: (String, String, String, String) -> Unit,
    onUpdateSettings: (AssociationSettings) -> Unit,
    onViewIdCard: (Advocate) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    // 0: Pending Approvals, 1: Finance Ledger, 2: Analytics & Stats, 3: Cause List, 4: Push Broadcast, 5: Emergency SOS

    val pendingAdvocates = advocates.filter { it.status == "PENDING" }
    val approvedAdvocates = advocates.filter { it.status == "APPROVED" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
            .testTag("admin_dashboard_screen")
    ) {
        // Top Admin Banner
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
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(LegalRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("BAR ADMIN CONTROL PANEL", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Ballia Civil Court & Collectorate Hub", color = GoldLight, fontSize = 10.sp)
                    }
                }

                if (pendingAdvocates.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(GoldPrimary)
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text("${pendingAdvocates.size} PENDING", color = NavyPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = NavySecondary,
            contentColor = GoldPrimary,
            edgePadding = 8.dp
        ) {
            val tabs = listOf(
                "Pending List (${pendingAdvocates.size})",
                "Finance & Ledger",
                "Analytics & Charts",
                "Cause List Manager",
                "Push Notice",
                "Emergency SOS (${emergencyRequests.size})"
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

        // Tab Content
        when (selectedTab) {
            0 -> AdminPendingApprovalsTab(
                pendingList = pendingAdvocates,
                approvedList = approvedAdvocates,
                onApprove = onApproveAdvocate,
                onReject = onRejectAdvocate,
                onViewIdCard = onViewIdCard
            )
            1 -> AdminFinanceTab(
                financeList = financeList,
                onAddEntry = onAddFinanceEntry,
                onDeleteEntry = onDeleteFinanceEntry
            )
            2 -> AdminAnalyticsTab(
                advocates = advocates,
                clients = clients,
                financeList = financeList
            )
            3 -> AdminCauseListTab(
                causeList = causeList,
                onAdd = onAddCauseListEntry,
                onDelete = onDeleteCauseListEntry
            )
            4 -> AdminPushBroadcastTab(
                advocates = approvedAdvocates,
                onSend = onSendNotification
            )
            5 -> AdminEmergencyRequestsTab(
                requests = emergencyRequests
            )
        }
    }
}

// -------------------------------------------------------------
// TAB 1: Pending Advocate Approvals & ID Cards
// -------------------------------------------------------------
@Composable
private fun AdminPendingApprovalsTab(
    pendingList: List<Advocate>,
    approvedList: List<Advocate>,
    onApprove: (Long) -> Unit,
    onReject: (Long) -> Unit,
    onViewIdCard: (Advocate) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("PENDING ADVOCATE REGISTRATIONS", color = GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        if (pendingList.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = NavySecondary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(24.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("No pending advocate registrations! All applications are processed.", color = LegalGreen, fontSize = 11.sp)
                    }
                }
            }
        } else {
            items(pendingList) { adv ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = NavySecondary),
                    modifier = Modifier.fillMaxWidth().border(1.5.dp, GoldPrimary, RoundedCornerShape(10.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(adv.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(LegalRed)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("STATUS: PENDING", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text("Father's Name: ${adv.fatherName} • Age: ${adv.age} yrs (DOB: ${adv.dob})", color = Color.LightGray, fontSize = 10.sp)
                        Text("Bar Enrolment No: ${adv.barCouncilNo}", color = GoldPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("COP No: ${adv.copNo} • Email: ${adv.email}", color = Color.White, fontSize = 10.sp)
                        Text("Address: ${adv.village}, ${adv.post}, ${adv.tahsil}, Ballia", color = Color.LightGray, fontSize = 10.sp)
                        Text("Mobile: +91 ${adv.contactNo}", color = GoldLight, fontSize = 10.sp)

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { onApprove(adv.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = LegalGreen),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Approve & Gen QR", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { onReject(adv.id) },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(0.7f)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = null, tint = LegalRed, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reject", color = LegalRed, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text("APPROVED BAR MEMBERS (${approvedList.size})", color = GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        items(approvedList) { adv ->
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = NavySecondary),
                modifier = Modifier.fillMaxWidth().border(1.dp, NavyTertiary, RoundedCornerShape(8.dp))
            ) {
                Row(
                    modifier = Modifier.padding(10.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(adv.name, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Verified, contentDescription = null, tint = LegalGreen, modifier = Modifier.size(12.dp))
                        }
                        Text("Bar No: ${adv.barCouncilNo} • COP: ${adv.copNo}", color = GoldLight, fontSize = 10.sp)
                    }

                    Button(
                        onClick = { onViewIdCard(adv) },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Icon(Icons.Default.QrCode, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("QR Card", color = NavyPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 2: Association Finance Ledger
// -------------------------------------------------------------
@Composable
private fun AdminFinanceTab(
    financeList: List<FinanceEntry>,
    onAddEntry: (FinanceEntry) -> Unit,
    onDeleteEntry: (FinanceEntry) -> Unit
) {
    val context = LocalContext.current
    var showAddModal by remember { mutableStateOf(false) }
    var entryType by remember { mutableStateOf("EARNING") }
    var advocateName by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("") }
    var purpose by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf("Online") }

    val todayDateStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    val totalEarnings = financeList.filter { it.type == "EARNING" }.sumOf { it.amount }
    val totalExpenses = financeList.filter { it.type == "EXPENSE" }.sumOf { it.amount }
    val netBalance = totalEarnings - totalExpenses
    val todayEarning = financeList.filter { it.type == "EARNING" && it.date == todayDateStr }.sumOf { it.amount }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Summary Row
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Card(shape = RoundedCornerShape(6.dp), colors = CardDefaults.cardColors(containerColor = NavySecondary), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("TODAY EARN", color = GoldLight, fontSize = 8.sp)
                        Text("₹ ${todayEarning.toInt()}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Card(shape = RoundedCornerShape(6.dp), colors = CardDefaults.cardColors(containerColor = NavySecondary), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("TOTAL EARN", color = Color(0xFF4ADE80), fontSize = 8.sp)
                        Text("₹ ${totalEarnings.toInt()}", color = Color(0xFF4ADE80), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Card(shape = RoundedCornerShape(6.dp), colors = CardDefaults.cardColors(containerColor = NavySecondary), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("TOTAL EXPENSE", color = LegalRed, fontSize = 8.sp)
                        Text("₹ ${totalExpenses.toInt()}", color = LegalRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Card(shape = RoundedCornerShape(6.dp), colors = CardDefaults.cardColors(containerColor = NavySecondary), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("NET BALANCE", color = GoldPrimary, fontSize = 8.sp)
                        Text("₹ ${netBalance.toInt()}", color = GoldPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Action Buttons Row (Add Entry & Export to Excel)
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { showAddModal = true },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Record Transaction", color = NavyPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        Toast.makeText(context, "Exported ${financeList.size} records to Bar_Finance_Report.csv", Toast.LENGTH_LONG).show()
                    },
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Export Excel/CSV", color = GoldPrimary, fontSize = 11.sp)
                }
            }
        }

        items(financeList) { entry ->
            val isEarning = entry.type == "EARNING"
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = NavySecondary),
                modifier = Modifier.fillMaxWidth().border(1.dp, if (isEarning) LegalGreen.copy(alpha = 0.3f) else LegalRed.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            ) {
                Row(
                    modifier = Modifier.padding(10.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(
                            imageVector = if (isEarning) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                            contentDescription = null,
                            tint = if (isEarning) LegalGreen else LegalRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(entry.purpose, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("Date: ${entry.date} • Mode: ${entry.mode}${if (entry.advocateName.isNotBlank()) " • By: ${entry.advocateName}" else ""}", color = Color.LightGray, fontSize = 9.sp)
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${if (isEarning) "+" else "-"} ₹${entry.amount.toInt()}",
                            color = if (isEarning) Color(0xFF4ADE80) else LegalRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        IconButton(onClick = { onDeleteEntry(entry) }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }
    }

    if (showAddModal) {
        Dialog(onDismissRequest = { showAddModal = false }) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NavyPrimary),
                modifier = Modifier.fillMaxWidth().border(2.dp, GoldPrimary, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("ADD FINANCE TRANSACTION", color = GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { entryType = "EARNING" },
                            colors = ButtonDefaults.buttonColors(containerColor = if (entryType == "EARNING") LegalGreen else NavySecondary),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Earning (+)", color = Color.White, fontSize = 10.sp)
                        }
                        Button(
                            onClick = { entryType = "EXPENSE" },
                            colors = ButtonDefaults.buttonColors(containerColor = if (entryType == "EXPENSE") LegalRed else NavySecondary),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Expense (-)", color = Color.White, fontSize = 10.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = amountStr,
                        onValueChange = { amountStr = it },
                        label = { Text("Amount (₹) *", fontSize = 11.sp) },
                        colors = adminInputColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = purpose,
                        onValueChange = { purpose = it },
                        label = { Text("Purpose / Description *", fontSize = 11.sp) },
                        colors = adminInputColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    if (entryType == "EARNING") {
                        OutlinedTextField(
                            value = advocateName,
                            onValueChange = { advocateName = it },
                            label = { Text("Advocate / Member Name", fontSize = 11.sp) },
                            colors = adminInputColors(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    OutlinedTextField(
                        value = mode,
                        onValueChange = { mode = it },
                        label = { Text("Payment Mode (UPI / Cash / Online)", fontSize = 11.sp) },
                        colors = adminInputColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val amt = amountStr.toDoubleOrNull() ?: 0.0
                            if (amt > 0 && purpose.isNotBlank()) {
                                onAddEntry(
                                    FinanceEntry(
                                        type = entryType,
                                        date = todayDateStr,
                                        advocateName = advocateName,
                                        amount = amt,
                                        purpose = purpose,
                                        mode = mode
                                    )
                                )
                                showAddModal = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Transaction", color = NavyPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 3: Analytics & Pie Chart
// -------------------------------------------------------------
@Composable
private fun AdminAnalyticsTab(
    advocates: List<Advocate>,
    clients: List<Client>,
    financeList: List<FinanceEntry>
) {
    val totalAdvocates = advocates.size
    val approvedCount = advocates.count { it.status == "APPROVED" }
    val pendingCount = advocates.count { it.status == "PENDING" }
    val totalClients = clients.size

    val criminalCount = clients.count { it.caseNature == "CRIMINAL" }
    val revenueCount = clients.count { it.caseNature == "REVENUE" }
    val civilCount = clients.count { it.caseNature == "CIVIL" }
    val matrimonialCount = clients.count { it.caseNature == "MATRIMONIAL" }
    val otherCount = maxOf(0, totalClients - criminalCount - revenueCount - civilCount - matrimonialCount)

    val todayStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    val casesToday = clients.count { it.nextHearingDate == todayStr }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("BAR ASSOCIATION ANALYTICS & INSIGHTS", color = GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        // Stats Counter Grid
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Card(shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = NavySecondary), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("TOTAL LAWYERS", color = Color.Gray, fontSize = 8.sp)
                        Text("$totalAdvocates", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("$approvedCount Appr | $pendingCount Pend", color = GoldLight, fontSize = 8.sp)
                    }
                }
                Card(shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = NavySecondary), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("ACTIVE CASES", color = Color.Gray, fontSize = 8.sp)
                        Text("$totalClients", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("In Purvanchal Courts", color = Color.Gray, fontSize = 8.sp)
                    }
                }
                Card(shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = NavySecondary), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("HEARINGS TODAY", color = LegalRed, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        Text("$casesToday Cases", color = LegalRed, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Scheduled $todayStr", color = Color.Gray, fontSize = 8.sp)
                    }
                }
            }
        }

        // Case Nature Distribution Pie Chart Canvas Box
        item {
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = NavySecondary),
                modifier = Modifier.fillMaxWidth().border(1.dp, GoldPrimary.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PieChart, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("CASE TYPE DISTRIBUTION (PURVANCHAL)", color = GoldLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        // Custom Canvas Donut Pie Chart
                        Box(modifier = Modifier.size(110.dp), contentAlignment = Alignment.Center) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val total = if (totalClients == 0) 1f else totalClients.toFloat()
                                val crimAngle = (criminalCount / total) * 360f
                                val revAngle = (revenueCount / total) * 360f
                                val civAngle = (civilCount / total) * 360f
                                val matAngle = (matrimonialCount / total) * 360f
                                val othAngle = 360f - (crimAngle + revAngle + civAngle + matAngle)

                                var startAngle = 0f
                                val strokeW = 28f

                                drawArc(Color(0xFFDC2626), startAngle, crimAngle, false, style = Stroke(strokeW))
                                startAngle += crimAngle
                                drawArc(Color(0xFFF59E0B), startAngle, revAngle, false, style = Stroke(strokeW))
                                startAngle += revAngle
                                drawArc(Color(0xFF3B82F6), startAngle, civAngle, false, style = Stroke(strokeW))
                                startAngle += civAngle
                                drawArc(Color(0xFF10B981), startAngle, matAngle, false, style = Stroke(strokeW))
                                startAngle += matAngle
                                drawArc(Color(0xFF8B5CF6), startAngle, othAngle, false, style = Stroke(strokeW))
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("$totalClients", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text("Cases", color = GoldLight, fontSize = 8.sp)
                            }
                        }

                        // Legends
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            LegendItem(color = Color(0xFFDC2626), label = "Criminal: $criminalCount")
                            LegendItem(color = Color(0xFFF59E0B), label = "Revenue: $revenueCount")
                            LegendItem(color = Color(0xFF3B82F6), label = "Civil: $civilCount")
                            LegendItem(color = Color(0xFF10B981), label = "Matrimonial: $matrimonialCount")
                            LegendItem(color = Color(0xFF8B5CF6), label = "Other / MVACT: $otherCount")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, color = Color.White, fontSize = 10.sp)
    }
}

// -------------------------------------------------------------
// TAB 4: Daily Cause List Manager
// -------------------------------------------------------------
@Composable
private fun AdminCauseListTab(
    causeList: List<CauseListEntry>,
    onAdd: (CauseListEntry) -> Unit,
    onDelete: (CauseListEntry) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var courtNo by remember { mutableStateOf("Court No. 1 - District & Sessions Judge") }
    var judgeName by remember { mutableStateOf("Hon'ble Sri A. K. Shukla, HJS") }
    var caseNo by remember { mutableStateOf("") }
    var parties by remember { mutableStateOf("") }
    var advocateName by remember { mutableStateOf("") }
    var stage by remember { mutableStateOf("Bail Hearing") }
    var itemNoStr by remember { mutableStateOf("1") }

    val todayStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("DAILY CAUSE LIST MANAGER", color = GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Button(
                    onClick = { showAddDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Cause Item", color = NavyPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        items(causeList) { item ->
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = NavySecondary),
                modifier = Modifier.fillMaxWidth().border(1.dp, NavyTertiary, RoundedCornerShape(8.dp))
            ) {
                Row(
                    modifier = Modifier.padding(10.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.clip(RoundedCornerShape(3.dp)).background(GoldPrimary).padding(horizontal = 4.dp, vertical = 1.dp)) {
                                Text("Item #${item.itemNo}", color = NavyPrimary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(item.caseNo, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(item.parties, color = GoldLight, fontSize = 10.sp)
                        Text("${item.courtNo} • ${item.judgeName}", color = Color.LightGray, fontSize = 9.sp)
                        Text("Counsel: ${item.advocateName} | Stage: ${item.stage}", color = Color(0xFF4ADE80), fontSize = 9.sp)
                    }

                    IconButton(onClick = { onDelete(item) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = LegalRed, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        Dialog(onDismissRequest = { showAddDialog = false }) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NavyPrimary),
                modifier = Modifier.fillMaxWidth().border(2.dp, GoldPrimary, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("ADD CAUSE LIST ITEM", color = GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(value = courtNo, onValueChange = { courtNo = it }, label = { Text("Court / Judge Designation", fontSize = 10.sp) }, colors = adminInputColors(), modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = caseNo, onValueChange = { caseNo = it }, label = { Text("Case No (e.g. ST 412/2024)", fontSize = 10.sp) }, colors = adminInputColors(), modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = parties, onValueChange = { parties = it }, label = { Text("Parties (State vs Respondent)", fontSize = 10.sp) }, colors = adminInputColors(), modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = advocateName, onValueChange = { advocateName = it }, label = { Text("Advocate / Counsel Name", fontSize = 10.sp) }, colors = adminInputColors(), modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = stage, onValueChange = { stage = it }, label = { Text("Stage (e.g. Bail / Evidence)", fontSize = 10.sp) }, colors = adminInputColors(), modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            if (caseNo.isNotBlank()) {
                                onAdd(
                                    CauseListEntry(
                                        courtNo = courtNo,
                                        judgeName = judgeName,
                                        date = todayStr,
                                        caseNo = caseNo,
                                        parties = parties,
                                        advocateName = advocateName,
                                        stage = stage,
                                        itemNo = itemNoStr.toIntOrNull() ?: 1
                                    )
                                )
                                showAddDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add to Board", color = NavyPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 5: Push Broadcast
// -------------------------------------------------------------
@Composable
private fun AdminPushBroadcastTab(
    advocates: List<Advocate>,
    onSend: (String, String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("ALL") }
    var link by remember { mutableStateOf("CAUSE_LIST") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("BROADCAST PUSH NOTIFICATION", color = GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text("Send real-time alerts to advocate mobile apps in Ballia Bar Association.", color = Color.Gray, fontSize = 10.sp)

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Notification Title (e.g. Urgent Bar Meeting / Strike Notice)", fontSize = 11.sp) },
            colors = adminInputColors(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Message Body", fontSize = 11.sp) },
            colors = adminInputColors(),
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (title.isNotBlank() && message.isNotBlank()) {
                    onSend(title, message, link, target)
                    title = ""
                    message = ""
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = NavyPrimary)
            Spacer(modifier = Modifier.width(6.dp))
            Text("BROADCAST TO ALL MEMBERS", color = NavyPrimary, fontWeight = FontWeight.Bold)
        }
    }
}

// -------------------------------------------------------------
// TAB 6: Emergency SOS Leads
// -------------------------------------------------------------
@Composable
private fun AdminEmergencyRequestsTab(
    requests: List<EmergencyRequest>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("🚨 REAL-TIME TURANT VAKIL SOS LEADS", color = LegalRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        if (requests.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(30.dp), contentAlignment = Alignment.Center) {
                    Text("No emergency SOS requests at this moment.", color = Color.Gray, fontSize = 11.sp)
                }
            }
        } else {
            items(requests) { req ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = NavySecondary),
                    modifier = Modifier.fillMaxWidth().border(1.5.dp, LegalRed, RoundedCornerShape(10.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Client: ${req.clientName}", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Just Now", color = Color.LightGray, fontSize = 9.sp)
                        }
                        Text("Mobile: +91 ${req.mobile}", color = LegalGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("Location / PS: ${req.location}", color = GoldLight, fontSize = 10.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Problem: ${req.problem}", color = Color.White, fontSize = 11.sp, lineHeight = 15.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun adminInputColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedBorderColor = GoldPrimary,
    unfocusedBorderColor = NavyTertiary,
    focusedLabelColor = GoldPrimary,
    unfocusedLabelColor = Color.LightGray,
    focusedContainerColor = NavySecondary,
    unfocusedContainerColor = NavySecondary
)
