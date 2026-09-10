package com.example.ui.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
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
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.ui.theme.LegalGreen
import com.example.ui.theme.LegalRed
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.NavyTertiary
import java.util.Calendar

@Composable
fun AdvocateRegisterScreen(
    onRegisterSubmit: (Advocate, () -> Unit, (String) -> Unit) -> Unit,
    onNavigateLogin: () -> Unit,
    onNavigateHome: () -> Unit
) {
    val context = LocalContext.current

    // Personal details
    var name by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("15/07/1990") }
    var calculatedAge by remember { mutableStateOf(36) }

    // 7 Address fields
    var village by remember { mutableStateOf("") }
    var post by remember { mutableStateOf("") }
    var tahsil by remember { mutableStateOf("Ballia Sadar") }
    var policeStation by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("Ballia") }
    var state by remember { mutableStateOf("Uttar Pradesh") }
    var pincode by remember { mutableStateOf("277001") }

    // Bar Credentials
    var barCouncilNo by remember { mutableStateOf("") }
    var copNo by remember { mutableStateOf("") }
    var specialization by remember { mutableStateOf("Criminal Trial & Bail") }
    var contactNo by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // File attachments
    var photoUploaded by remember { mutableStateOf(false) }
    var certificateUploaded by remember { mutableStateOf(false) }

    // Validation State
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSubmittedSuccess by remember { mutableStateOf(false) }

    // Regex check on Bar Council No: ^[0-9]{1,6}/[0-9]{4}$
    val isBarNoValid = Regex("^[0-9]{1,6}/[0-9]{4}$").matches(barCouncilNo.trim())

    // DatePicker Dialog setup
    val calendar = Calendar.getInstance()
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val dayStr = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                val monthStr = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"
                dob = "$dayStr/$monthStr/$year"
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                calculatedAge = maxOf(0, currentYear - year)
            },
            1990,
            6,
            15
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("advocate_registration_screen")
    ) {
        // Top Header
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = NavySecondary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(NavyPrimary, NavySecondary)
                        )
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.Gavel, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "ADVOCATE REGISTRATION FORM",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Criminal & Revenue Bar Association, Ballia",
                    color = GoldLight,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isSubmittedSuccess) {
            // Success & Pending Approval State Banner
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = NavySecondary),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, GoldPrimary, RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = LegalGreen, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("REGISTRATION SUBMITTED!", color = GoldLight, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Status: PENDING ADMIN APPROVAL\n\nYour profile has been forwarded to the Bar Association Administrative Committee. You will receive access and auto-generated QR ID Card once verified.",
                        color = Color.White,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onNavigateHome,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Back to Home", color = NavyPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            // Step 1: Personal Details
            Text("1. PERSONAL DETAILS", color = GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Advocate Full Name (e.g. Adv. Amit Singh)", fontSize = 11.sp) },
                colors = inputColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reg_name_input")
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = fatherName,
                onValueChange = { fatherName = it },
                label = { Text("Father's / Husband's Name", fontSize = 11.sp) },
                colors = inputColors(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // DOB Calendar with Live Age Calculation
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = dob,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("DOB (DD/MM/YYYY Calendar)", fontSize = 11.sp) },
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(Icons.Default.CalendarMonth, contentDescription = "Select DOB", tint = GoldPrimary)
                        }
                    },
                    colors = inputColors(),
                    modifier = Modifier
                        .weight(1.3f)
                        .clickable { datePickerDialog.show() }
                )

                // Live Age Calculator Display Box
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = NavySecondary),
                    modifier = Modifier
                        .weight(0.7f)
                        .height(56.dp)
                        .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Current Age", color = GoldLight, fontSize = 9.sp)
                        Text("$calculatedAge Years", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step 2: 7 Address Fields
            Text("2. PERMANENT & CHAMBER ADDRESS (7 Fields)", color = GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = village,
                    onValueChange = { village = it },
                    label = { Text("Village / Mohalla", fontSize = 11.sp) },
                    colors = inputColors(),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = post,
                    onValueChange = { post = it },
                    label = { Text("Post Office", fontSize = 11.sp) },
                    colors = inputColors(),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = tahsil,
                    onValueChange = { tahsil = it },
                    label = { Text("Tahsil (e.g. Sadar/Rasra)", fontSize = 11.sp) },
                    colors = inputColors(),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = policeStation,
                    onValueChange = { policeStation = it },
                    label = { Text("Police Station (Thana)", fontSize = 11.sp) },
                    colors = inputColors(),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = district,
                    onValueChange = { district = it },
                    label = { Text("District", fontSize = 11.sp) },
                    colors = inputColors(),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = state,
                    onValueChange = { state = it },
                    label = { Text("State", fontSize = 11.sp) },
                    colors = inputColors(),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = pincode,
                onValueChange = { if (it.length <= 6) pincode = it },
                label = { Text("Pincode (6 digits)", fontSize = 11.sp) },
                colors = inputColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Step 3: Bar Council Credentials & Verification Regex
            Text("3. BAR COUNCIL CREDENTIALS & PRACTICE", color = GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = barCouncilNo,
                onValueChange = { barCouncilNo = it },
                label = { Text("Bar Council Enrolment No (Regex: ^[0-9]{1,6}/[0-9]{4}$)", fontSize = 11.sp) },
                placeholder = { Text("e.g. 1234/1950", color = Color.Gray) },
                trailingIcon = {
                    if (barCouncilNo.isNotBlank()) {
                        Icon(
                            imageVector = if (isBarNoValid) Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = null,
                            tint = if (isBarNoValid) LegalGreen else LegalRed
                        )
                    }
                },
                supportingText = {
                    Text(
                        text = if (isBarNoValid) "✓ Valid Bar Council No format (e.g. 1234/1950)" else "Format required: digits/year (e.g. 1234/1950)",
                        color = if (isBarNoValid) LegalGreen else LegalRed,
                        fontSize = 10.sp
                    )
                },
                colors = inputColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reg_bar_no_input")
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = copNo,
                    onValueChange = { copNo = it },
                    label = { Text("Certificate of Practice (COP No)", fontSize = 11.sp) },
                    placeholder = { Text("e.g. COP/UP/2022/9901") },
                    colors = inputColors(),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = specialization,
                    onValueChange = { specialization = it },
                    label = { Text("Practice Specialization", fontSize = 11.sp) },
                    colors = inputColors(),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Step 4: Contact & Portal Account
            Text("4. CONTACT & PORTAL LOGIN SECURITY", color = GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = contactNo,
                onValueChange = { if (it.length <= 10) contactNo = it },
                label = { Text("Contact No (10-Digit Mobile)", fontSize = 11.sp) },
                colors = inputColors(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address (Used for Login)", fontSize = 11.sp) },
                colors = inputColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reg_email_input")
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Create Secret Password", fontSize = 11.sp) },
                visualTransformation = PasswordVisualTransformation(),
                colors = inputColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reg_password_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Step 5: Document Uploads (Simulated)
            Text("5. MANDATORY ATTACHMENTS (Photo & Bar Certificate)", color = GoldLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = NavySecondary),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { photoUploaded = !photoUploaded }
                        .border(1.dp, if (photoUploaded) LegalGreen else NavyTertiary, RoundedCornerShape(8.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (photoUploaded) Icons.Default.CheckCircle else Icons.Default.UploadFile,
                            contentDescription = null,
                            tint = if (photoUploaded) LegalGreen else GoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (photoUploaded) "Photo Added ✓" else "Attach Photo",
                            color = if (photoUploaded) LegalGreen else Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = NavySecondary),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { certificateUploaded = !certificateUploaded }
                        .border(1.dp, if (certificateUploaded) LegalGreen else NavyTertiary, RoundedCornerShape(8.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (certificateUploaded) Icons.Default.CheckCircle else Icons.Default.AttachFile,
                            contentDescription = null,
                            tint = if (certificateUploaded) LegalGreen else GoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (certificateUploaded) "Certificate Added ✓" else "Bar Enrol PDF/JPG",
                            color = if (certificateUploaded) LegalGreen else Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(10.dp))
                Text(it, color = LegalRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (!isBarNoValid) {
                        errorMessage = "Invalid Bar Council No. Must match regex: 1234/1950"
                    } else if (name.isBlank() || email.isBlank() || password.isBlank() || contactNo.length < 10) {
                        errorMessage = "Please fill in all mandatory fields with valid contact and password."
                    } else {
                        errorMessage = null
                        val newAdv = Advocate(
                            name = name,
                            fatherName = fatherName,
                            dob = dob,
                            age = calculatedAge,
                            village = village.ifBlank { "Bahadurpur" },
                            post = post.ifBlank { "Ballia Head Office" },
                            tahsil = tahsil,
                            policeStation = policeStation.ifBlank { "Kotwali" },
                            district = district,
                            state = state,
                            pincode = pincode,
                            barCouncilNo = barCouncilNo,
                            copNo = copNo.ifBlank { "COP/UP/2026/PENDING" },
                            contactNo = contactNo,
                            email = email,
                            password = password,
                            status = "PENDING",
                            validTill = "",
                            membershipFeePaid = false,
                            specialization = specialization
                        )
                        onRegisterSubmit(
                            newAdv,
                            { isSubmittedSuccess = true },
                            { err -> errorMessage = err }
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_advocate_registration_button")
            ) {
                Text("SUBMIT REGISTRATION (STATUS: PENDING)", color = NavyPrimary, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Already registered?", color = Color.Gray, fontSize = 11.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Login to Portal",
                    color = GoldLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateLogin() }
                )
            }
        }
    }
}

@Composable
private fun inputColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedBorderColor = GoldPrimary,
    unfocusedBorderColor = NavyTertiary,
    focusedLabelColor = GoldPrimary,
    unfocusedLabelColor = Color.LightGray,
    focusedContainerColor = NavySecondary,
    unfocusedContainerColor = NavySecondary
)
