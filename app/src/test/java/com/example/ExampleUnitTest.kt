package com.example

import com.example.core.crypto.GeneratorConfig
import com.example.core.crypto.PasswordGenerator
import com.example.core.crypto.PasswordStrengthLevel
import com.example.core.crypto.TotpManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class VaultCoreUnitTest {

    @Test
    fun testPasswordGeneratorLengthAndComplexity() {
        val config = GeneratorConfig(
            length = 20,
            includeUppercase = true,
            includeLowercase = true,
            includeNumbers = true,
            includeSymbols = true
        )
        val generated = PasswordGenerator.generate(config)

        assertEquals(20, generated.password.length)
        assertTrue(generated.entropyBits > 90)
        assertEquals(PasswordStrengthLevel.VERY_STRONG, generated.strengthLevel)
        assertTrue(generated.password.any { it.isUpperCase() })
        assertTrue(generated.password.any { it.isLowerCase() })
        assertTrue(generated.password.any { it.isDigit() })
    }

    @Test
    fun testEntropyCalculation() {
        val weakEntropy = PasswordGenerator.calculateEntropy("123456")
        val strongEntropy = PasswordGenerator.calculateEntropy("g%Q2gXJ(|h^yL;Ga")

        assertTrue(weakEntropy < 30)
        assertTrue(strongEntropy >= 90)
        assertEquals(PasswordStrengthLevel.WEAK, PasswordGenerator.evaluateStrength(weakEntropy))
        assertEquals(PasswordStrengthLevel.VERY_STRONG, PasswordGenerator.evaluateStrength(strongEntropy))
    }

    @Test
    fun testTotpGeneration() {
        val sampleSecret = "JBSWY3DPEHPK3PXP"
        val code = TotpManager.getCurrentTotpCode(sampleSecret, 1600000000L)
        val remaining = TotpManager.getRemainingSeconds(1600000000L)

        assertNotNull(code)
        assertEquals(6, code.length)
        assertTrue(remaining in 1..30)
    }
}
