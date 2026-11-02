/*
 * Copyright (C) 2026 H7Night <h7night@gmail.com>
 * This file is part of Kenko (GPL-3.0-or-later).
 */

package com.looker.kenko.data.repository

import com.looker.kenko.domain.statistics.StatisticsUiState
import kotlinx.coroutines.flow.StateFlow

interface StatisticsRepo {

    /** 应用作用域缓存的统计状态；`null` 表示首次结果尚未就绪。 */
    val state: StateFlow<StatisticsUiState?>
}
