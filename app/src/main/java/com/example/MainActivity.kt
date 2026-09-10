package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import com.example.data.model.Advocate
import com.example.data.model.Client
import com.example.data.model.FinanceEntry
import com.example.ui.MainViewModel
import com.example.ui.UserRole
import com.example.ui.components.AiChatbotSheet
import com.example.ui.components.AppHeader
import com.example.ui.components.DocumentExportSheet
import com.example.ui.components.EmergencyVakilDialog
import com.example.ui.components.IdCardDialog
import com.example.ui.components.ReceiptDialog
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AdvocateDashboardScreen
import com.example.ui.screens.AdvocateRegisterScreen
import com.example.ui.screens.CauseListScreen
import com.example.ui.screens.ELibraryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.VerifyBarScreen
import com.example.ui.theme.BarBalliaTheme
import com.example.ui.theme.NavyBackground

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BarBalliaTheme {
                MainApp(viewModel = viewModel)
            }
        }
    }
}

enum class AppScreen {
    HOME,
    REGISTER,
    LOGIN,
    ADVOCATE_DASHBOARD,
    ADMIN_DASHBOARD,
    CAUSE_LIST,
    E_LIBRARY,
    VERIFY_BAR
}

@Composable
fun MainApp(viewModel: MainViewModel) {
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf(AppScreen.HOME) }
    val userRole by viewModel.currentUserRole.collectAsState()
    val uiMessage by viewModel.uiMessage.collectAsState()

    val allAdvocates by viewModel.allAdvocates.collectAsState()
    val approvedAdvocates by viewModel.approvedAdvocates.collectAsState()
    val allClients by viewModel.allClients.collectAsState()
    val allFinance by viewModel.allFinance.collectAsState()
    val allCauseList by viewModel.allCauseList.collectAsState()
    val allBareActs by viewModel.allBareActs.collectAsState()
    val allCommittee by viewModel.allCommittee.collectAsState()
    val allProjects by viewModel.allProjects.collectAsState()
    val allJuniorSenior by viewModel.allJuniorSeniorPosts.collectAsState()
    val allChatMessages by viewModel.allChatMessages.collectAsState()
    val allNotifications by viewModel.allNotifications.collectAsState()
    val allReviews by viewModel.allReviews.collectAsState()
    val allEmergency by viewModel.allEmergencyRequests.collectAsState()
    val settings by viewModel.associationSettings.collectAsState()

    val aiMessages by viewModel.aiChatMessages.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()

    // Dialog & Modal State
    var showAiChatbotSheet by remember { mutableStateOf(false) }
    var showEmergencyDialog by remember { mutableStateOf(false) }
    var selectedAdvocateForIdCard by remember { mutableStateOf<Advocate?>(null) }
    var selectedClientForDocument by remember { mutableStateOf<Client?>(null) }
    var selectedFinanceReceipt by remember { mutableStateOf<FinanceEntry?>(null) }
    var activeVerificationBarNo by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiMessage) {
        uiMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearUiMessage()
        }
    }

    // Role-based auto navigation
    LaunchedEffect(userRole) {
        when (userRole) {
            is UserRole.AdvocateUser -> currentScreen = AppScreen.ADVOCATE_DASHBOARD
            is UserRole.Admin -> currentScreen = AppScreen.ADMIN_DASHBOARD
            is UserRole.Public -> {
                if (currentScreen == AppScreen.ADVOCATE_DASHBOARD || currentScreen == AppScreen.ADMIN_DASHBOARD) {
                    currentScreen = AppScreen.HOME
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AppHeader(
                currentRole = userRole,
                notifications = allNotifications,
                onNavigateHome = { currentScreen = AppScreen.HOME },
                onNavigateLogin = { currentScreen = AppScreen.LOGIN },
                onNavigateRegister = { currentScreen = AppScreen.REGISTER },
                onNavigateDashboard = { currentScreen = AppScreen.ADVOCATE_DASHBOARD },
                onNavigateAdmin = { currentScreen = AppScreen.ADMIN_DASHBOARD },
                onNavigateDirectory = { currentScreen = AppScreen.HOME },
                onNavigateELibrary = { currentScreen = AppScreen.E_LIBRARY },
                onNavigateCauseList = { currentScreen = AppScreen.CAUSE_LIST },
                onOpenAiChat = { showAiChatbotSheet = true },
                onLogout = { viewModel.logout() }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
            .testTag("main_app_scaffold")
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(NavyBackground)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> {
                    HomeScreen(
                        advocates = approvedAdvocates,
                        financeList = allFinance,
                        committeeList = allCommittee,
                        projectsList = allProjects,
                        reviewsList = allReviews,
                        settings = settings,
                        onNavigateRegister = { currentScreen = AppScreen.REGISTER },
                        onNavigateLogin = { currentScreen = AppScreen.LOGIN },
                        onNavigateDirectory = { /* Already on Home where directory is */ },
                        onNavigateCauseList = { currentScreen = AppScreen.CAUSE_LIST },
                        onNavigateELibrary = { currentScreen = AppScreen.E_LIBRARY },
                        onOpenEmergencyDialog = { showEmergencyDialog = true },
                        onViewIdCard = { adv -> selectedAdvocateForIdCard = adv },
                        onSubmitReview = { review -> viewModel.submitAdvocateReview(review) }
                    )
                }
                AppScreen.REGISTER -> {
                    AdvocateRegisterScreen(
                        onRegisterSubmit = { adv, onSuccess, onError ->
                            viewModel.registerNewAdvocate(adv, onSuccess, onError)
                        },
                        onNavigateLogin = { currentScreen = AppScreen.LOGIN },
                        onNavigateHome = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.LOGIN -> {
                    LoginScreen(
                        advocates = approvedAdvocates,
                        onLoginAdvocate = { email, pass, onResult ->
                            viewModel.loginAsAdvocate(email, pass, onResult)
                        },
                        onLoginAdmin = { email, pass ->
                            viewModel.loginAsAdmin(email, pass)
                        },
                        onQuickSwitchAdvocate = { adv ->
                            viewModel.quickSwitchAdvocate(adv)
                            currentScreen = AppScreen.ADVOCATE_DASHBOARD
                        },
                        onNavigateRegister = { currentScreen = AppScreen.REGISTER },
                        onLoginSuccessAdvocate = { currentScreen = AppScreen.ADVOCATE_DASHBOARD },
                        onLoginSuccessAdmin = { currentScreen = AppScreen.ADMIN_DASHBOARD }
                    )
                }
                AppScreen.ADVOCATE_DASHBOARD -> {
                    val currentAdvocate = (userRole as? UserRole.AdvocateUser)?.advocate
                        ?: approvedAdvocates.firstOrNull()
                        ?: Advocate(name = "Advocate Demo", barCouncilNo = "1042/1998", copNo = "COP/UP/2020/9941", email = "pandeysr@barballia.com", password = "Password@123")

                    AdvocateDashboardScreen(
                        currentAdvocate = currentAdvocate,
                        allClients = allClients,
                        allAdvocates = approvedAdvocates,
                        juniorSeniorPosts = allJuniorSenior,
                        chatMessages = allChatMessages,
                        onSaveClient = { client, onDone -> viewModel.saveClient(client, onDone) },
                        onDeleteClient = { client -> viewModel.deleteClient(client) },
                        onPayAnnualFee = { adv, onReceiptReady -> viewModel.payAnnualMembershipFee(adv, onReceiptReady) },
                        onCreateJuniorSeniorPost = { post -> viewModel.createJuniorSeniorPost(post) },
                        onSendChatMessage = { senderBar, senderName, recBar, msg ->
                            viewModel.sendChatMessage(senderBar, senderName, recBar, msg)
                        },
                        onGenerateDocument = { client -> selectedClientForDocument = client },
                        onShowReceipt = { entry -> selectedFinanceReceipt = entry },
                        onVerifyBarNo = { barNo ->
                            activeVerificationBarNo = barNo
                            currentScreen = AppScreen.VERIFY_BAR
                        }
                    )
                }
                AppScreen.ADMIN_DASHBOARD -> {
                    AdminDashboardScreen(
                        advocates = allAdvocates,
                        clients = allClients,
                        financeList = allFinance,
                        causeList = allCauseList,
                        emergencyRequests = allEmergency,
                        settings = settings,
                        onApproveAdvocate = { id -> viewModel.approveAdvocate(id) },
                        onRejectAdvocate = { id -> viewModel.rejectAdvocate(id) },
                        onAddFinanceEntry = { entry -> viewModel.addFinanceEntry(entry) },
                        onDeleteFinanceEntry = { entry -> viewModel.deleteFinanceEntry(entry) },
                        onAddCauseListEntry = { entry -> viewModel.addCauseListEntry(entry) },
                        onDeleteCauseListEntry = { entry -> viewModel.deleteCauseListEntry(entry) },
                        onSendNotification = { title, msg, link, target ->
                            viewModel.sendPushNotification(title, msg, link, target)
                        },
                        onUpdateSettings = { s -> viewModel.updateSettings(s) },
                        onViewIdCard = { adv -> selectedAdvocateForIdCard = adv }
                    )
                }
                AppScreen.CAUSE_LIST -> {
                    CauseListScreen(
                        causeList = allCauseList,
                        onBack = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.E_LIBRARY -> {
                    ELibraryScreen(
                        bareActs = allBareActs,
                        onBack = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.VERIFY_BAR -> {
                    VerifyBarScreen(
                        barNo = activeVerificationBarNo ?: "1042/1998",
                        advocates = approvedAdvocates,
                        onBack = { currentScreen = AppScreen.HOME }
                    )
                }
            }
        }
    }

    // Modal: AI Legal Assistant Chatbot Sheet (Gemini API)
    if (showAiChatbotSheet) {
        AiChatbotSheet(
            messages = aiMessages,
            isLoading = isAiLoading,
            onSendMessage = { query -> viewModel.askAiAssistant(query) },
            onClearChat = { viewModel.clearAiChat() },
            onDismiss = { showAiChatbotSheet = false }
        )
    }

    // Modal: Emergency Vakil Request
    if (showEmergencyDialog) {
        EmergencyVakilDialog(
            onDismiss = { showEmergencyDialog = false },
            onSubmit = { name, mob, problem, loc ->
                viewModel.submitEmergencyRequest(name, mob, problem, loc)
            }
        )
    }

    // Modal: QR ID Card Dialog
    selectedAdvocateForIdCard?.let { adv ->
        IdCardDialog(
            advocate = adv,
            onDismiss = { selectedAdvocateForIdCard = null },
            onVerifyClick = { barNo ->
                selectedAdvocateForIdCard = null
                activeVerificationBarNo = barNo
                currentScreen = AppScreen.VERIFY_BAR
            }
        )
    }

    // Modal: Document Generator Sheet (Advocate Slip, Vakalatnama, Affidavit)
    selectedClientForDocument?.let { client ->
        val currentAdv = (userRole as? UserRole.AdvocateUser)?.advocate
            ?: approvedAdvocates.firstOrNull { it.barCouncilNo == client.advocateBarNo }
            ?: approvedAdvocates.firstOrNull()
            ?: Advocate(name = "Adv. Rameshwar Nath Pandey", barCouncilNo = "1042/1998", copNo = "COP/UP/2020/9941", contactNo = "9415988800")

        DocumentExportSheet(
            advocate = currentAdv,
            client = client,
            onDismiss = { selectedClientForDocument = null }
        )
    }

    // Modal: Membership Fee Receipt Preview
    selectedFinanceReceipt?.let { entry ->
        val currentAdv = (userRole as? UserRole.AdvocateUser)?.advocate
            ?: approvedAdvocates.firstOrNull()
            ?: Advocate(name = entry.advocateName, barCouncilNo = "1042/1998", copNo = "COP/UP/2020/9941", contactNo = "9415988800")

        ReceiptDialog(
            financeEntry = entry,
            advocate = currentAdv,
            onDismiss = { selectedFinanceReceipt = null }
        )
    }
}
