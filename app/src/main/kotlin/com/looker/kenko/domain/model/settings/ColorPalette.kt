/*
 * Copyright (C) 2026 H7Night <h7night@gmail.com>
 * This file is part of Kenko (GPL-3.0-or-later).
 */

package com.looker.kenko.domain.model.settings

/**
 * Selectable color style for the app theme.
 *
 * [seedArgb] is the Material Color Utilities seed for the palette; the UI layer
 * generates the full light/dark scheme from it (MaterialKolor). `null` marks
 * [Material], which uses Android dynamic color (Monet) instead.
 */
enum class ColorPalette(val seedArgb: Long? = null) {
    /** Android dynamic color (Monet on Android 12+); MaterialKolor baseline below. */
    Material,

    Blue(0xFF4285F4),
    Green(0xFF0F9D58),
    Amber(0xFFF4B400),
    Pink(0xFFE91E63),
    Monochrome(0xFF5F6368),
}
