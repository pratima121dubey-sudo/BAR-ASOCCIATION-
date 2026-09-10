package com.example.data.repository

import com.example.data.db.BarDao
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
import kotlinx.coroutines.flow.Flow

class BarRepository(private val dao: BarDao) {
    // Advocates
    val allAdvocates: Flow<List<Advocate>> = dao.getAllAdvocates()
    val approvedAdvocates: Flow<List<Advocate>> = dao.getAdvocatesByStatus("APPROVED")
    val pendingAdvocates: Flow<List<Advocate>> = dao.getAdvocatesByStatus("PENDING")

    suspend fun getAdvocateByBarNo(barNo: String): Advocate? = dao.getAdvocateByBarNo(barNo)
    suspend fun getAdvocateByEmail(email: String): Advocate? = dao.getAdvocateByEmail(email)
    suspend fun registerAdvocate(advocate: Advocate): Long = dao.insertAdvocate(advocate)
    suspend fun updateAdvocate(advocate: Advocate) = dao.updateAdvocate(advocate)
    suspend fun updateAdvocateStatus(id: Long, status: String, validTill: String) =
        dao.updateAdvocateStatus(id, status, validTill)
    suspend fun updateMembershipPayment(barNo: String, expiryDate: String) =
        dao.updateMembershipPayment(barNo, expiryDate)
    suspend fun deleteAdvocate(advocate: Advocate) = dao.deleteAdvocate(advocate)

    // Clients
    fun getClientsForAdvocate(barNo: String): Flow<List<Client>> = dao.getClientsForAdvocate(barNo)
    val allClients: Flow<List<Client>> = dao.getAllClients()
    suspend fun getClientById(id: Long): Client? = dao.getClientById(id)
    suspend fun insertClient(client: Client): Long = dao.insertClient(client)
    suspend fun updateClient(client: Client) = dao.updateClient(client)
    suspend fun deleteClient(client: Client) = dao.deleteClient(client)

    // Finance
    val allFinance: Flow<List<FinanceEntry>> = dao.getAllFinanceEntries()
    suspend fun insertFinanceEntry(entry: FinanceEntry): Long = dao.insertFinanceEntry(entry)
    suspend fun deleteFinanceEntry(entry: FinanceEntry) = dao.deleteFinanceEntry(entry)

    // Cause List
    val allCauseList: Flow<List<CauseListEntry>> = dao.getAllCauseList()
    suspend fun insertCauseListEntry(entry: CauseListEntry): Long = dao.insertCauseListEntry(entry)
    suspend fun deleteCauseListEntry(entry: CauseListEntry) = dao.deleteCauseListEntry(entry)

    // Bare Acts
    val allBareActs: Flow<List<BareAct>> = dao.getAllBareActs()
    suspend fun insertBareAct(act: BareAct): Long = dao.insertBareAct(act)
    suspend fun deleteBareAct(act: BareAct) = dao.deleteBareAct(act)

    // Committee & Projects
    val allCommittee: Flow<List<CommitteeMember>> = dao.getAllCommitteeMembers()
    suspend fun insertCommitteeMember(member: CommitteeMember): Long = dao.insertCommitteeMember(member)
    suspend fun deleteCommitteeMember(member: CommitteeMember) = dao.deleteCommitteeMember(member)

    val allProjects: Flow<List<BarProject>> = dao.getAllProjects()
    suspend fun insertProject(project: BarProject): Long = dao.insertProject(project)
    suspend fun deleteProject(project: BarProject) = dao.deleteProject(project)

    // Junior Senior Posts
    val allJuniorSeniorPosts: Flow<List<JuniorSeniorPost>> = dao.getAllJuniorSeniorPosts()
    suspend fun insertJuniorSeniorPost(post: JuniorSeniorPost): Long = dao.insertJuniorSeniorPost(post)
    suspend fun deleteJuniorSeniorPost(post: JuniorSeniorPost) = dao.deleteJuniorSeniorPost(post)

    // Chat
    val allChatMessages: Flow<List<ChatMessage>> = dao.getAllChatMessages()
    fun getChatBetween(barNo1: String, barNo2: String): Flow<List<ChatMessage>> =
        dao.getChatBetween(barNo1, barNo2)
    suspend fun sendChatMessage(msg: ChatMessage): Long = dao.insertChatMessage(msg)

    // Notifications
    val allNotifications: Flow<List<AppNotification>> = dao.getAllNotifications()
    fun getNotificationsForAdvocate(barNo: String): Flow<List<AppNotification>> =
        dao.getNotificationsForAdvocate(barNo)
    suspend fun insertNotification(notification: AppNotification): Long = dao.insertNotification(notification)
    suspend fun markNotificationRead(id: Long) = dao.markNotificationRead(id)

    // Reviews
    fun getReviewsForAdvocate(barNo: String): Flow<List<AdvocateReview>> = dao.getReviewsForAdvocate(barNo)
    val allReviews: Flow<List<AdvocateReview>> = dao.getAllReviews()
    suspend fun insertReview(review: AdvocateReview): Long = dao.insertReview(review)

    // Emergency Requests
    val allEmergencyRequests: Flow<List<EmergencyRequest>> = dao.getAllEmergencyRequests()
    suspend fun insertEmergencyRequest(req: EmergencyRequest): Long = dao.insertEmergencyRequest(req)
    suspend fun updateEmergencyStatus(id: Long, status: String) = dao.updateEmergencyStatus(id, status)

    // Settings
    val settings: Flow<AssociationSettings?> = dao.getSettings()
    suspend fun saveSettings(settings: AssociationSettings) = dao.saveSettings(settings)
}
