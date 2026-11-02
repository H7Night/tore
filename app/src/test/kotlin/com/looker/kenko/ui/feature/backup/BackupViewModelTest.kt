package com.looker.kenko.ui.feature.backup

import android.net.Uri
import com.looker.kenko.data.backup.BackupManager
import com.looker.kenko.data.backup.BackupResult
import com.looker.kenko.data.export.ExportManager
import com.looker.kenko.data.export.ExportOptions
import com.looker.kenko.domain.model.settings.BackupInterval
import com.looker.kenko.testutil.FakeSessionRepo
import com.looker.kenko.testutil.FakeSettingsRepo
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class BackupViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() = Dispatchers.setMain(dispatcher)

    @AfterTest
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `initial state is idle with no backup location`() = runTest(dispatcher) {
        val vm = viewModel()
        keepSubscribed(vm)
        advanceUntilIdle()
        val state = vm.state.value
        assertNull(state.backupUri)
        assertEquals(BackupInterval.Off, state.backupInterval)
        assertNull(state.backupMessage)
        assertEquals(false, state.isBackingUp)
    }

    @Test
    fun `backupNow without a location does not call the backup manager`() = runTest(dispatcher) {
        val manager = FakeBackupManager()
        val vm = viewModel(backupManager = manager)
        keepSubscribed(vm)
        advanceUntilIdle()

        vm.backupNow()
        advanceUntilIdle()

        assertEquals(0, manager.createCount)
        assertEquals(false, vm.state.value.isBackingUp)
    }

    @Test
    fun `setBackupInterval without a location stores the interval only`() = runTest(dispatcher) {
        val manager = FakeBackupManager()
        val vm = viewModel(backupManager = manager)
        keepSubscribed(vm)
        advanceUntilIdle()

        vm.setBackupInterval(BackupInterval.Weekly)
        advanceUntilIdle()

        assertEquals(BackupInterval.Weekly, vm.state.value.backupInterval)
        assertEquals(0, manager.scheduleCount)
    }

    @Test
    fun `clearBackupMessage keeps the other operation flags`() = runTest(dispatcher) {
        val vm = viewModel()
        keepSubscribed(vm)
        advanceUntilIdle()

        vm.clearBackupMessage()
        advanceUntilIdle()

        val state = vm.state.value
        assertNull(state.backupMessage)
        assertEquals(false, state.isExporting)
    }

    /** stateIn(WhileSubscribed) only refreshes while subscribed, so keep a collector alive. */
    private fun TestScope.keepSubscribed(vm: BackupViewModel) {
        backgroundScope.launch { vm.state.collect {} }
    }

    private fun viewModel(
        settings: FakeSettingsRepo = FakeSettingsRepo(),
        backupManager: BackupManager = FakeBackupManager(),
        exportManager: ExportManager = FakeExportManager(),
    ) = BackupViewModel(
        repo = settings,
        sessionRepo = FakeSessionRepo(),
        backupManager = backupManager,
        exportManager = exportManager,
    )

    private class FakeBackupManager : BackupManager {
        var createCount = 0
        var scheduleCount = 0

        override suspend fun createBackup(destinationUri: Uri): BackupResult {
            createCount++
            return BackupResult.Success
        }

        override suspend fun restoreBackup(sourceUri: Uri): BackupResult = BackupResult.Success

        override fun schedulePeriodicBackup(interval: BackupInterval, destinationUri: Uri) {
            scheduleCount++
        }

        override fun cancelScheduledBackup() = Unit
    }

    private class FakeExportManager : ExportManager {
        override suspend fun export(options: ExportOptions, destinationUri: Uri): BackupResult =
            BackupResult.Success
    }
}
