package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "advocates")
data class Advocate(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "",
    val fatherName: String = "",
    val dob: String = "01/01/1985", // DD/MM/YYYY
    val age: Int = 40,
    // 7 Address fields
    val village: String = "Bahadurpur",
    val post: String = "Ballia HO",
    val tahsil: String = "Ballia Sadar",
    val policeStation: String = "Kotwali",
    val district: String = "Ballia",
    val state: String = "Uttar Pradesh",
    val pincode: String = "277001",
    // Credentials & Bar info
    val barCouncilNo: String = "", // Validation Regex ^[0-9]{1,6}/[0-9]{4}$ e.g. 1234/1950
    val copNo: String = "",
    val contactNo: String = "",
    val email: String = "",
    val password: String = "",
    val photoUrl: String = "",
    val certificateFileName: String = "bar_council_cert.pdf",
    val status: String = "PENDING", // PENDING, APPROVED, REJECTED
    val validTill: String = "",
    val membershipFeePaid: Boolean = false,
    val membershipExpiryDate: String = "",
    val rating: Float = 4.8f,
    val ratingCount: Int = 12,
    val isBestOfMonth: Boolean = false,
    val specialization: String = "Criminal & Revenue Law",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "clients")
data class Client(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val advocateBarNo: String,
    val clientName: String,
    val mobile: String = "",
    val isMobileVerified: Boolean = false,
    // 7 Address fields
    val village: String = "",
    val post: String = "",
    val tahsil: String = "",
    val policeStation: String = "",
    val district: String = "",
    val state: String = "",
    val pincode: String = "",
    // Case Info
    val caseNature: String, // CIVIL, CRIMINAL, MATRIMONIAL, MVACT, JUVENILE, REVENUE
    val caseNo: String,
    val courtName: String,
    val previousDate: String, // DD/MM/YYYY
    val nextHearingDate: String, // DD/MM/YYYY
    val caseStage: String,
    val totalFees: Double = 0.0,
    val feesReceived: Double = 0.0,
    val feesPending: Double = 0.0,
    val notes: String = "",
    val status: String = "ACTIVE" // ACTIVE, CLOSED, DISPOSED
)

@Entity(tableName = "finance_entries")
data class FinanceEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // EARNING, EXPENSE
    val date: String, // YYYY-MM-DD or DD/MM/YYYY
    val advocateName: String = "",
    val amount: Double,
    val purpose: String,
    val mode: String = "Online", // UPI, Cash, Online
    val billUrl: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "cause_list")
data class CauseListEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val courtNo: String,
    val judgeName: String,
    val date: String, // DD/MM/YYYY
    val caseNo: String,
    val parties: String,
    val advocateName: String,
    val stage: String,
    val itemNo: Int = 1
)

@Entity(tableName = "bare_acts")
data class BareAct(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String, // Criminal, Revenue, Civil, Procedural
    val actCode: String, // IPC/BNS, CrPC/BNSS, CPC, UP_REVENUE
    val description: String,
    val sectionCount: Int,
    val pdfUrl: String = "",
    val keyProvisions: String = ""
)

@Entity(tableName = "committee_members")
data class CommitteeMember(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val post: String, // President, General Secretary, Sr. Vice President, Treasurer, Member
    val photo: String = "",
    val mobile: String,
    val tenureYear: String, // e.g. 2025-2026
    val isCurrent: Boolean = true
)

@Entity(tableName = "bar_projects")
data class BarProject(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val status: String, // UPCOMING, RUNNING, COMPLETED
    val budget: String,
    val image: String = "",
    val date: String = ""
)

@Entity(tableName = "junior_senior_posts")
data class JuniorSeniorPost(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val advocateName: String,
    val barNo: String,
    val type: String, // NEED_SENIOR, NEED_JUNIOR
    val caseNature: String,
    val location: String,
    val details: String,
    val contact: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val senderBarNo: String,
    val senderName: String,
    val recipientBarNo: String, // "ALL" or specific barNo
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class AppNotification(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val message: String,
    val link: String = "",
    val targetBarNo: String = "ALL", // "ALL" or specific barNo
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(tableName = "advocate_reviews")
data class AdvocateReview(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val advocateBarNo: String,
    val clientName: String,
    val caseNo: String,
    val rating: Int,
    val comment: String,
    val date: String
)

@Entity(tableName = "emergency_requests")
data class EmergencyRequest(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val clientName: String,
    val mobile: String,
    val problem: String,
    val location: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "PENDING"
)

@Entity(tableName = "association_settings")
data class AssociationSettings(
    @PrimaryKey
    val key: String = "main",
    val associationName: String = "CRIMINAL & REVENUE BAR ASSOCIATION, BALLIA",
    val aboutText: String = "Criminal & Revenue Bar Association, Ballia is a premier judicial collective dedicated to upholding the highest standards of the legal profession, defending constitutional rights, and providing swift legal aid in civil, revenue, and criminal jurisdictions across Ballia, Uttar Pradesh.",
    val address: String = "Civil Court Campus, Bahadurpur, Ballia, Uttar Pradesh - 277001",
    val contactPhone: String = "9415988800",
    val email: String = "contact@barballia.com",
    val website: String = "sahayogifoundation.in",
    val membershipFeeAmount: Double = 1200.0,
    val galleryImagesCsv: String = "court_gate.jpg,library_hall.jpg,annual_meeting.jpg,oath_ceremony.jpg"
)
