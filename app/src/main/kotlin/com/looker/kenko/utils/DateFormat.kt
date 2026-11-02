/*
 * Copyright (C) 2026 H7Night <h7night@gmail.com>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.looker.kenko.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char

@JvmInline
value class DateFormat(private val format: DateTimeFormat<LocalDate>) {

    fun format(localDate: LocalDate): String = format.format(localDate)

    companion object {
        val YearMonthDay = DateFormat(
            LocalDate.Format {
                year()
                char('-')
                monthNumber()
                char('-')
                day()
            },
        )
        val MonthDay = DateFormat(
            LocalDate.Format {
                monthNumber()
                char('-')
                day()
            },
        )
    }
}

fun formatDate(
    date: LocalDate,
    dateTimeFormat: DateFormat = DateFormat.YearMonthDay,
): String = dateTimeFormat.format(date)
