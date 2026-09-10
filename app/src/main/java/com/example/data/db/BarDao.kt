package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

@Dao
interface BarDao {
    // --- Advocate Management ---
    @Query("SELECT * FROM advocates ORDER BY createdAt DESC")
    fun getAllAdvocates(): Flow<List<Advocate>>

    @Query("SELECT * FROM advocates WHERE status = :status ORDER BY createdAt DESC")
    fun getAdvocatesByStatus(status: String): Flow<List<Advocate>>

    @Query("SELECT * FROM advocates WHERE barCouncilNo = :barNo LIMIT 1")
    suspend fun getAdvocateByBarNo(barNo: String): Advocate?

    @Query("SELECT * FROM advocates WHERE email = :email LIMIT 1")
    suspend fun getAdvocateByEmail(email: String): Advocate?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdvocate(advocate: Advocate): Long

    @Update
    suspend fun updateAdvocate(advocate: Advocate)

    @Query("UPDATE advocates SET status = :status, validTill = :validTill WHERE id = :id")
    suspend fun updateAdvocateStatus(id: Long, status: String, validTill: String)

    @Query("UPDATE advocates SET membershipFeePaid = 1, membershipExpiryDate = :expiryDate WHERE barCouncilNo = :barNo")
    suspend fun updateMembershipPayment(barNo: String, expiryDate: String)

    @Delete
    suspend fun deleteAdvocate(advocate: Advocate)

    // --- Client Management ---
    @Query("SELECT * FROM clients WHERE advocateBarNo = :advocateBarNo ORDER BY id DESC")
    fun getClientsForAdvocate(advocateBarNo: String): Flow<List<Client>>

    @Query("SELECT * FROM clients ORDER BY id DESC")
    fun getAllClients(): Flow<List<Client>>

    @Query("SELECT * FROM clients WHERE id = :id LIMIT 1")
    suspend fun getClientById(id: Long): Client?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: Client): Long

    @Update
    suspend fun updateClient(client: Client)

    @Delete
    suspend fun deleteClient(client: Client)

    // --- Finance Entries ---
    @Query("SELECT * FROM finance_entries ORDER BY timestamp DESC")
    fun getAllFinanceEntries(): Flow<List<FinanceEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinanceEntry(entry: FinanceEntry): Long

    @Delete
    suspend fun deleteFinanceEntry(entry: FinanceEntry)

    // --- Cause List ---
    @Query("SELECT * FROM cause_list ORDER BY date DESC, courtNo ASC, itemNo ASC")
    fun getAllCauseList(): Flow<List<CauseListEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCauseListEntry(entry: CauseListEntry): Long

    @Delete
    suspend fun deleteCauseListEntry(entry: CauseListEntry)

    // --- Bare Acts ---
    @Query("SELECT * FROM bare_acts ORDER BY id ASC")
    fun getAllBareActs(): Flow<List<BareAct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBareAct(act: BareAct): Long

    @Delete
    suspend fun deleteBareAct(act: BareAct)

    // --- Committee ---
    @Query("SELECT * FROM committee_members ORDER BY isCurrent DESC, id ASC")
    fun getAllCommitteeMembers(): Flow<List<CommitteeMember>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommitteeMember(member: CommitteeMember): Long

    @Delete
    suspend fun deleteCommitteeMember(member: CommitteeMember)

    // --- Projects ---
    @Query("SELECT * FROM bar_projects ORDER BY id ASC")
    fun getAllProjects(): Flow<List<BarProject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: BarProject): Long

    @Delete
    suspend fun deleteProject(project: BarProject)

    // --- Junior Senior Connect ---
    @Query("SELECT * FROM junior_senior_posts ORDER BY timestamp DESC")
    fun getAllJuniorSeniorPosts(): Flow<List<JuniorSeniorPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJuniorSeniorPost(post: JuniorSeniorPost): Long

    @Delete
    suspend fun deleteJuniorSeniorPost(post: JuniorSeniorPost)

    // --- Chat Messages ---
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllChatMessages(): Flow<List<ChatMessage>>

    @Query("SELECT * FROM chat_messages WHERE (senderBarNo = :barNo1 AND recipientBarNo = :barNo2) OR (senderBarNo = :barNo2 AND recipientBarNo = :barNo1) OR recipientBarNo = 'ALL' ORDER BY timestamp ASC")
    fun getChatBetween(barNo1: String, barNo2: String): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(msg: ChatMessage): Long

    // --- Notifications ---
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<AppNotification>>

    @Query("SELECT * FROM notifications WHERE targetBarNo = 'ALL' OR targetBarNo = :barNo ORDER BY timestamp DESC")
    fun getNotificationsForAdvocate(barNo: String): Flow<List<AppNotification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: AppNotification): Long

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markNotificationRead(id: Long)

    // --- Reviews ---
    @Query("SELECT * FROM advocate_reviews WHERE advocateBarNo = :barNo ORDER BY id DESC")
    fun getReviewsForAdvocate(barNo: String): Flow<List<AdvocateReview>>

    @Query("SELECT * FROM advocate_reviews ORDER BY id DESC")
    fun getAllReviews(): Flow<List<AdvocateReview>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: AdvocateReview): Long

    // --- Emergency Requests ---
    @Query("SELECT * FROM emergency_requests ORDER BY timestamp DESC")
    fun getAllEmergencyRequests(): Flow<List<EmergencyRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencyRequest(req: EmergencyRequest): Long

    @Query("UPDATE emergency_requests SET status = :status WHERE id = :id")
    suspend fun updateEmergencyStatus(id: Long, status: String)

    // --- Settings ---
    @Query("SELECT * FROM association_settings WHERE `key` = 'main' LIMIT 1")
    fun getSettings(): Flow<AssociationSettings?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: AssociationSettings)
}
