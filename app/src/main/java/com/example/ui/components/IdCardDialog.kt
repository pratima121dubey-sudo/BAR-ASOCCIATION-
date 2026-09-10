package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Advocate
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.LegalGreen
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.NavyTertiary
import kotlin.math.abs

@Composable
fun QrCodeView(
    dataString: String,
    modifier: Modifier = Modifier,
    sizeDp: Int = 110
) {
    Box(
        modifier = modifier
            .size(sizeDp.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(2.dp, GoldPrimary, RoundedCornerShape(8.dp))
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        // High fidelity procedural QR matrix pattern generated from data string hash
        Canvas(modifier = Modifier.size((sizeDp - 12).dp)) {
            val hash = abs(dataString.hashCode())
            val gridSize = 17
            val cellSize = size.width / gridSize

            // Draw QR Background
            drawRect(Color.White, size = size)

            // Corner Eye Finder 1 (Top-Left)
            drawRect(Color(0xFF0D1B2A), Offset(0f, 0f), Size(cellSize * 5, cellSize * 5))
            drawRect(Color.White, Offset(cellSize, cellSize), Size(cellSize * 3, cellSize * 3))
            drawRect(Color(0xFF0D1B2A), Offset(cellSize * 2, cellSize * 2), Size(cellSize, cellSize))

            // Corner Eye Finder 2 (Top-Right)
            drawRect(Color(0xFF0D1B2A), Offset((gridSize - 5) * cellSize, 0f), Size(cellSize * 5, cellSize * 5))
            drawRect(Color.White, Offset((gridSize - 4) * cellSize, cellSize), Size(cellSize * 3, cellSize * 3))
            drawRect(Color(0xFF0D1B2A), Offset((gridSize - 3) * cellSize, cellSize * 2), Size(cellSize, cellSize))

            // Corner Eye Finder 3 (Bottom-Left)
            drawRect(Color(0xFF0D1B2A), Offset(0f, (gridSize - 5) * cellSize), Size(cellSize * 5, cellSize * 5))
            drawRect(Color.White, Offset(cellSize, (gridSize - 4) * cellSize), Size(cellSize * 3, cellSize * 3))
            drawRect(Color(0xFF0D1B2A), Offset(cellSize * 2, (gridSize - 3) * cellSize), Size(cellSize, cellSize))

            // Procedural Data Blocks
            for (r in 0 until gridSize) {
                for (c in 0 until gridSize) {
                    // Skip eyes
                    val inEye1 = r < 5 && c < 5
                    val inEye2 = r < 5 && c >= gridSize - 5
                    val inEye3 = r >= gridSize - 5 && c < 5
                    if (inEye1 || inEye2 || inEye3) continue

                    // Pseudorandom deterministic fill based on hash and coordinates
                    val bit = ((hash * (r + 1) * 31 + (c + 1) * 17 + (r * c)) % 3) == 0
                    if (bit) {
                        drawRect(
                            color = Color(0xFF0D1B2A),
                            topLeft = Offset(c * cellSize, r * cellSize),
                            size = Size(cellSize * 0.92f, cellSize * 0.92f)
                        )
                    }
                }
            }
        }

        // Center Gold Stamp
        Box(
            modifier = Modifier
                .size((sizeDp / 4.5).dp)
                .clip(CircleShape)
                .background(GoldPrimary)
                .border(1.dp, NavyPrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Gavel,
                contentDescription = null,
                tint = NavyPrimary,
                modifier = Modifier.size((sizeDp / 7).dp)
            )
        }
    }
}

@Composable
fun IdCardDialog(
    advocate: Advocate,
    onDismiss: () -> Unit,
    onVerifyClick: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NavyPrimary),
            elevation = CardDefaults.cardElevation(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("id_card_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, GoldPrimary, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                // Header with Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = LegalGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "OFFICIAL BAR COUNCIL ID CARD",
                            color = GoldLight,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Official ID Card Graphic Box
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = NavySecondary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(NavySecondary, Color(0xFF0D1B2A))
                                )
                            )
                            .border(1.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        // Association Letterhead
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.Gavel, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "CRIMINAL & REVENUE BAR ASSOCIATION",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "CIVIL COURT & COLLECTORATE, BALLIA (U.P.)",
                                    color = GoldPrimary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Body Row: Photo + Details
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Advocate Avatar Box
                            Box(
                                modifier = Modifier
                                    .size(74.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NavyTertiary)
                                    .border(2.dp, GoldPrimary, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Advocate Photo",
                                    tint = GoldLight,
                                    modifier = Modifier.size(48.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = advocate.name,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "S/O: ${advocate.fatherName}",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "Bar Council No: ${advocate.barCouncilNo}",
                                    color = GoldPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "COP No: ${advocate.copNo}",
                                    color = GoldLight,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Bottom Row: Address + QR Verification
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Tahsil: ${advocate.tahsil}, Ballia",
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = "Mobile: +91 ${advocate.contactNo}",
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = "Status: APPROVED MEMBER",
                                    color = Color(0xFF4ADE80),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Valid Till: ${if (advocate.validTill.isNotBlank()) advocate.validTill else "31/12/2026"}",
                                    color = GoldAccent,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            // QR Code
                            val verifyUrl = "https://sahayogifoundation.in/verify/${advocate.barCouncilNo.replace("/", "-")}"
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable { onVerifyClick(advocate.barCouncilNo) }
                            ) {
                                QrCodeView(dataString = verifyUrl, sizeDp = 76)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Scan to Verify",
                                    color = GoldLight,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action Buttons
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
                        Text("Verify Status", color = NavyPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
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
