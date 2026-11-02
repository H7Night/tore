package com.looker.kenko.data.repository

import com.looker.kenko.data.repository.local.LocalStatisticsRepo
import com.looker.kenko.domain.model.CountType
import com.looker.kenko.domain.model.Exercise
import com.looker.kenko.domain.model.SessionSummary
import com.looker.kenko.domain.model.Tag
import com.looker.kenko.domain.model.today
import com.looker.kenko.testutil.FakeExerciseRepo
import com.looker.kenko.testutil.FakePlanRepo
import com.looker.kenko.testutil.FakeSessionRepo
import com.looker.kenko.testutil.FakeTagRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StatisticsRepositoryTest {

    @Test
    fun `state is null before first computation`() = runTest {
        val repo = build()
        assertNull(repo.state.value)
    }

    @Test
    fun `emits aggregated state with cardio minutes`() = runTest {
        val today = today()
        val summaries = listOf(
            SessionSummary(date = today, planId = 1, exerciseNames = listOf("Bench"), setCount = 1),
        )
        val repo = build(
            summaries = summaries,
            cardioMinutes = mapOf(today to 30),
            exercises = listOf(
                Exercise(name = "Bench", tags = listOf(Tag(id = 9, name = "中胸", parentId = 1)), countType = CountType.REPS),
                Exercise(name = "Treadmill", tags = listOf(Tag(id = 28, name = "跑步", parentId = 7)), countType = CountType.MINUTES, id = 7),
            ),
            tags = listOf(
                Tag(id = 1, name = "胸"),
                Tag(id = 7, name = "有氧"),
                Tag(id = 9, name = "中胸", parentId = 1),
                Tag(id = 28, name = "跑步", parentId = 7),
            ),
        )
        val state = repo.state.first { it != null }!!
        assertEquals(30, state.cardioWeekly)
    }

    private fun TestScope.build(
        summaries: List<SessionSummary> = emptyList(),
        exercises: List<Exercise> = emptyList(),
        tags: List<Tag> = emptyList(),
        cardioMinutes: Map<kotlinx.datetime.LocalDate, Int> = emptyMap(),
    ): LocalStatisticsRepo = LocalStatisticsRepo(
        sessionRepo = FakeSessionRepo(
            summaries = summaries,
            cardioMinutes = cardioMinutes,
        ),
        exerciseRepo = FakeExerciseRepo(exercises = exercises),
        tagRepo = FakeTagRepo(tags = tags),
        planRepo = FakePlanRepo(),
        appScope = backgroundScope,
        defaultDispatcher = StandardTestDispatcher(testScheduler),
    )
}
