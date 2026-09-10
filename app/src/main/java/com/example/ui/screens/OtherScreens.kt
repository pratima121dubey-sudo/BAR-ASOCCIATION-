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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Advocate
import com.example.data.model.BareAct
import com.example.data.model.CauseListEntry
import com.example.ui.components.QrCodeView
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.LegalGreen
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.NavyTertiary

@Composable
fun VerifyBarScreen(
    barNo: String,
    advocates: List<Advocate>,
    onBack: () -> Unit
) {
    val cleanBarNo = barNo.replace("-", "/")
    val advocate = advocates.firstOrNull { it.barCouncilNo.equals(cleanBarNo, ignoreCase = true) || it.barCouncilNo.replace("/", "-").equals(barNo, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("verify_bar_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = GoldPrimary)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "BAR COUNCIL VERIFICATION PORTAL",
                color = GoldLight,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (advocate != null) {
            // Verified Badge Box
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavySecondary),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, LegalGreen, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(LegalGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = Color.White, modifier = Modifier.size(38.dp))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "GENUINE & VERIFIED ADVOCATE",
                        color = LegalGreen,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Bar Council of Uttar Pradesh • Purvanchal Registry",
                        color = GoldLight,
                        fontSize = 10.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(thickness = 1.dp, color = NavyTertiary)
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(NavyPrimary)
                                .border(1.dp, GoldPrimary, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = GoldLight, modifier = Modifier.size(40.dp))
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(advocate.name, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("S/O: ${advocate.fatherName}", color = Color.LightGray, fontSize = 11.sp)
                            Text("Enrolment No: ${advocate.barCouncilNo}", color = GoldPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("COP: ${advocate.copNo}", color = GoldLight, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(NavyPrimary, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text("Association: Criminal & Revenue Bar Association, Ballia", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                            Text("Chamber/Tahsil: ${advocate.tahsil}, Ballia", color = Color.LightGray, fontSize = 10.sp)
                            Text("Membership Status: ACTIVE & IN GOOD STANDING", color = Color(0xFF4ADE80), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text("Valid Till: ${if (advocate.validTill.isNotBlank()) advocate.validTill else "31/12/2026"}", color = GoldLight, fontSize = 10.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    QrCodeView(dataString = "VERIFIED-${advocate.barCouncilNo}", sizeDp = 70)
                }
            }
        } else {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = NavySecondary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No registered record found for Bar Enrolment: $cleanBarNo", color = Color.White, fontSize = 12.sp, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
fun ELibraryScreen(
    bareActs: List<BareAct>,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredActs = bareActs.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.category.contains(searchQuery, ignoreCase = true) ||
                it.keyProvisions.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
            .padding(12.dp)
            .testTag("elibrary_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = GoldPrimary)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text("DIGITAL LAW e-LIBRARY (BARE ACTS)", color = GoldLight, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text("Criminal, Revenue & Civil Statutory Codes", color = Color.Gray, fontSize = 10.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search Bare Act, Section or Statute...", color = Color.Gray, fontSize = 11.sp) },
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

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(filteredActs) { act ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = NavySecondary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
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
                                    .background(GoldPrimary)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(act.category, color = NavyPrimary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            }
                            Text("${act.sectionCount} Sections", color = GoldLight, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(act.title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(act.description, color = Color.LightGray, fontSize = 10.sp, lineHeight = 14.sp)

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(NavyPrimary, RoundedCornerShape(6.dp))
                                .padding(8.dp)
                        ) {
                            Column {
                                Text("Key Provisions & Sections:", color = GoldPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text(act.keyProvisions, color = Color.White, fontSize = 10.sp, lineHeight = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CauseListScreen(
    causeList: List<CauseListEntry>,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredList = causeList.filter {
        it.caseNo.contains(searchQuery, ignoreCase = true) ||
                it.parties.contains(searchQuery, ignoreCase = true) ||
                it.advocateName.contains(searchQuery, ignoreCase = true) ||
                it.courtNo.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
            .padding(12.dp)
            .testTag("cause_list_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = GoldPrimary)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text("DAILY CAUSE LIST BOARD", color = GoldLight, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text("District Court & SDM Revenue Courts, Ballia", color = Color.Gray, fontSize = 10.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search case no, party name, advocate...", color = Color.Gray, fontSize = 11.sp) },
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

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filteredList) { item ->
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = NavySecondary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NavyTertiary, RoundedCornerShape(8.dp))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(GoldPrimary)
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                            ) {
                                Text("Item #${item.itemNo}", color = NavyPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(item.date, color = GoldLight, fontSize = 10.sp)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(item.caseNo, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(item.parties, color = GoldAccent, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Text(item.courtNo, color = Color.LightGray, fontSize = 9.sp)
                        Text("Counsel: ${item.advocateName} • Stage: ${item.stage}", color = Color(0xFF4ADE80), fontSize = 10.sp)
                    }
                }
            }
        }
    }
}
