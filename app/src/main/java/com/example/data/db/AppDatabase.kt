package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Database(
    entities = [
        Advocate::class,
        Client::class,
        FinanceEntry::class,
        CauseListEntry::class,
        BareAct::class,
        CommitteeMember::class,
        BarProject::class,
        JuniorSeniorPost::class,
        ChatMessage::class,
        AppNotification::class,
        AdvocateReview::class,
        EmergencyRequest::class,
        AssociationSettings::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun barDao(): BarDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bar_association_ballia.db"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.barDao())
                    }
                }
            }
        }

        private suspend fun populateInitialData(dao: BarDao) {
            val todayStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

            // Default Settings
            dao.saveSettings(
                AssociationSettings(
                    key = "main",
                    associationName = "CRIMINAL & REVENUE BAR ASSOCIATION, BALLIA",
                    aboutText = "Established in the historic district of Ballia, the Criminal & Revenue Bar Association represents over 500 practicing advocates in the Civil Court & Collectorate. We are dedicated to the defense of human liberty, speedy resolution of land & revenue disputes, and legal modernization in Purvanchal.",
                    address = "Civil Court Campus, Bahadurpur, Ballia (U.P.) - 277001",
                    contactPhone = "9415988800",
                    email = "admin@barballia.com",
                    website = "sahayogifoundation.in",
                    membershipFeeAmount = 1200.0
                )
            )

            // Seed Advocates (Approved)
            val adv1 = Advocate(
                name = "Adv. Rameshwar Nath Pandey",
                fatherName = "Late Kedar Nath Pandey",
                dob = "15/07/1975",
                age = 51,
                village = "Maniyar",
                post = "Maniyar",
                tahsil = "Bansdih",
                policeStation = "Maniyar",
                district = "Ballia",
                state = "Uttar Pradesh",
                pincode = "277211",
                barCouncilNo = "1042/1998",
                copNo = "COP/UP/2020/9941",
                contactNo = "9415988800",
                email = "pandeysr@barballia.com",
                password = "Password@123",
                status = "APPROVED",
                validTill = "31/12/2026",
                membershipFeePaid = true,
                membershipExpiryDate = "31/12/2026",
                rating = 4.9f,
                ratingCount = 28,
                isBestOfMonth = true,
                specialization = "Senior Criminal & Writ Defense"
            )
            val adv2 = Advocate(
                name = "Adv. Anand Kumar Singh",
                fatherName = "R. B. Singh",
                dob = "22/11/1982",
                age = 43,
                village = "Bahadurpur",
                post = "Ballia Head Office",
                tahsil = "Ballia Sadar",
                policeStation = "Kotwali",
                district = "Ballia",
                state = "Uttar Pradesh",
                pincode = "277001",
                barCouncilNo = "2450/2006",
                copNo = "COP/UP/2021/4122",
                contactNo = "9839123456",
                email = "anand.singh@barballia.com",
                password = "Password@123",
                status = "APPROVED",
                validTill = "31/12/2026",
                membershipFeePaid = true,
                membershipExpiryDate = "31/12/2026",
                rating = 4.8f,
                ratingCount = 19,
                isBestOfMonth = false,
                specialization = "Revenue Code, Khatauni & Partition"
            )
            val adv3 = Advocate(
                name = "Adv. Priya Srivastava",
                fatherName = "Devendra Srivastava",
                dob = "05/04/1990",
                age = 36,
                village = "Rasra",
                post = "Rasra",
                tahsil = "Rasra",
                policeStation = "Rasra",
                district = "Ballia",
                state = "Uttar Pradesh",
                pincode = "277123",
                barCouncilNo = "5120/2015",
                copNo = "COP/UP/2022/8814",
                contactNo = "9450123789",
                email = "priya.law@barballia.com",
                password = "Password@123",
                status = "APPROVED",
                validTill = "31/12/2026",
                membershipFeePaid = true,
                membershipExpiryDate = "31/12/2026",
                rating = 4.7f,
                ratingCount = 14,
                isBestOfMonth = false,
                specialization = "Matrimonial & POCSO / Bail Specialist"
            )
            val adv4Pending = Advocate(
                name = "Adv. Vikash Kumar Yadav",
                fatherName = "Ramdev Yadav",
                dob = "10/08/1996",
                age = 30,
                village = "Bairia",
                post = "Surmanpur",
                tahsil = "Bairia",
                policeStation = "Bairia",
                district = "Ballia",
                state = "Uttar Pradesh",
                pincode = "277201",
                barCouncilNo = "8912/2022",
                copNo = "COP/UP/2023/1102",
                contactNo = "9123456780",
                email = "vikash.yadav@gmail.com",
                password = "Password@123",
                status = "PENDING",
                validTill = "",
                membershipFeePaid = false,
                specialization = "Criminal Trial & Bail"
            )

            dao.insertAdvocate(adv1)
            dao.insertAdvocate(adv2)
            dao.insertAdvocate(adv3)
            dao.insertAdvocate(adv4Pending)

            // Seed Clients for Rameshwar Nath Pandey
            dao.insertClient(
                Client(
                    advocateBarNo = "1042/1998",
                    clientName = "Subhash Chandra Tiwari",
                    mobile = "9876543210",
                    isMobileVerified = true,
                    village = "Maniyar",
                    post = "Maniyar",
                    tahsil = "Bansdih",
                    policeStation = "Maniyar",
                    district = "Ballia",
                    state = "Uttar Pradesh",
                    pincode = "277211",
                    caseNature = "CRIMINAL",
                    caseNo = "ST 412/2024",
                    courtName = "Sessions Court, Ballia",
                    previousDate = "10/08/2026",
                    nextHearingDate = todayStr, // Today!
                    caseStage = "Prosecution Evidence (PW-3)",
                    totalFees = 35000.0,
                    feesReceived = 25000.0,
                    feesPending = 10000.0,
                    notes = "Summon medical officer for cross examination."
                )
            )
            dao.insertClient(
                Client(
                    advocateBarNo = "1042/1998",
                    clientName = "Ram Ashish Verma",
                    mobile = "9415001122",
                    isMobileVerified = true,
                    village = "Chitbara Gaon",
                    post = "Chitbara Gaon",
                    tahsil = "Ballia Sadar",
                    policeStation = "Fefna",
                    district = "Ballia",
                    state = "Uttar Pradesh",
                    pincode = "277501",
                    caseNature = "REVENUE",
                    caseNo = "Rev. Suit 182/2023 (Sec 116 UP Rev Code)",
                    courtName = "Court of SDM Sadar, Ballia",
                    previousDate = "01/08/2026",
                    nextHearingDate = "12/09/2026", // within 2 days!
                    caseStage = "Fard Batwara Hearing",
                    totalFees = 20000.0,
                    feesReceived = 15000.0,
                    feesPending = 5000.0,
                    notes = "Objection to Lekhpal Qurra report filed."
                )
            )
            dao.insertClient(
                Client(
                    advocateBarNo = "1042/1998",
                    clientName = "Ganga Devi",
                    mobile = "9988776655",
                    isMobileVerified = true,
                    village = "Sikanderpur",
                    post = "Sikanderpur",
                    tahsil = "Sikanderpur",
                    policeStation = "Sikanderpur",
                    district = "Ballia",
                    state = "Uttar Pradesh",
                    pincode = "277303",
                    caseNature = "MATRIMONIAL",
                    caseNo = "Matrimonial Case 45/2025 (Sec 125 CrPC)",
                    courtName = "Family Court, Ballia",
                    previousDate = "15/07/2026",
                    nextHearingDate = "28/09/2026",
                    caseStage = "Interim Maintenance Argument",
                    totalFees = 15000.0,
                    feesReceived = 15000.0,
                    feesPending = 0.0,
                    notes = "Bank statements of respondent filed on record."
                )
            )

            // Seed Finance Entries
            dao.insertFinanceEntry(
                FinanceEntry(
                    type = "EARNING",
                    date = todayStr,
                    advocateName = "Adv. Rameshwar Nath Pandey",
                    amount = 1200.0,
                    purpose = "Annual Bar Membership Fee 2026-27",
                    mode = "Online"
                )
            )
            dao.insertFinanceEntry(
                FinanceEntry(
                    type = "EARNING",
                    date = todayStr,
                    advocateName = "Adv. Anand Kumar Singh",
                    amount = 1200.0,
                    purpose = "Annual Bar Membership Fee 2026-27",
                    mode = "UPI"
                )
            )
            dao.insertFinanceEntry(
                FinanceEntry(
                    type = "EARNING",
                    date = "01/09/2026",
                    advocateName = "Adv. Priya Srivastava",
                    amount = 1200.0,
                    purpose = "Bar Welfare Fund Contribution",
                    mode = "Online"
                )
            )
            dao.insertFinanceEntry(
                FinanceEntry(
                    type = "EXPENSE",
                    date = "05/09/2026",
                    amount = 8500.0,
                    purpose = "Purchase of Latest All India Reporter & UP Revenue Codes for e-Library",
                    mode = "Online",
                    billUrl = "bill_books_2026.pdf"
                )
            )
            dao.insertFinanceEntry(
                FinanceEntry(
                    type = "EXPENSE",
                    date = todayStr,
                    amount = 3200.0,
                    purpose = "High-speed Wi-Fi router maintenance & Association Hall Sanitization",
                    mode = "UPI"
                )
            )

            // Seed Committee
            dao.insertCommitteeMember(
                CommitteeMember(
                    name = "Adv. Devendra Kumar Rai",
                    post = "President",
                    mobile = "9415203344",
                    tenureYear = "2025-2026",
                    isCurrent = true
                )
            )
            dao.insertCommitteeMember(
                CommitteeMember(
                    name = "Adv. Satyendra Nath Mishra",
                    post = "General Secretary",
                    mobile = "9450882211",
                    tenureYear = "2025-2026",
                    isCurrent = true
                )
            )
            dao.insertCommitteeMember(
                CommitteeMember(
                    name = "Adv. Rajesh Kumar Choubey",
                    post = "Treasurer",
                    mobile = "9838009922",
                    tenureYear = "2025-2026",
                    isCurrent = true
                )
            )
            dao.insertCommitteeMember(
                CommitteeMember(
                    name = "Adv. Mithilesh Kumar Singh",
                    post = "Senior Vice President",
                    mobile = "9415776633",
                    tenureYear = "2025-2026",
                    isCurrent = true
                )
            )
            dao.insertCommitteeMember(
                CommitteeMember(
                    name = "Adv. Harihar Prasad Gupta",
                    post = "Former President",
                    mobile = "9415000111",
                    tenureYear = "2023-2024",
                    isCurrent = false
                )
            )

            // Seed Projects
            dao.insertProject(
                BarProject(
                    title = "Digital Bar Library & High-Speed Optical Wi-Fi",
                    description = "Installation of computerized legal research pods with SCC Online, Manupatra and All India Reporter terminal for young and senior advocates.",
                    status = "RUNNING",
                    budget = "₹ 4,50,000",
                    date = "Target Oct 2026"
                )
            )
            dao.insertProject(
                BarProject(
                    title = "New Advocate Chambers & Solar Powered Shed",
                    description = "Construction of 40 well-ventilated advocate consulting chambers and rooftop green solar canopy in Ballia Civil Court campus.",
                    status = "UPCOMING",
                    budget = "₹ 18,00,000",
                    date = "Target Jan 2027"
                )
            )
            dao.insertProject(
                BarProject(
                    title = "Annual Health & Medical Emergency Welfare Trust",
                    description = "Comprehensive accidental coverage and emergency interest-free financial aid scheme for association members.",
                    status = "COMPLETED",
                    budget = "₹ 6,00,000",
                    date = "Completed May 2026"
                )
            )

            // Seed Bare Acts
            dao.insertBareAct(
                BareAct(
                    title = "Bharatiya Nyaya Sanhita (BNS) / IPC",
                    category = "Criminal",
                    actCode = "BNS_2023",
                    description = "The substantive criminal penal code defining offenses against state, body, property, public peace, and their respective punishments.",
                    sectionCount = 358,
                    keyProvisions = "Sec 103 (Murder), Sec 115 (Grievous Hurt), Sec 303 (Theft), Sec 318 (Cheating), Sec 189 (Unlawful Assembly)"
                )
            )
            dao.insertBareAct(
                BareAct(
                    title = "Bharatiya Nagarik Suraksha Sanhita (BNSS) / CrPC",
                    category = "Criminal Procedure",
                    actCode = "BNSS_2023",
                    description = "Governs criminal investigation, arrest protocols, FIR registration, regular/anticipatory bail, trial procedure, and revision.",
                    sectionCount = 531,
                    keyProvisions = "Sec 479 (Bail for undertrials), Sec 480 (Anticipatory Bail), Sec 173 (Cognizable Report), Sec 126 (Maintenance), Sec 187 (Police Custody)"
                )
            )
            dao.insertBareAct(
                BareAct(
                    title = "Uttar Pradesh Revenue Code, 2006",
                    category = "Revenue & Land Laws",
                    actCode = "UP_REV_2006",
                    description = "Unified statutory code relating to land tenures, revenue administration, partition (Batwara), boundary disputes (Dakhil Kharij), and mutation in UP.",
                    sectionCount = 234,
                    keyProvisions = "Sec 34 (Mutation/Dakhil Kharij), Sec 24 (Demarcation/Seemankan), Sec 116 (Partition/Batwara of Holdings), Sec 67 (Eviction from Gaon Sabha Land), Sec 144 (Declaratory Suit)"
                )
            )
            dao.insertBareAct(
                BareAct(
                    title = "Code of Civil Procedure, 1908 (CPC)",
                    category = "Civil Procedure",
                    actCode = "CPC_1908",
                    description = "Administration of civil litigation in India, jurisdiction of civil courts, summons, injunctions, pleadings, and decree execution.",
                    sectionCount = 158,
                    keyProvisions = "Order 39 Rule 1 & 2 (Temporary Injunction), Sec 9 (Courts jurisdiction), Order 7 Rule 11 (Rejection of Plaint), Sec 96 (First Appeal), Sec 100 (Second Appeal)"
                )
            )
            dao.insertBareAct(
                BareAct(
                    title = "Bharatiya Sakshya Adhiniyam (BSA) / Evidence Act",
                    category = "Evidence",
                    actCode = "BSA_2023",
                    description = "Principles of admissibility of evidence, burden of proof, electronic records validation, examination of witnesses, and judicial presumptions.",
                    sectionCount = 170,
                    keyProvisions = "Sec 61 (Electronic Records), Sec 104 (Burden of Proof), Sec 140 (Leading Questions), Sec 148 (Impeaching credit of witness)"
                )
            )

            // Seed Daily Cause List
            dao.insertCauseListEntry(
                CauseListEntry(
                    courtNo = "Court No. 1 - District & Sessions Judge",
                    judgeName = "Hon'ble Sri A. K. Shukla, HJS",
                    date = todayStr,
                    caseNo = "ST 412/2024",
                    parties = "State of U.P. vs. Munna Tiwari & Ors.",
                    advocateName = "Adv. Rameshwar Nath Pandey",
                    stage = "Prosecution Evidence",
                    itemNo = 4
                )
            )
            dao.insertCauseListEntry(
                CauseListEntry(
                    courtNo = "Court No. 1 - District & Sessions Judge",
                    judgeName = "Hon'ble Sri A. K. Shukla, HJS",
                    date = todayStr,
                    caseNo = "Bail App 921/2026",
                    parties = "Rakesh Pandey vs. State of U.P.",
                    advocateName = "Adv. Rameshwar Nath Pandey",
                    stage = "Bail Hearing (U/S 480 BNSS)",
                    itemNo = 9
                )
            )
            dao.insertCauseListEntry(
                CauseListEntry(
                    courtNo = "Court No. 3 - Additional District Judge-I",
                    judgeName = "Hon'ble Sri R. K. Verma, HJS",
                    date = todayStr,
                    caseNo = "Civil Appeal 28/2023",
                    parties = "Suraj Gupta vs. Shyam Sunder",
                    advocateName = "Adv. Anand Kumar Singh",
                    stage = "Final Arguments",
                    itemNo = 12
                )
            )
            dao.insertCauseListEntry(
                CauseListEntry(
                    courtNo = "Court of SDM Sadar, Ballia",
                    judgeName = "Sri Prashant Kumar, PCS",
                    date = todayStr,
                    caseNo = "Mutation Suit 312/2026",
                    parties = "Lalita Devi vs. Ramdhani",
                    advocateName = "Adv. Anand Kumar Singh",
                    stage = "Objection Hearing (Sec 34 UP Rev Code)",
                    itemNo = 2
                )
            )

            // Seed Junior Senior Posts
            dao.insertJuniorSeniorPost(
                JuniorSeniorPost(
                    advocateName = "Adv. Vikash Kumar Yadav",
                    barNo = "8912/2022",
                    type = "NEED_SENIOR",
                    caseNature = "Criminal Trial (Bail & Cross Examination)",
                    location = "Civil Court Ballia",
                    details = "Looking for guidance in Sessions Court trial on murder & firearm ballistics evidence.",
                    contact = "9123456780"
                )
            )
            dao.insertJuniorSeniorPost(
                JuniorSeniorPost(
                    advocateName = "Adv. Rameshwar Nath Pandey",
                    barNo = "1042/1998",
                    type = "NEED_JUNIOR",
                    caseNature = "Revenue & Criminal Writ / SDM Courts",
                    location = "Collectorate & Civil Court Ballia",
                    details = "Inviting energetic junior advocate for revenue filing, batwara drafting, and client brief preparation.",
                    contact = "9415988800"
                )
            )

            // Seed Initial Chat
            dao.insertChatMessage(
                ChatMessage(
                    senderBarNo = "1042/1998",
                    senderName = "Adv. Rameshwar Nath Pandey",
                    recipientBarNo = "ALL",
                    message = "Colleagues, today's Cause List in Court No. 1 is starting at 10:30 AM sharp. Please prepare list of bail applications."
                )
            )
            dao.insertChatMessage(
                ChatMessage(
                    senderBarNo = "2450/2006",
                    senderName = "Adv. Anand Kumar Singh",
                    recipientBarNo = "ALL",
                    message = "Noted Senior. SDM Sadar court board is also published on the Association portal."
                )
            )

            // Seed Notification
            dao.insertNotification(
                AppNotification(
                    title = "Annual Bar General Body Meeting",
                    message = "All members are requested to attend the General Body Meeting on Saturday at 4 PM in Association Hall regarding Digital Library inauguration.",
                    link = "COMMITTEE",
                    targetBarNo = "ALL"
                )
            )
            dao.insertNotification(
                AppNotification(
                    title = "Next Hearing Alert: ST 412/2024",
                    message = "Client Subhash Chandra Tiwari case has hearing scheduled TODAY in Sessions Court Ballia.",
                    link = "CLIENTS",
                    targetBarNo = "1042/1998"
                )
            )

            // Seed Reviews
            dao.insertReview(
                AdvocateReview(
                    advocateBarNo = "1042/1998",
                    clientName = "Subhash Chandra Tiwari",
                    caseNo = "ST 412/2024",
                    rating = 5,
                    comment = "Excellent legal strategy and aggressive bail argument in District Court Ballia. Highly respected lawyer.",
                    date = "02/09/2026"
                )
            )
            dao.insertReview(
                AdvocateReview(
                    advocateBarNo = "1042/1998",
                    clientName = "Kameshwar Singh",
                    caseNo = "Rev 88/2023",
                    rating = 5,
                    comment = "Solved our 12 year old land division dispute under UP Revenue Code within 4 hearings!",
                    date = "15/08/2026"
                )
            )
        }
    }
}
