/*
 * Copyright (C) 2026 H7Night <h7night@gmail.com>
 * This file is part of Kenko (GPL-3.0-or-later).
 */

package com.looker.kenko.ui.feature.backup

import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.looker.kenko.data.backup.BackupManager
import com.looker.kenko.data.backup.BackupResult
import com.looker.kenko.data.export.ExportManager
import com.looker.kenko.data.export.ExportOptions
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.data.repository.SettingsRepo
import com.looker.kenko.domain.model.settings.BackupInterval
import com.looker.kenko.ui.base.KenkoViewModel
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

/** 备份、恢复与数据导出（设置页 + 备份页共用）。 */
@HiltViewModel
class BackupViewModel @Inject constructor(
    private val repo: SettingsRepo,
    private val sessionRepo: SessionRepo,
    private val backupManager: BackupManager,
    private val exportManager: ExportManager,
) : KenkoViewModel() {

    private val _operation = MutableStateFlow(BackupOperationState())

    val state: StateFlow<BackupUiData> = combine(
        repo.stream,
        _operation,
        sessionRepo.earliestSessionDate,
    ) { settings, operation, earliestDate ->
        BackupUiData(
            backupUri = settings.backupUri,
            backupInterval = settings.backupInterval,
            lastBackupTime = settings.lastBackupTime,
            isBackingUp = operation.isBackingUp,
            isRestoring = operation.isRestoring,
            isExporting = operation.isExporting,
            backupMessage = operation.message,
            earliestSessionDate = earliestDate,
        )
    }.asStateFlow(BackupUiData())

    fun setBackupLocation(uri: Uri) {
        launchCatching {
            repo.setBackupUri(uri.toString())
            backupManager.schedulePeriodicBackup(state.value.backupInterval, uri)
        }
    }

    fun setBackupInterval(interval: BackupInterval) {
        launchCatching {
            repo.setBackupInterval(interval)
            val backupUri = state.value.backupUri?.toUri()
            if (backupUri != null) {
                backupManager.schedulePeriodicBackup(interval, backupUri)
            }
        }
    }

    fun backupNow() {
        val backupUri = state.value.backupUri?.toUri() ?: return

        viewModelScope.launch {
            try {
                _operation.update { it.copy(isBackingUp = true, message = null) }

                when (backupManager.createBackup(backupUri)) {
                    is BackupResult.Success -> {
                        repo.setLastBackupTime(Clock.System.now())
                        _operation.update {
                            it.copy(isBackingUp = false, message = BackupMessage.BackupSuccess)
                        }
                    }

                    is BackupResult.Error -> {
                        _operation.update {
                            it.copy(isBackingUp = false, message = BackupMessage.BackupFailed)
                        }
                    }
                }
            } catch (e: Exception) {
                _operation.update { it.copy(isBackingUp = false) }
                emitError(e)
            }
        }
    }

    fun restore(uri: Uri) {
        viewModelScope.launch {
            try {
                _operation.update { it.copy(isRestoring = true, message = null) }

                when (backupManager.restoreBackup(uri)) {
                    is BackupResult.Success -> {
                        repo.setBackupUri(null)
                        repo.setLastBackupTime(null)
                        _operation.update {
                            it.copy(isRestoring = false, message = BackupMessage.RestoreSuccess)
                        }
                    }

                    is BackupResult.Error -> {
                        _operation.update {
                            it.copy(isRestoring = false, message = BackupMessage.RestoreFailed)
                        }
                    }
                }
            } catch (e: Exception) {
                _operation.update { it.copy(isRestoring = false) }
                emitError(e)
            }
        }
    }

    fun exportData(options: ExportOptions, uri: Uri) {
        viewModelScope.launch {
            try {
                _operation.update { it.copy(isExporting = true, message = null) }

                when (exportManager.export(options, uri)) {
                    is BackupResult.Success -> {
                        _operation.update {
                            it.copy(isExporting = false, message = BackupMessage.ExportSuccess)
                        }
                    }

                    is BackupResult.Error -> {
                        _operation.update {
                            it.copy(isExporting = false, message = BackupMessage.ExportFailed)
                        }
                    }
                }
            } catch (e: Exception) {
                _operation.update { it.copy(isExporting = false) }
                emitError(e)
            }
        }
    }

    fun clearBackupMessage() {
        _operation.update { it.copy(message = null) }
    }
}

private data class BackupOperationState(
    val isBackingUp: Boolean = false,
    val isRestoring: Boolean = false,
    val isExporting: Boolean = false,
    val message: BackupMessage? = null,
)

enum class BackupMessage {
    BackupSuccess,
    BackupFailed,
    RestoreSuccess,
    RestoreFailed,
    ExportSuccess,
    ExportFailed,
}

@Stable
data class BackupUiData(
    val backupUri: String? = null,
    val backupInterval: BackupInterval = BackupInterval.Off,
    val lastBackupTime: Instant? = null,
    val isBackingUp: Boolean = false,
    val isRestoring: Boolean = false,
    val isExporting: Boolean = false,
    val backupMessage: BackupMessage? = null,
    val earliestSessionDate: LocalDate? = null,
)
