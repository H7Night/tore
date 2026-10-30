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

package com.looker.kenko.ui.feature.exercise.progress

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.looker.kenko.data.repository.ExerciseRepo
import com.looker.kenko.data.repository.WeightRepo
import com.looker.kenko.domain.model.Exercise
import com.looker.kenko.domain.statistics.BodyWeightRecord
import com.looker.kenko.domain.statistics.ExerciseSessionEntry
import com.looker.kenko.domain.statistics.buildExerciseEntries
import com.looker.kenko.ui.base.KenkoViewModel
import com.looker.kenko.ui.feature.exercise.navigation.ExerciseProgressRoute
import com.looker.kenko.utils.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

@HiltViewModel
class ExerciseProgressViewModel @Inject constructor(
    private val repo: ExerciseRepo,
    private val weightRepo: WeightRepo,
    savedStateHandle: SavedStateHandle,
) : KenkoViewModel() {

    private val route: ExerciseProgressRoute = savedStateHandle.toRoute()
    private val exerciseId: Int = route.exerciseId

    private val _exercise = MutableStateFlow<Exercise?>(null)

    init {
        launchCatching { _exercise.value = repo.get(exerciseId) }
    }

    val state: StateFlow<ExerciseProgressUiData> = combine(
        _exercise,
        repo.streamExerciseSets(exerciseId),
        weightRepo.weights,
    ) { exercise, records, weights ->
        val bodyWeights = weights.map { BodyWeightRecord(it.date, it.value) }
        val entries = buildExerciseEntries(
            records = records,
            isBodyweight = exercise?.isBodyweight == true,
            bodyWeights = bodyWeights,
        )
        ExerciseProgressUiData(
            exercise = exercise,
            entries = entries,
            isLoading = exercise == null,
        )
    }.asStateFlow(ExerciseProgressUiData(), started = SharingStarted.Eagerly)
}

@Stable
data class ExerciseProgressUiData(
    val exercise: Exercise? = null,
    val entries: List<ExerciseSessionEntry> = emptyList(),
    val isLoading: Boolean = true,
)
