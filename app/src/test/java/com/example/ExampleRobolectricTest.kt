package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.database.VaultDatabase
import com.example.data.repository.VaultRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Vault", appName)
    }

    @Test
    fun `database initial seeding and read test`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val database = VaultDatabase.getInstance(context)
        val repository = VaultRepositoryImpl(
            credentialDao = database.credentialDao(),
            categoryDao = database.categoryDao()
        )

        repository.seedInitialDataIfEmpty()
        val list = repository.getAllCredentials().first()

        assertTrue(list.isNotEmpty())
        assertTrue(list.any { it.service.equals("Google", ignoreCase = true) })
    }
}
