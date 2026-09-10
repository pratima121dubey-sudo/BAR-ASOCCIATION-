package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiLegalAssistant
import com.example.data.db.AppDatabase
import com.example.data.model.Advocate
import com.example.data.model.AdvocateReview
import com.example.data.model.AppNotification
import com.example.data.model.AssociationSettings
import com.example.data.model.BareAct
import com.example.data.model.BarProject
import com.example.data.model.CauseListEntry
import com.example.data.model.ChatMessage
import com.example.data.model.Client
import com.example.data.model.CommitteeMember
import com.example.data.model.EmergencyRequest
import com.example.data.model.FinanceEntry
import com.example.data.model.JuniorSeniorPost
import com.example.data.repository.BarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

sealed class UserRole {
    object Public : UserRole()
    data class AdvocateUser(val advocate: Advocate) : UserRole()
    object Admin : UserRole()
}

data class ChatItem(val text: String, val isUser: Boolean, val timestamp: Long = System.currentTimeMillis())

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BarRepository

    init {
        val db = AppDatabase.getDatabase(application, viewModelScope)
        repository = BarRepository(db.barDao())
    }

    // User Role State
    private val _currentUserRole = MutableStateFlow<UserRole>(UserRole.Public)
    val currentUserRole: StateFlow<UserRole> = _currentUserRole.asStateFlow()

    // Active Verification Bar No
    private val _verificationBarNo = MutableStateFlow<String?>(null)
    val verificationBarNo: StateFlow<String?> = _verificationBarNo.asStateFlow()

    // AI Chat Messages
    private val _aiChatMessages = MutableStateFlow<List<ChatItem>>(
        listOf(
            ChatItem(
                text = "Namaste! I am the UP Criminal & Revenue Law AI Assistant for Ballia Bar Association. Ask me any question regarding sections of BNS, BNSS, UP Revenue Code, CPC, or Bail laws.",
                isUser = false
            )
        )
    )
    val aiChatMessages: StateFlow<List<ChatItem>> = _aiChatMessages.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    // Data Streams from Repository
    val allAdvocates: StateFlow<List<Advocate>> = repository.allAdvocates
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val approvedAdvocates: StateFlow<List<Advocate>> = repository.approvedAdvocates
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingAdvocates: StateFlow<List<Advocate>> = repository.pendingAdvocates
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allClients: StateFlow<List<Client>> = repository.allClients
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFinance: StateFlow<List<FinanceEntry>> = repository.allFinance
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCauseList: StateFlow<List<CauseListEntry>> = repository.allCauseList
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBareActs: StateFlow<List<BareAct>> = repository.allBareActs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCommittee: StateFlow<List<CommitteeMember>> = repository.allCommittee
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allProjects: StateFlow<List<BarProject>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allJuniorSeniorPosts: StateFlow<List<JuniorSeniorPost>> = repository.allJuniorSeniorPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allChatMessages: StateFlow<List<ChatMessage>> = repository.allChatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotifications: StateFlow<List<AppNotification>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allReviews: StateFlow<List<AdvocateReview>> = repository.allReviews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allEmergencyRequests: StateFlow<List<EmergencyRequest>> = repository.allEmergencyRequests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val associationSettings: StateFlow<AssociationSettings?> = repository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // UI Toast / Status message
    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage: StateFlow<String?> = _uiMessage.asStateFlow()

    fun clearUiMessage() {
        _uiMessage.value = null
    }

    fun setUiMessage(msg: String) {
        _uiMessage.value = msg
    }

    // --- Authentication & Session ---
    fun loginAsAdmin(email: String, pass: String): Boolean {
        if (email.trim().equals("admin@barballia.com", ignoreCase = true) && pass == "Admin@123") {
            _currentUserRole.value = UserRole.Admin
            _uiMessage.value = "Welcome Admin! Logged in to Bar Association Control Panel."
            return true
        }
        _uiMessage.value = "Invalid Admin credentials. Use admin@barballia.com / Admin@123"
        return false
    }

    fun loginAsAdvocate(email: String, pass: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val adv = repository.getAdvocateByEmail(email.trim())
            if (adv == null) {
                onResult(false, "No advocate account found with this email.")
                return@launch
            }
            if (adv.password != pass) {
                onResult(false, "Incorrect password.")
                return@launch
            }
            if (adv.status == "PENDING") {
                onResult(false, "Your registration is PENDING Admin approval. Please contact the Bar Council desk.")
                return@launch
            }
            if (adv.status == "REJECTED") {
                onResult(false, "Your registration was rejected by Admin. Please re-apply with valid credentials.")
                return@launch
            }
            _currentUserRole.value = UserRole.AdvocateUser(adv)
            onResult(true, "Welcome back, ${adv.name}!")
        }
    }

    fun quickSwitchAdvocate(advocate: Advocate) {
        _currentUserRole.value = UserRole.AdvocateUser(advocate)
    }

    fun logout() {
        _currentUserRole.value = UserRole.Public
        _uiMessage.value = "Logged out successfully."
    }

    fun setVerificationBarNo(barNo: String?) {
        _verificationBarNo.value = barNo
    }

    // --- Registration & Validation ---
    fun validateBarCouncilNo(barNo: String): Boolean {
        val regex = Regex("^[0-9]{1,6}/[0-9]{4}$")
        return regex.matches(barNo.trim())
    }

    fun calculateAge(dob: String): Int {
        return try {
            val parts = dob.split("/")
            if (parts.size == 3) {
                val birthYear = parts[2].toInt()
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                maxOf(0, currentYear - birthYear)
            } else {
                0
            }
        } catch (e: Exception) {
            0
        }
    }

    fun registerNewAdvocate(
        advocate: Advocate,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (!validateBarCouncilNo(advocate.barCouncilNo)) {
            onError("Invalid Bar Council No format. Must match regex: 1234/1950")
            return
        }
        if (advocate.name.isBlank() || advocate.email.isBlank() || advocate.password.isBlank()) {
            onError("Please fill in all mandatory fields.")
            return
        }

        viewModelScope.launch {
            try {
                val existing = repository.getAdvocateByBarNo(advocate.barCouncilNo)
                if (existing != null) {
                    onError("Bar Council No already registered!")
                    return@launch
                }
                repository.registerAdvocate(advocate.copy(status = "PENDING"))
                _uiMessage.value = "Registration submitted successfully! Status is PENDING admin approval."
                onSuccess()
            } catch (e: Exception) {
                onError("Registration failed: ${e.message}")
            }
        }
    }

    // --- Admin Actions ---
    fun approveAdvocate(advocateId: Long) {
        viewModelScope.launch {
            val cal = Calendar.getInstance()
            cal.add(Calendar.YEAR, 1)
            val expiryDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(cal.time)
            repository.updateAdvocateStatus(advocateId, "APPROVED", expiryDate)
            _uiMessage.value = "Advocate approved! QR ID Card generated."
        }
    }

    fun rejectAdvocate(advocateId: Long) {
        viewModelScope.launch {
            repository.updateAdvocateStatus(advocateId, "REJECTED", "")
            _uiMessage.value = "Advocate registration rejected."
        }
    }

    fun addFinanceEntry(entry: FinanceEntry) {
        viewModelScope.launch {
            repository.insertFinanceEntry(entry)
            _uiMessage.value = "Finance entry recorded successfully!"
        }
    }

    fun deleteFinanceEntry(entry: FinanceEntry) {
        viewModelScope.launch {
            repository.deleteFinanceEntry(entry)
            _uiMessage.value = "Finance entry removed."
        }
    }

    // --- Membership Payment Simulation (Razorpay) ---
    fun payAnnualMembershipFee(advocate: Advocate, onReceiptReady: (FinanceEntry) -> Unit) {
        viewModelScope.launch {
            val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val cal = Calendar.getInstance()
            cal.add(Calendar.YEAR, 1)
            val expiryDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(cal.time)

            // Update advocate payment status
            repository.updateMembershipPayment(advocate.barCouncilNo, expiryDate)

            // Record in Association Finance
            val finance = FinanceEntry(
                type = "EARNING",
                date = today,
                advocateName = advocate.name,
                amount = 1200.0,
                purpose = "Annual Bar Membership Fee (Online Razorpay Txn #${System.currentTimeMillis() % 1000000})",
                mode = "Online"
            )
            repository.insertFinanceEntry(finance)

            // Update current user state if logged in
            val updatedAdv = advocate.copy(membershipFeePaid = true, membershipExpiryDate = expiryDate)
            _currentUserRole.value = UserRole.AdvocateUser(updatedAdv)

            _uiMessage.value = "Payment of ₹1200 successful! Membership active till $expiryDate."
            onReceiptReady(finance)
        }
    }

    // --- Client Management ---
    fun saveClient(client: Client, onDone: () -> Unit) {
        viewModelScope.launch {
            val pending = maxOf(0.0, client.totalFees - client.feesReceived)
            val clientWithFees = client.copy(feesPending = pending)
            if (client.id == 0L) {
                repository.insertClient(clientWithFees)
                _uiMessage.value = "Client record added to Case Diary!"
            } else {
                repository.updateClient(clientWithFees)
                _uiMessage.value = "Client details updated."
            }
            onDone()
        }
    }

    fun deleteClient(client: Client) {
        viewModelScope.launch {
            repository.deleteClient(client)
            _uiMessage.value = "Client record deleted."
        }
    }

    // --- Cause List Management ---
    fun addCauseListEntry(entry: CauseListEntry) {
        viewModelScope.launch {
            repository.insertCauseListEntry(entry)
            _uiMessage.value = "Cause List item added."
        }
    }

    fun deleteCauseListEntry(entry: CauseListEntry) {
        viewModelScope.launch {
            repository.deleteCauseListEntry(entry)
            _uiMessage.value = "Cause List item removed."
        }
    }

    // --- Bare Acts Management ---
    fun addBareAct(act: BareAct) {
        viewModelScope.launch {
            repository.insertBareAct(act)
            _uiMessage.value = "Bare Act added to e-Library."
        }
    }

    // --- Committee & Projects ---
    fun addCommitteeMember(member: CommitteeMember) {
        viewModelScope.launch {
            repository.insertCommitteeMember(member)
            _uiMessage.value = "Committee member saved."
        }
    }

    fun addProject(project: BarProject) {
        viewModelScope.launch {
            repository.insertProject(project)
            _uiMessage.value = "Project added."
        }
    }

    // --- Junior Senior Connect ---
    fun createJuniorSeniorPost(post: JuniorSeniorPost) {
        viewModelScope.launch {
            repository.insertJuniorSeniorPost(post)
            _uiMessage.value = "Junior-Senior Connect notice posted!"
        }
    }

    // --- Internal Chat ---
    fun sendChatMessage(senderBarNo: String, senderName: String, recipientBarNo: String, message: String) {
        if (message.isBlank()) return
        viewModelScope.launch {
            repository.sendChatMessage(
                ChatMessage(
                    senderBarNo = senderBarNo,
                    senderName = senderName,
                    recipientBarNo = recipientBarNo,
                    message = message
                )
            )
        }
    }

    // --- Push Notifications ---
    fun sendPushNotification(title: String, message: String, link: String, target: String) {
        viewModelScope.launch {
            repository.insertNotification(
                AppNotification(
                    title = title,
                    message = message,
                    link = link,
                    targetBarNo = target
                )
            )
            _uiMessage.value = "Push Notification broadcasted to advocates!"
        }
    }

    fun markNotificationRead(id: Long) {
        viewModelScope.launch {
            repository.markNotificationRead(id)
        }
    }

    // --- Public Review ---
    fun submitAdvocateReview(review: AdvocateReview) {
        viewModelScope.launch {
            repository.insertReview(review)
            _uiMessage.value = "Thank you! Your 5-star rating and review have been submitted."
        }
    }

    // --- Emergency Request ---
    fun submitEmergencyRequest(clientName: String, mobile: String, problem: String, location: String) {
        viewModelScope.launch {
            repository.insertEmergencyRequest(
                EmergencyRequest(
                    clientName = clientName,
                    mobile = mobile,
                    problem = problem,
                    location = location
                )
            )
            // Also notify criminal lawyers
            repository.insertNotification(
                AppNotification(
                    title = "🚨 EMERGENCY VAKIL ALERT: $location",
                    message = "Client: $clientName (Ph: $mobile). Problem: $problem. Immediate assistance requested.",
                    link = "EMERGENCY",
                    targetBarNo = "ALL"
                )
            )
            _uiMessage.value = "Emergency request dispatched! Top 5 criminal lawyers in Ballia notified."
        }
    }

    // --- Settings Editor ---
    fun updateSettings(settings: AssociationSettings) {
        viewModelScope.launch {
            repository.saveSettings(settings)
            _uiMessage.value = "Association information updated successfully."
        }
    }

    // --- AI Legal Assistant Chatbot ---
    fun askAiAssistant(query: String) {
        if (query.isBlank()) return
        val userItem = ChatItem(text = query, isUser = true)
        _aiChatMessages.value = _aiChatMessages.value + userItem
        _isAiLoading.value = true

        viewModelScope.launch {
            val response = GeminiLegalAssistant.askLegalAssistant(query)
            val aiItem = ChatItem(text = response, isUser = false)
            _aiChatMessages.value = _aiChatMessages.value + aiItem
            _isAiLoading.value = false
        }
    }

    fun clearAiChat() {
        _aiChatMessages.value = listOf(
            ChatItem(
                text = "Chat history cleared. How can I assist you with UP Criminal & Revenue laws today?",
                isUser = false
            )
        )
    }

    // --- Helper calculations ---
    fun getHearingBadgeColor(nextHearingDate: String): Boolean {
        // Returns true if hearing is today or within next 2 days
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val hearing = sdf.parse(nextHearingDate) ?: return false
            val today = Date()
            val diff = hearing.time - today.time
            val days = diff / (1000 * 60 * 60 * 24)
            days in -1..2
        } catch (e: Exception) {
            false
        }
    }
}
