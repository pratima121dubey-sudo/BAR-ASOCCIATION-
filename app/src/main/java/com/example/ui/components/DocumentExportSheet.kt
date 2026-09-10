package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Advocate
import com.example.data.model.Client
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.NavyTertiary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DocumentExportSheet(
    advocate: Advocate,
    client: Client,
    onDismiss: () -> Unit
) {
    var selectedDocType by remember { mutableStateOf(0) } // 0: Vakalatnama, 1: Advocate Slip, 2: Affidavit
    val docTitles = listOf("Vakalatnama (वकालतनामा)", "Advocate Memo Slip", "Court Affidavit (शपथ-पत्र)")
    val todayDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.75f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(680.dp)
                    .border(2.dp, GoldPrimary, RoundedCornerShape(16.dp))
                    .testTag("document_generator_dialog")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Description, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "LEGAL DOCUMENT GENERATOR",
                                color = GoldLight,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Document Selector Tabs
                    TabRow(
                        selectedTabIndex = selectedDocType,
                        containerColor = NavySecondary,
                        contentColor = GoldPrimary
                    ) {
                        docTitles.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedDocType == index,
                                onClick = { selectedDocType = index },
                                text = {
                                    Text(
                                        text = title.split(" ")[0],
                                        color = if (selectedDocType == index) GoldPrimary else Color.White.copy(alpha = 0.7f),
                                        fontSize = 11.sp,
                                        fontWeight = if (selectedDocType == index) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Printable Legal Page Paper Box
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFF8)), // Ivory legal paper color
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFD1D5DB), RoundedCornerShape(8.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            when (selectedDocType) {
                                0 -> VakalatnamaContent(advocate, client, todayDate)
                                1 -> AdvocateSlipContent(advocate, client, todayDate)
                                2 -> AffidavitContent(advocate, client, todayDate)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Print & Export Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Print, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Print Document", color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Download PDF", color = GoldPrimary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VakalatnamaContent(advocate: Advocate, client: Client, date: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Court Header
        Text(
            text = "IN THE COURT OF: ${client.courtName.ifBlank { "SESSIONS JUDGE / SDM, BALLIA" }}",
            color = Color.Black,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Case No: ${client.caseNo.ifBlank { "ST / Rev. Suit No. ______ of 2026" }} (Nature: ${client.caseNature})",
            color = Color.DarkGray,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))
        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "VAKALATNAMA / वकालतनामा",
            color = Color(0xFF1E3A8A),
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "I / We, ${client.clientName}, Resident of Village: ${client.village}, Post: ${client.post}, Tahsil: ${client.tahsil}, District: ${client.district} (${client.state}), the Complainant / Applicant / Appellant in the above-titled proceeding, do hereby appoint and retain:",
            color = Color.Black,
            fontSize = 10.sp,
            lineHeight = 14.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF3F4F6), RoundedCornerShape(4.dp))
                .padding(8.dp)
        ) {
            Column {
                Text(
                    text = "${advocate.name}, Advocate",
                    color = Color(0xFF0F172A),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "U.P. Bar Council Enrolment No: ${advocate.barCouncilNo} | COP: ${advocate.copNo}",
                    color = Color.DarkGray,
                    fontSize = 10.sp
                )
                Text(
                    text = "Member, Criminal & Revenue Bar Association, Ballia (Purvanchal)",
                    color = Color(0xFF1E3A8A),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Chamber / Contact: Civil Court Campus Ballia | Ph: +91 ${advocate.contactNo}",
                    color = Color.DarkGray,
                    fontSize = 9.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "as my/our Advocate to plead, act, compromise, receive documents, file appeals, revisions, bail applications, affidavits and represent me/us in all proceedings connected with the aforesaid case till final disposal.",
            color = Color.Black,
            fontSize = 10.sp,
            lineHeight = 13.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text("Accepted by me:", color = Color.DarkGray, fontSize = 9.sp)
                Spacer(modifier = Modifier.height(20.dp))
                Text("_________________________", color = Color.Black, fontSize = 9.sp)
                Text("${advocate.name}", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text("Advocate, Ballia", color = Color.DarkGray, fontSize = 9.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Date: $date", color = Color.DarkGray, fontSize = 9.sp)
                Spacer(modifier = Modifier.height(20.dp))
                Text("_________________________", color = Color.Black, fontSize = 9.sp)
                Text("${client.clientName}", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text("Signature / Thumb Impression of Client", color = Color.DarkGray, fontSize = 8.sp)
            }
        }
    }
}

@Composable
private fun AdvocateSlipContent(advocate: Advocate, client: Client, date: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Advocate Memo Letterhead
        Text(
            text = "MEMORANDUM OF APPEARANCE / ADVOCATE SLIP",
            color = Color(0xFF0F172A),
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "CRIMINAL & REVENUE BAR ASSOCIATION, BALLIA",
            color = Color(0xFF1E3A8A),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(thickness = 1.dp, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Advocate: ${advocate.name}", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text("Bar Enrolment No: ${advocate.barCouncilNo}", color = Color.Black, fontSize = 9.sp)
                Text("COP No: ${advocate.copNo}", color = Color.Black, fontSize = 9.sp)
                Text("Mobile: +91 ${advocate.contactNo}", color = Color.Black, fontSize = 9.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Date: $date", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text("Stage: ${client.caseStage.ifBlank { "Appearance / Hearing" }}", color = Color.DarkGray, fontSize = 9.sp)
                QrCodeView(dataString = "SLIP-${advocate.barCouncilNo}-${client.caseNo}-$date", sizeDp = 50)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .padding(8.dp)
        ) {
            Column {
                Text("CASE BRIEF DETAILS", color = Color(0xFF1E3A8A), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text("Court: ${client.courtName}", color = Color.Black, fontSize = 9.sp)
                Text("Case No: ${client.caseNo}", color = Color.Black, fontSize = 9.sp)
                Text("Client Name: ${client.clientName} (Mob: ${client.mobile})", color = Color.Black, fontSize = 9.sp)
                Text("Address: ${client.village}, ${client.tahsil}, ${client.district}", color = Color.Black, fontSize = 9.sp)
                Text("Nature of Case: ${client.caseNature}", color = Color.Black, fontSize = 9.sp)
                Text("Next Hearing Date: ${client.nextHearingDate}", color = Color(0xFFDC2626), fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Signature of Advocate: ____________________",
            color = Color.Black,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AffidavitContent(advocate: Advocate, client: Client, date: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "BEFORE THE COURT OF: ${client.courtName.ifBlank { "JUDICIAL MAGISTRATE / SDM, BALLIA" }}",
            color = Color.Black,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Case No: ${client.caseNo.ifBlank { "Misc. App No. ______ / 2026" }}",
            color = Color.DarkGray,
            fontSize = 9.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "AFFIDAVIT / शपथ-पत्र",
            color = Color(0xFF1E3A8A),
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "I, ${client.clientName}, aged about ____ years, resident of Village: ${client.village}, Post: ${client.post}, Tahsil: ${client.tahsil}, District: ${client.district}, do solemnly affirm and state on oath as under:",
            color = Color.Black,
            fontSize = 10.sp,
            lineHeight = 13.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "1. That I am the deponent and fully conversant with the facts and circumstances of the present case.\n" +
                   "2. That the contents of the application/petition filed herewith in connection with ${client.caseNature} matter have been read over and explained to me in Hindi and the same are true to my personal knowledge and belief.\n" +
                   "3. That no part of it is false and nothing material has been concealed therefrom.",
            color = Color.Black,
            fontSize = 9.sp,
            lineHeight = 13.sp
        )

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "VERIFICATION",
            color = Color.Black,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Verified at Ballia on this $date that the contents of paragraphs 1 to 3 of the affidavit are true to my personal knowledge. So help me God.",
            color = Color.Black,
            fontSize = 9.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Identified by me:", color = Color.DarkGray, fontSize = 8.sp)
                Spacer(modifier = Modifier.height(14.dp))
                Text("${advocate.name}", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text("Advocate (Enrol: ${advocate.barCouncilNo})", color = Color.DarkGray, fontSize = 8.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Spacer(modifier = Modifier.height(14.dp))
                Text("_________________________", color = Color.Black, fontSize = 8.sp)
                Text("Deponent (${client.clientName})", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
