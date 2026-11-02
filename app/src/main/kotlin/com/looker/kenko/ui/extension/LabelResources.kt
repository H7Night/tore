/*
 * Copyright (C) 2026 H7Night <h7night@gmail.com>
 * This file is part of Kenko (GPL-3.0-or-later).
 */

package com.looker.kenko.ui.extension

import androidx.annotation.StringRes
import com.looker.kenko.R
import com.looker.kenko.domain.model.ExerciseEquipment
import com.looker.kenko.domain.model.settings.BackupInterval
import com.looker.kenko.domain.model.settings.ColorPalette
import com.looker.kenko.domain.model.settings.Language
import com.looker.kenko.domain.model.settings.Theme

@get:StringRes
val ExerciseEquipment.labelRes: Int
    get() = when (this) {
        ExerciseEquipment.BARBELL -> R.string.label_equipment_barbell
        ExerciseEquipment.DUMBBELL -> R.string.label_equipment_dumbbell
        ExerciseEquipment.MACHINE -> R.string.label_equipment_machine
        ExerciseEquipment.CABLE -> R.string.label_equipment_cable
        ExerciseEquipment.BODYWEIGHT -> R.string.label_equipment_bodyweight
        ExerciseEquipment.OTHER -> R.string.label_equipment_other
    }

@get:StringRes
val Theme.nameRes: Int
    get() = when (this) {
        Theme.System -> R.string.label_theme_system
        Theme.Light -> R.string.label_theme_light
        Theme.Dark -> R.string.label_theme_dark
    }

@get:StringRes
val Language.labelRes: Int
    get() = when (this) {
        Language.System -> R.string.label_theme_system
        Language.English -> R.string.label_language_en
        Language.Chinese -> R.string.label_language_zh
    }

@get:StringRes
val ColorPalette.nameRes: Int
    get() = when (this) {
        ColorPalette.Material -> R.string.label_color_style_material
        ColorPalette.Blue -> R.string.label_color_style_blue
        ColorPalette.Green -> R.string.label_color_style_green
        ColorPalette.Amber -> R.string.label_color_style_amber
        ColorPalette.Pink -> R.string.label_color_style_pink
        ColorPalette.Monochrome -> R.string.label_color_style_monochrome
    }

@get:StringRes
val BackupInterval.nameRes: Int
    get() = when (this) {
        BackupInterval.Off -> R.string.label_backup_off
        BackupInterval.Daily -> R.string.label_backup_daily
        BackupInterval.Weekly -> R.string.label_backup_weekly
        BackupInterval.Monthly -> R.string.label_backup_monthly
    }
