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

import com.looker.kenko.domain.model.Labels.Difficulty
import com.looker.kenko.domain.model.Labels.Equipment
import com.looker.kenko.domain.model.Labels.Focus
import com.looker.kenko.domain.model.Labels.Time
import kotlin.time.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class Plan(
    val name: String,
    val description: String?,
    val difficulty: Difficulty?,
    val focus: Focus?,
    val equipment: Equipment?,
    val time: Time?,
    val isActive: Boolean,
    val stat: PlanStat = PlanStat(0, 0),
    val dayTitles: String? = null,
    val dayCount: Int = 7,
    val currentDayIndex: Int = 1,
    val id: Int? = null,
)

data class PlanItem(
    val dayIndex: Int,
    val exercise: Exercise,
    val planId: Int,
    val id: Long? = null,
)

val Plan.titlesMap: Map<Int, String>
    get() = try {
        if (dayTitles.isNullOrBlank()) emptyMap()
        else Json.decodeFromString<Map<Int, String>>(dayTitles)
    } catch (e: Exception) {
        emptyMap()
    }

fun Plan.withDayTitle(dayIndex: Int, title: String?): Plan {
    val currentMap = titlesMap.toMutableMap()
    if (title.isNullOrBlank()) {
        currentMap.remove(dayIndex)
    } else {
        currentMap[dayIndex] = title
    }
    val newDayTitles = if (currentMap.isEmpty()) null else Json.encodeToString(currentMap)
    return copy(dayTitles = newDayTitles)
}

fun today(): LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
