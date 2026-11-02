/*
 * Copyright (C) 2026 H7Night <h7night@gmail.com>
 * This file is part of Kenko (GPL-3.0-or-later).
 */

package com.looker.kenko.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.looker.kenko.domain.model.CountType
import com.looker.kenko.domain.model.Exercise
import com.looker.kenko.domain.model.Labels.Difficulty
import com.looker.kenko.domain.model.Labels.Equipment
import com.looker.kenko.domain.model.Labels.Focus
import com.looker.kenko.domain.model.Labels.Time
import com.looker.kenko.domain.model.Plan
import com.looker.kenko.domain.model.PlanStat
import com.looker.kenko.domain.model.Tag

class ExercisesPreviewParameter : PreviewParameterProvider<List<Exercise>> {
    override val values = sequenceOf(
        listOf(
            Exercise("Curls", tags = listOf(Tag(id = 1, name = "二头肌", parentId = 5, parentName = "手臂"))),
            Exercise("Barbell Curls", tags = listOf(Tag(id = 1, name = "二头肌", parentId = 5, parentName = "手臂"))),
            Exercise("Preacher Curls", tags = listOf(Tag(id = 1, name = "二头肌", parentId = 5, parentName = "手臂"))),
            Exercise("B-t-B Curls", tags = listOf(Tag(id = 1, name = "二头肌", parentId = 5, parentName = "手臂"))),
        ),
        listOf(
            Exercise("Push-down", tags = listOf(Tag(id = 2, name = "三头肌", parentId = 5, parentName = "手臂"))),
            Exercise("Skull-Crushers", tags = listOf(Tag(id = 2, name = "三头肌", parentId = 5, parentName = "手臂"))),
            Exercise("Push-overs", tags = listOf(Tag(id = 2, name = "三头肌", parentId = 5, parentName = "手臂"))),
        ),
        listOf(
            Exercise("Lateral Raises", tags = listOf(Tag(id = 3, name = "中束", parentId = 4, parentName = "肩"))),
            Exercise("Shoulder Press", tags = listOf(Tag(id = 3, name = "中束", parentId = 4, parentName = "肩"))),
            Exercise("Face Pulls", tags = listOf(Tag(id = 3, name = "中束", parentId = 4, parentName = "肩"))),
        ),
        listOf(
            Exercise("Squats", tags = listOf(Tag(id = 4, name = "股四头肌", parentId = 3, parentName = "腿"))),
            Exercise("Leg Press", tags = listOf(Tag(id = 4, name = "股四头肌", parentId = 3, parentName = "腿"))),
            Exercise("Hack Squats", tags = listOf(Tag(id = 4, name = "股四头肌", parentId = 3, parentName = "腿"))),
            Exercise("Leg Extensions", tags = listOf(Tag(id = 4, name = "股四头肌", parentId = 3, parentName = "腿"))),
        ),
        listOf(
            Exercise("SDL", tags = listOf(Tag(id = 5, name = "腘绳肌", parentId = 3, parentName = "腿"))),
            Exercise("Lying Leg Curls", tags = listOf(Tag(id = 5, name = "腘绳肌", parentId = 3, parentName = "腿"))),
        ),
        listOf(Exercise("Calve Raises", tags = listOf(Tag(id = 6, name = "小腿", parentId = 3, parentName = "腿")))),
        listOf(
            Exercise("Hip Thrusts", tags = listOf(Tag(id = 7, name = "臀肌", parentId = 3, parentName = "腿"))),
            Exercise("Lunges", tags = listOf(Tag(id = 7, name = "臀肌", parentId = 3, parentName = "腿"))),
        ),
        listOf(
            Exercise("Sit-ups", tags = listOf(Tag(id = 8, name = "上腹", parentId = 6, parentName = "腹"))),
            Exercise("Leg Raises", tags = listOf(Tag(id = 8, name = "上腹", parentId = 6, parentName = "腹"))),
        ),
        listOf(
            Exercise("Bench Press", tags = listOf(Tag(id = 9, name = "中胸", parentId = 1, parentName = "胸"))),
            Exercise("Incline Bench", tags = listOf(Tag(id = 10, name = "上胸", parentId = 1, parentName = "胸"))),
            Exercise("Pec Dec", tags = listOf(Tag(id = 9, name = "中胸", parentId = 1, parentName = "胸"))),
            Exercise("Chest Fly", tags = listOf(Tag(id = 9, name = "中胸", parentId = 1, parentName = "胸"))),
        ),
        listOf(Exercise("Shrugs", tags = listOf(Tag(id = 11, name = "斜方肌", parentId = 2, parentName = "背")))),
        listOf(
            Exercise("Lat Pull-down", tags = listOf(Tag(id = 12, name = "背阔肌", parentId = 2, parentName = "背"))),
            Exercise("Pull-ups", tags = listOf(Tag(id = 12, name = "背阔肌", parentId = 2, parentName = "背"))),
            Exercise("Lat Prayers", tags = listOf(Tag(id = 12, name = "背阔肌", parentId = 2, parentName = "背"))),
        ),
        listOf(
            Exercise("Bent-over Rows", tags = listOf(Tag(id = 13, name = "竖脊肌", parentId = 2, parentName = "背"))),
            Exercise("Chest-Supported Rows", tags = listOf(Tag(id = 13, name = "竖脊肌", parentId = 2, parentName = "背"))),
            Exercise("Rows", tags = listOf(Tag(id = 13, name = "竖脊肌", parentId = 2, parentName = "背"))),
        ),
        listOf(
            Exercise("Treadmill", tags = listOf(Tag(id = 14, name = "跑步", parentId = 7, parentName = "有氧")), countType = CountType.MINUTES),
            Exercise("Cycling", tags = listOf(Tag(id = 15, name = "骑行", parentId = 7, parentName = "有氧")), countType = CountType.MINUTES),
        ),
    )
}

class PlanPreviewParameters : PreviewParameterProvider<List<Plan>> {
    override val values: Sequence<List<Plan>> = sequenceOf(
        listOf(
            Plan(
                name = "Push Pull Leg",
                description = null,
                difficulty = Difficulty.ADAPTABLE,
                focus = null,
                equipment = Equipment.FULL_GYM,
                time = Time.NORMAL,
                isActive = true,
                stat = PlanStat(21, 5),
            ),
            Plan(
                name = "Upper Lower",
                description = "Alternative upper lower split",
                difficulty = Difficulty.BEGINNER,
                focus = Focus.POWER_BUILDING,
                equipment = Equipment.FULL_GYM,
                time = Time.QUICK,
                isActive = false,
                stat = PlanStat(21, 4),
            ),
            Plan(
                name = "Upper Lower 2",
                description = "Lower Upper split at home",
                difficulty = Difficulty.ADAPTABLE,
                focus = Focus.POWER_BUILDING,
                equipment = Equipment.DUMBBELLS,
                time = Time.QUICK,
                isActive = false,
                stat = PlanStat(21, 5),
            ),
        ),
    )
}
