/*
 * Copyright (C) 2025 LooKeR & Contributors
 * Copyright (C) 2026 H7Night <h7night@gmail.com>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.looker.kenko.ui.feature.settings

import androidx.compose.runtime.Stable
import com.looker.kenko.data.health.HealthConnectManager
import com.looker.kenko.data.repository.SessionRepo
import com.looker.kenko.data.repository.SettingsRepo
import com.looker.kenko.domain.health.excludingActiveSession
import com.looker.kenko.domain.health.toHealthExerciseSession
import com.looker.kenko.domain.model.settings.ColorPalette
import com.looker.kenko.domain.model.settings.Language
import com.looker.kenko.domain.model.settings.Theme
import com.looker.kenko.ui.base.KenkoViewModel
import com.looker.kenko.ui.component.timer.TrainingSessionManager
import com.looker.kenko.ui.component.timer.TrainingSessionState
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

/** 偏好设置与 Health Connect；备份/导出见 [com.looker.kenko.ui.feature.backup.BackupViewModel]。 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repo: SettingsRepo,
    private val sessionRepo: SessionRepo,
    private val healthConnectManager: HealthConnectManager,
    private val trainingSessionManager: TrainingSessionManager,
) : KenkoViewModel() {

    private val _healthSyncCount = MutableStateFlow<Int?>(null)

    private val healthConnectAvailable = healthConnectManager.isAvailable()

    val state: StateFlow<SettingsUiData> = combine(
        repo.stream,
        _healthSyncCount,
    ) { settings, healthSyncCount ->
        SettingsUiData(
            selectedTheme = settings.theme,
            language = settings.language,
            bodyWeightEnabled = settings.bodyWeightEnabled,
            healthConnectEnabled = settings.healthConnectEnabled,
            healthConnectAvailable = healthConnectAvailable,
            healthConnectPermissions = healthConnectManager.permissions,
            healthConnectSyncCount = healthSyncCount,
            colorPalette = settings.colorPalette,
        )
    }.asStateFlow(
        SettingsUiData(
            selectedTheme = Theme.System,
            language = Language.System,
            bodyWeightEnabled = true,
            healthConnectEnabled = false,
            healthConnectAvailable = false,
            healthConnectPermissions = emptySet(),
            healthConnectSyncCount = null,
            colorPalette = ColorPalette.Material,
        ),
    )
    fun updateTheme(theme: Theme) {
        launchCatching {
            repo.setTheme(theme)
        }
    }

    fun updateLanguage(language: Language) {
        launchCatching {
            repo.setLanguage(language)
        }
    }

    fun updateBodyWeightEnabled(enabled: Boolean) {
        launchCatching {
            repo.setBodyWeightEnabled(enabled)
        }
    }

    fun updateColorPalette(palette: ColorPalette) {
        launchCatching {
            repo.setColorPalette(palette)
        }
    }

    fun updateHealthConnectEnabled(enabled: Boolean) {
        launchCatching {
            repo.setHealthConnectEnabled(enabled)
            if (enabled) syncHealthConnect()
        }
    }

    fun onHealthPermissionsResult(granted: Boolean) {
        launchCatching {
            repo.setHealthConnectEnabled(granted)
            if (granted) syncHealthConnect()
        }
    }

    fun syncHealthConnect() {
        launchCatching {
            if (!healthConnectManager.hasAllPermissions()) return@launchCatching
            val activeSessionId =
                (trainingSessionManager.sessionState.value as? TrainingSessionState.Active)?.sessionId
            val dtos = sessionRepo.stream.first()
                .excludingActiveSession(activeSessionId)
                .mapNotNull { it.toHealthExerciseSession(it.performExercises.map { ex -> ex.name }) }
            _healthSyncCount.value = healthConnectManager.syncAll(dtos)
        }
    }
}


@Stable
data class SettingsUiData(
    val selectedTheme: Theme,
    val language: Language,
    val bodyWeightEnabled: Boolean,
    val healthConnectEnabled: Boolean,
    val healthConnectAvailable: Boolean,
    val healthConnectPermissions: Set<String>,
    val healthConnectSyncCount: Int?,
    val colorPalette: ColorPalette,
)