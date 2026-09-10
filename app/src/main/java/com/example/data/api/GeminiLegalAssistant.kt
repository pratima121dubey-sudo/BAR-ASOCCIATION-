package com.example.data.api

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiLegalAssistant {

    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val SYSTEM_PROMPT =
        "You are UP Criminal & Revenue Law Assistant for the Criminal & Revenue Bar Association, Ballia. Answer with relevant statutory section numbers (e.g., Bharatiya Nyaya Sanhita (BNS)/IPC, Bharatiya Nagarik Suraksha Sanhita (BNSS)/CrPC, UP Revenue Code 2006, CPC, Bharatiya Sakshya Adhiniyam) briefly, clearly, and authoritatively."

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun askLegalAssistant(userQuery: String): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val url = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent?key=$apiKey"

                val jsonPayload = JSONObject().apply {
                    val contentsArr = JSONArray().apply {
                        val contentObj = JSONObject().apply {
                            val partsArr = JSONArray().apply {
                                put(JSONObject().put("text", userQuery))
                            }
                            put("parts", partsArr)
                        }
                        put(contentObj)
                    }
                    put("contents", contentsArr)

                    val systemObj = JSONObject().apply {
                        val sysParts = JSONArray().apply {
                            put(JSONObject().put("text", SYSTEM_PROMPT))
                        }
                        put("parts", sysParts)
                    }
                    put("systemInstruction", systemObj)

                    val genConfig = JSONObject().apply {
                        put("temperature", 0.3)
                        put("maxOutputTokens", 800)
                    }
                    put("generationConfig", genConfig)
                }

                val mediaType = "application/json; charset=utf-8".toMediaType()
                val requestBody = jsonPayload.toString().toRequestBody(mediaType)

                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val bodyString = response.body?.string() ?: ""
                        val rootJson = JSONObject(bodyString)
                        val candidates = rootJson.optJSONArray("candidates")
                        if (candidates != null && candidates.length() > 0) {
                            val content = candidates.getJSONObject(0).optJSONObject("content")
                            val parts = content?.optJSONArray("parts")
                            if (parts != null && parts.length() > 0) {
                                val text = parts.getJSONObject(0).optString("text")
                                if (text.isNotBlank()) {
                                    return@withContext text
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // Fall back to offline legal engine
            }
        }

        // UP Criminal & Revenue Legal Knowledge Engine Fallback
        generateLocalLegalResponse(userQuery)
    }

    private fun generateLocalLegalResponse(query: String): String {
        val q = query.lowercase()
        return when {
            q.contains("bail") || q.contains("zamanat") || q.contains("जमानत") -> {
                """
                ⚖️ **Statutory Provisions on Bail in UP:**
                1. **Regular Bail**: Section 483 BNSS (erstwhile Sec 437/439 CrPC) - Filed before Sessions Court or High Court.
                2. **Anticipatory Bail**: Section 482 BNSS (erstwhile Sec 438 CrPC) - Restored in Uttar Pradesh by UP Act No. 4 of 2019. Can be filed before District & Sessions Court, Ballia.
                3. **Default Bail**: Section 187 BNSS (erstwhile Sec 167(2) CrPC) - Entitled if charge-sheet is not filed within 60 or 90 days.
                4. **Bail for Undertrials**: Section 479 BNSS - First-time offenders eligible for bail after serving 1/3rd of maximum imprisonment.
                """.trimIndent()
            }
            q.contains("batwara") || q.contains("partition") || q.contains("बंटवारा") || q.contains("hissa") -> {
                """
                ⚖️ **Land Partition (Batwara) under UP Revenue Code, 2006:**
                1. **Section 116 UP Revenue Code**: Suit for division/partition of holdings filed before Sub-Divisional Officer (SDO/SDM).
                2. **Section 117 UP Revenue Code**: SDO declares preliminary decree defining shares of co-tenure holders.
                3. **Lekhpal Qurra Preparation**: Revenue Inspector/Lekhpal prepares field lot demarcation (Qurra).
                4. **Final Decree & Dakhil Kharij**: After hearing objections, final decree is sealed and revenue map (Shajra) modified.
                """.trimIndent()
            }
            q.contains("dakhil") || q.contains("mutation") || q.contains("दाखिल खारिज") || q.contains("namantaran") -> {
                """
                ⚖️ **Mutation / Dakhil Kharij under UP Revenue Code, 2006:**
                1. **Section 34 UP Revenue Code**: Mandatory report of succession or transfer of land by registered sale deed, gift, or will.
                2. **Section 35 UP Revenue Code**: Procedure on report - Tehsildar issues 30-day proclamation (Ishtehar) for public objection.
                3. **Undisputed Mutation**: Disposed of by Revenue Inspector within 35 days under Rule 33.
                4. **Contested Mutation**: Disposed of by Tehsildar / Naib Tehsildar within 90 days.
                """.trimIndent()
            }
            q.contains("demarcation") || q.contains("seemankan") || q.contains("पैमाइश") || q.contains("boundary") || q.contains("medh") -> {
                """
                ⚖️ **Boundary Settlement & Demarcation (Seemankan):**
                1. **Section 24 UP Revenue Code, 2006**: Application for demarcation/paimash of agricultural boundary filed before SDM with challan fee (₹1000 per gata).
                2. **Revenue Survey**: Revenue Inspector with Amin conducts field measurement using fixed survey stations (Sihadda/Duhadda).
                3. **Section 24(2)**: SDM confirms demarcation report and orders fixing of boundary pillars (Medhbandi).
                """.trimIndent()
            }
            q.contains("eviction") || q.contains("kabza") || q.contains("gaon sabha") || q.contains("67") -> {
                """
                ⚖️ **Eviction from Gram Sabha / Public Land:**
                1. **Section 67 UP Revenue Code, 2006**: Tehsildar initiates proceedings against unauthorized occupation/encroachment of Gaon Sabha, pond, pasture, or pathway land.
                2. **Form RC-20**: Notice issued to encroacher to show cause and pay damages.
                3. **Appeal**: Appeal lies before Collector/DM under Section 67(5) within 30 days.
                """.trimIndent()
            }
            q.contains("murder") || q.contains("302") || q.contains("103") || q.contains("hatya") || q.contains("हत्या") -> {
                """
                ⚖️ **Murder & Culpable Homicide Provisions:**
                1. **Section 103(1) Bharatiya Nyaya Sanhita (BNS)** (erstwhile Sec 302 IPC): Punishment for murder — Death or imprisonment for life, and fine.
                2. **Section 103(2) BNS**: Lynching by a group of five or more persons on grounds of race, caste, sex, place of birth — Death or life imprisonment.
                3. **Section 105 BNS** (erstwhile Sec 304 IPC): Culpable homicide not amounting to murder.
                """.trimIndent()
            }
            q.contains("fir") || q.contains("police") || q.contains("156(3)") || q.contains("175(3)") || q.contains("complaint") -> {
                """
                ⚖️ **FIR & Judicial Complaint Procedure:**
                1. **Section 173 BNSS** (erstwhile Sec 154 CrPC): Registration of Information in cognizable offenses, including Zero FIR & e-FIR.
                2. **Section 175(3) BNSS** (erstwhile Sec 156(3) CrPC): Application before Judicial Magistrate for direction to police to investigate cognizable crime (requires affidavit of compliance with Sec 173(4)).
                3. **Section 223 BNSS** (erstwhile Sec 200 CrPC): Criminal complaint before Magistrate with examination of complainant.
                """.trimIndent()
            }
            q.contains("maintenance") || q.contains("125") || q.contains("144") || q.contains("k خرچہ") || q.contains("guzara") -> {
                """
                ⚖️ **Maintenance for Wife, Children & Parents:**
                1. **Section 144 BNSS** (erstwhile Section 125 CrPC): Order for monthly allowance for maintenance of wife, legitimate/illegitimate minor children, and aged parents.
                2. **Interim Maintenance**: Section 144(2) BNSS — Application for interim maintenance to be disposed of within 60 days of notice.
                3. **Enforcement**: Warrant of recovery as fine and imprisonment up to 1 month for default.
                """.trimIndent()
            }
            q.contains("injunction") || q.contains("stay") || q.contains("39") || q.contains("rok") || q.contains("रोक") -> {
                """
                ⚖️ **Civil Injunction & Stay Orders (CPC):**
                1. **Order 39 Rule 1 & 2 CPC**: Temporary injunction to restrain damage, alienation, or wrongful dispossession of property.
                2. **Three Golden Principles**: (a) Prima facie case, (b) Irreparable loss, (c) Balance of convenience.
                3. **Order 39 Rule 3A CPC**: Court must dispose of ex-parte ad-interim injunction application within 30 days.
                4. **Section 151 CPC**: Inherent powers of civil court to prevent abuse of process.
                """.trimIndent()
            }
            else -> {
                """
                ⚖️ **Legal Analysis & Statutory Provisions:**
                - For **Criminal Matters**: Governed by Bharatiya Nyaya Sanhita (BNS), Bharatiya Nagarik Suraksha Sanhita (BNSS), and Bharatiya Sakshya Adhiniyam (BSA).
                - For **Revenue Matters in UP**: Governed by the Uttar Pradesh Revenue Code, 2006 (Sections 24 for Demarcation, 34 for Mutation, 116 for Partition, 144 for Declaratory Suits, and 209 for Land Bar).
                - For **Civil Practice**: Governed by Code of Civil Procedure, 1908 (CPC) and Specific Relief Act, 1963.
                
                *Please ask specific queries regarding Bail, Demarcation, Mutation, Partition, FIR, or Injunction.*
                """.trimIndent()
            }
        }
    }
}
