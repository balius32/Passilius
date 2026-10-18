package com.example.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class SecurityReportTest {

    @Test
    fun emptyVaultIsEmpty() {
        val report = SecurityReport.from(emptyList())
        assertEquals(0, report.totalCount)
        assertEquals(0, report.strongCount)
        assertEquals(0, report.reusedCount)
        assertEquals(0, report.securityScorePercentage)
        assertEquals("Empty", report.gradeLabel)
    }

    @Test
    fun uniqueMilitaryPasswordsScoreFull() {
        val report = SecurityReport.from(
            listOf(
                credential("one", entropyBits = 100),
                credential("two", entropyBits = 96)
            )
        )
        assertEquals(2, report.totalCount)
        assertEquals(2, report.strongCount)
        assertEquals(0, report.weakCount)
        assertEquals(0, report.reusedCount)
        assertEquals(100, report.securityScorePercentage)
        assertEquals("Military-Grade", report.gradeLabel)
    }

    @Test
    fun uniqueStrongPasswordsAreStrong() {
        val report = SecurityReport.from(
            listOf(
                credential("alpha", entropyBits = 70),
                credential("beta", entropyBits = 80)
            )
        )
        assertEquals(2, report.strongCount)
        assertEquals(0, report.reusedCount)
        assertEquals(100, report.securityScorePercentage)
        assertEquals("Strong", report.gradeLabel)
    }

    @Test
    fun reusedPasswordsAreCountedAndNotScoredAsSecure() {
        val report = SecurityReport.from(
            listOf(
                credential("same-secret", entropyBits = 100),
                credential("same-secret", entropyBits = 100),
                credential("unique", entropyBits = 100)
            )
        )
        assertEquals(3, report.totalCount)
        assertEquals(3, report.strongCount)
        assertEquals(2, report.reusedCount)
        assertEquals(33, report.securityScorePercentage)
        assertEquals("Weak", report.gradeLabel)
    }

    @Test
    fun mixedStrengthIsFairWhenHalfAreSecure() {
        val report = SecurityReport.from(
            listOf(
                credential("weak-one", entropyBits = 20),
                credential("strong-one", entropyBits = 100)
            )
        )
        assertEquals(1, report.strongCount)
        assertEquals(1, report.weakCount)
        assertEquals(50, report.securityScorePercentage)
        assertEquals("Fair", report.gradeLabel)
    }

    @Test
    fun missingEntropyIsCalculatedFromThePassword() {
        val report = SecurityReport.from(
            listOf(credential("a", entropyBits = 0))
        )
        assertEquals(1, report.totalCount)
        assertEquals(0, report.strongCount)
        assertEquals(1, report.weakCount)
        assertEquals("Weak", report.gradeLabel)
    }

    private fun credential(password: String, entropyBits: Int) = Credential(
        service = password,
        username = "user",
        password = password,
        entropyBits = entropyBits
    )
}
