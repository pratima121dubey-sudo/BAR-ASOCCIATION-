package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Advocate
import com.example.data.model.AdvocateReview
import com.example.data.model.AssociationSettings
import com.example.data.model.BarProject
import com.example.data.model.CommitteeMember
import com.example.data.model.FinanceEntry
import com.example.ui.components.AppFooter
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
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    advocates: List<Advocate>,
    financeList: List<FinanceEntry>,
    committeeList: List<CommitteeMember>,
    projectsList: List<BarProject>,
    reviewsList: List<AdvocateReview>,
    settings: AssociationSettings?,
    onNavigateRegister: () -> Unit,
    onNavigateLogin: () -> Unit,
    onNavigateDirectory: () -> Unit,
    onNavigateCauseList: () -> Unit,
    onNavigateELibrary: () -> Unit,
    onOpenEmergencyDialog: () -> Unit,
    onViewIdCard: (Advocate) -> Unit,
    onSubmitReview: (AdvocateReview) -> Unit
) {
    val todayDateStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    // Financial calculations
    val todayEarning = financeList
        .filter { it.type == "EARNING" && it.date == todayDateStr }
        .sumOf { it.amount }

    val totalEarning = financeList
        .filter { it.type == "EARNING" }
        .sumOf { it.amount }

    // Directory Search State
    var searchQuery by remember { mutableStateOf("") }
    var selectedCaseNatureFilter by remember { mutableStateOf("ALL") }
    var selectedAdvocateForContact by remember { mutableStateOf<Advocate?>(null) }
    var contactOtpInput by remember { mutableStateOf("") }
    var contactPhoneInput by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }
    var isContactUnlocked by remember { mutableStateOf(false) }

    // Review modal state
    var selectedAdvocateForReview by remember { mutableStateOf<Advocate?>(null) }
    var reviewerName by remember { mutableStateOf("") }
    var reviewerCaseNo by remember { mutableStateOf("") }
    var reviewRating by remember { mutableStateOf(5) }
    var reviewComment by remember { mutableStateOf("") }

    val bestAdvocate = advocates.firstOrNull { it.isBestOfMonth } ?: advocates.maxByOrNull { it.rating }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Hero Section Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(NavyPrimary, NavySecondary, NavyBackground)
                        )
                    )
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "WELCOME TO THE OFFICIAL PORTAL",
                        color = GoldLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "CRIMINAL & REVENUE BAR ASSOCIATION, BALLIA",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Purvanchal's Premier Legal Institution • Civil Court & Collectorate Campus",
                        color = GoldAccent,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action Buttons Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onNavigateRegister,
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("hero_register_advocate_button")
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Advocate Register", color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = onNavigateLogin,
                            shape = RoundedCornerShape(8.dp),
                            border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(GoldPrimary, GoldSecondary))),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("hero_login_button")
                        ) {
                            Icon(Icons.Default.Gavel, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Portal Login", color = GoldPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Emergency Red Button Fixed / Highlight
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = LegalRed),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clickable { onOpenEmergencyDialog() }
                    .testTag("emergency_sos_banner")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = LegalRed, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "TURANT VAKIL CHAHIYE?",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Emergency 24x7 Legal Help • Alerts Top Criminal Lawyers",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 10.sp
                            )
                        }
                    }
                    Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                }
            }
        }

        // Live Earning & Statistics Counters
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ASSOCIATION REAL-TIME METRICS",
                    color = GoldLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Today Earning Card
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = NavySecondary),
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, GoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = LegalGreen, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("TODAY EARNING", color = GoldLight, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("₹ ${todayEarning.toInt()}", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                            Text("Updated $todayDateStr", color = Color.Gray, fontSize = 8.sp)
                        }
                    }

                    // Total Earning Card
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = NavySecondary),
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, GoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AccountBalance, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("TOTAL EARNING", color = GoldLight, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("₹ ${totalEarning.toInt()}", color = GoldPrimary, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                            Text("Bar Welfare Fund", color = Color.Gray, fontSize = 8.sp)
                        }
                    }
                }
            }
        }

        // Best Advocate of the Month Spotlight
        bestAdvocate?.let { advocate ->
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = NavySurface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .border(1.5.dp, GoldPrimary, RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MilitaryTech, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "BEST ADVOCATE OF THE MONTH",
                                    color = GoldPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(14.dp))
                                Text(" ${advocate.rating} (${advocate.ratingCount} reviews)", color = GoldLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(NavyTertiary)
                                    .border(2.dp, GoldPrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = GoldLight, modifier = Modifier.size(36.dp))
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(advocate.name, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text("Bar Enrolment: ${advocate.barCouncilNo} | COP: ${advocate.copNo}", color = GoldLight, fontSize = 11.sp)
                                Text("Specialization: ${advocate.specialization}", color = Color.LightGray, fontSize = 10.sp)
                                Text("Tahsil: ${advocate.tahsil}, Ballia", color = Color.LightGray, fontSize = 10.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { onViewIdCard(advocate) },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.QrCode, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("View Verified QR Card", color = NavyPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { selectedAdvocateForReview = advocate },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Give Review", color = GoldPrimary, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        // Public Advocate Directory Section
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "PUBLIC ADVOCATE DIRECTORY",
                            color = GoldLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Verified Bar Council Lawyers of Ballia",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by Name, Bar No (e.g. 1042/1998), Village, Tahsil...", color = Color.Gray, fontSize = 11.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GoldPrimary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = NavyTertiary,
                        focusedContainerColor = NavySecondary,
                        unfocusedContainerColor = NavySecondary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("directory_search_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Case Nature Filter Chips
                val categories = listOf("ALL", "CRIMINAL", "REVENUE", "CIVIL", "MATRIMONIAL", "MVACT")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (selectedCaseNatureFilter == cat) GoldPrimary else NavySecondary)
                                .clickable { selectedCaseNatureFilter = cat }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = cat,
                                color = if (selectedCaseNatureFilter == cat) NavyPrimary else Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Advocate Cards Grid / List
        val filteredAdvocates = advocates.filter { adv ->
            val matchSearch = adv.name.contains(searchQuery, ignoreCase = true) ||
                    adv.barCouncilNo.contains(searchQuery, ignoreCase = true) ||
                    adv.village.contains(searchQuery, ignoreCase = true) ||
                    adv.tahsil.contains(searchQuery, ignoreCase = true) ||
                    adv.specialization.contains(searchQuery, ignoreCase = true)

            val matchCategory = if (selectedCaseNatureFilter == "ALL") true else {
                adv.specialization.contains(selectedCaseNatureFilter, ignoreCase = true)
            }
            matchSearch && matchCategory
        }

        if (filteredAdvocates.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No advocates found matching your query.", color = Color.Gray, fontSize = 12.sp)
                }
            }
        } else {
            items(filteredAdvocates) { adv ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = NavySecondary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .border(1.dp, NavyTertiary, RoundedCornerShape(10.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(NavyPrimary)
                                    .border(1.dp, GoldPrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = GoldLight, modifier = Modifier.size(28.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(adv.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.Verified, contentDescription = "Approved", tint = LegalGreen, modifier = Modifier.size(14.dp))
                                }
                                Text("Bar No: ${adv.barCouncilNo} | COP: ${adv.copNo}", color = GoldPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                Text("${adv.specialization} • ${adv.tahsil}, Ballia", color = Color.LightGray, fontSize = 10.sp)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(12.dp))
                                Text(" ${adv.rating}", color = GoldLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Button(
                                onClick = {
                                    selectedAdvocateForContact = adv
                                    isOtpSent = false
                                    isContactUnlocked = false
                                    contactPhoneInput = ""
                                    contactOtpInput = ""
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = NavyTertiary),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Contact (OTP)", color = Color.White, fontSize = 10.sp)
                            }

                            Button(
                                onClick = { onViewIdCard(adv) },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.QrCode, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("QR Card", color = NavyPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { selectedAdvocateForReview = adv },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.weight(0.8f)
                            ) {
                                Text("Rate ★", color = GoldLight, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        // Executive Committee Carousel Section
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "EXECUTIVE COMMITTEE (2025-2026)",
                    color = GoldLight,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(committeeList.filter { it.isCurrent }) { member ->
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = NavySecondary),
                            modifier = Modifier
                                .width(150.dp)
                                .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(NavyPrimary)
                                        .border(1.5.dp, GoldPrimary, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = GoldLight, modifier = Modifier.size(30.dp))
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(member.name, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text(member.post, color = GoldPrimary, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
                                Text("Ph: ${member.mobile}", color = Color.Gray, fontSize = 9.sp, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
        }

        // Association Projects Section
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ASSOCIATION PROJECTS & WELFARE",
                    color = GoldLight,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                projectsList.forEach { proj ->
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = NavySecondary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .border(1.dp, NavyTertiary, RoundedCornerShape(10.dp))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(proj.title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            when (proj.status) {
                                                "RUNNING" -> Color(0xFF3B82F6)
                                                "COMPLETED" -> LegalGreen
                                                else -> GoldPrimary
                                            }
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = proj.status,
                                        color = if (proj.status == "UPCOMING") NavyPrimary else Color.White,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(proj.description, color = Color.LightGray, fontSize = 10.sp, lineHeight = 14.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Budget: ${proj.budget}", color = GoldLight, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                Text(proj.date, color = Color.Gray, fontSize = 9.sp)
                            }
                        }
                    }
                }
            }
        }

        // About Us Section
        item {
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = NavySecondary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, GoldPrimary.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ABOUT BAR ASSOCIATION BALLIA", color = GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = settings?.aboutText ?: "The Criminal & Revenue Bar Association, Ballia is a historic judicial organization committed to upholding constitutional liberties, defending the public interest in criminal trials, and facilitating speedy revenue resolutions in Ballia, Uttar Pradesh.",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Footer Section
        item {
            AppFooter(onEmergencyClick = onOpenEmergencyDialog)
        }
    }

    // Modal for Contact Unlock with OTP verification
    selectedAdvocateForContact?.let { adv ->
        Dialog(onDismissRequest = { selectedAdvocateForContact = null }) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NavyPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, GoldPrimary, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "VERIFIED CONTACT ACCESS",
                        color = GoldLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "To protect advocate privacy against spam, please enter your mobile number to receive a verification OTP.",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 10.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (!isContactUnlocked) {
                        OutlinedTextField(
                            value = contactPhoneInput,
                            onValueChange = { if (it.length <= 10) contactPhoneInput = it },
                            label = { Text("Your 10-Digit Mobile Number", fontSize = 11.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = NavyTertiary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (!isOtpSent) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    if (contactPhoneInput.length == 10) {
                                        isOtpSent = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Send Verification OTP", color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        } else {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("OTP sent via Firebase Phone Auth to +91 $contactPhoneInput (Demo code: 123456)", color = GoldLight, fontSize = 10.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = contactOtpInput,
                                onValueChange = { contactOtpInput = it },
                                label = { Text("Enter 6-Digit OTP", fontSize = 11.sp) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = GoldPrimary,
                                    unfocusedBorderColor = NavyTertiary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    if (contactOtpInput.isNotBlank()) {
                                        isContactUnlocked = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = LegalGreen),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Verify & Unlock Contact Details", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    } else {
                        // Unlocked Contact Details
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF1E293B), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text("Advocate Contact Details:", color = GoldLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Name: ${adv.name}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("Phone: +91 ${adv.contactNo}", color = Color(0xFF4ADE80), fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                                Text("Email: ${adv.email}", color = Color.White, fontSize = 11.sp)
                                Text("Chamber: Civil Court Campus, Ballia", color = Color.LightGray, fontSize = 10.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { selectedAdvocateForContact = null },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Done", color = NavyPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Modal for Advocate Review
    selectedAdvocateForReview?.let { adv ->
        Dialog(onDismissRequest = { selectedAdvocateForReview = null }) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NavyPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, GoldPrimary, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "RATE & REVIEW ADVOCATE",
                        color = GoldLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Advocate: ${adv.name} (Bar No: ${adv.barCouncilNo})", color = Color.White, fontSize = 11.sp)

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = reviewerName,
                        onValueChange = { reviewerName = it },
                        label = { Text("Your Name", fontSize = 11.sp) },
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
                        value = reviewerCaseNo,
                        onValueChange = { reviewerCaseNo = it },
                        label = { Text("Case No / Matter (e.g. ST 412/2024)", fontSize = 11.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = NavyTertiary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Rating: ", color = GoldLight, fontSize = 11.sp)
                        (1..5).forEach { star ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "$star stars",
                                tint = if (star <= reviewRating) GoldPrimary else Color.Gray,
                                modifier = Modifier
                                    .size(26.dp)
                                    .clickable { reviewRating = star }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = reviewComment,
                        onValueChange = { reviewComment = it },
                        label = { Text("Your Review & Feedback", fontSize = 11.sp) },
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
                            if (reviewerName.isNotBlank() && reviewComment.isNotBlank()) {
                                onSubmitReview(
                                    AdvocateReview(
                                        advocateBarNo = adv.barCouncilNo,
                                        clientName = reviewerName,
                                        caseNo = reviewerCaseNo,
                                        rating = reviewRating,
                                        comment = reviewComment,
                                        date = todayDateStr
                                    )
                                )
                                selectedAdvocateForReview = null
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Submit 5-Star Review", color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
