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

package com.looker.kenko.domain.model

import androidx.annotation.StringRes
import com.looker.kenko.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("exercise")
data class Exercise(
    val name: String,
    val tags: List<Tag> = emptyList(),
    val countType: CountType = CountType.REPS,
    val isBodyweight: Boolean = false,
    val reference: String? = null,
    val equipment: ExerciseEquipment? = null,
    /** 主肌群 tag id；其余 tags 视为次肌群。 */
    val primaryTagId: Int? = null,
    val id: Int? = null,
) {
    /** 主肌群 tag（若 [primaryTagId] 有效且存在）；否则取第一个标签作回退。 */
    val primaryTag: Tag?
        get() = tags.firstOrNull { it.id == primaryTagId } ?: tags.firstOrNull()
}

val Exercise.repDurationStringRes: Int
    @StringRes
    get() = when (countType) {
        CountType.MINUTES -> R.string.label_min
        else -> R.string.label_reps
    }
