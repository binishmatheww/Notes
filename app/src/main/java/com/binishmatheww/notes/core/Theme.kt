package com.binishmatheww.notes.core

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.binishmatheww.notes.R
import com.binishmatheww.notes.core.utilities.networkManagers.ConnectivityObserver

object Theme {

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun NotesTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit
    ) {
        val colors = if (darkTheme) {
            DarkColors
        } else {
            LightColors
        }

        val systemUiController = rememberSystemUiController()

        systemUiController.setSystemBarsColor(
            color = colors.background,
            darkIcons = !darkTheme
        )


        MaterialTheme(
            colorScheme = colors,
            typography = Typography(
                displayLarge = Typography.displayLarge,
                displayMedium = Typography.displayMedium,
                displaySmall = Typography.displaySmall,
                headlineLarge = Typography.headlineLarge,
                headlineMedium = Typography.headlineMedium,
                headlineSmall = Typography.headlineSmall,
                titleLarge = Typography.titleLarge,
                titleMedium = Typography.titleMedium,
                titleSmall = Typography.titleSmall,
                bodyLarge = Typography.bodyLarge,
                bodyMedium = Typography.bodyMedium,
                bodySmall = Typography.bodySmall,
                labelLarge = Typography.labelLarge,
                labelMedium = Typography.labelMedium,
                labelSmall = Typography.labelSmall,
            ),
            shapes = shapes,
        ) {
            CompositionLocalProvider(
                LocalOverscrollConfiguration provides null,
                content = content
            )
        }
    }

    private val LightColors = lightColorScheme(
        primary = ColorPalette.md_theme_light_primary,
        onPrimary = ColorPalette.md_theme_light_onPrimary,
        primaryContainer = ColorPalette.md_theme_light_primaryContainer,
        onPrimaryContainer = ColorPalette.md_theme_light_onPrimaryContainer,
        secondary = ColorPalette.md_theme_light_secondary,
        onSecondary = ColorPalette.md_theme_light_onSecondary,
        secondaryContainer = ColorPalette.md_theme_light_secondaryContainer,
        onSecondaryContainer = ColorPalette.md_theme_light_onSecondaryContainer,
        tertiary = ColorPalette.md_theme_light_tertiary,
        onTertiary = ColorPalette.md_theme_light_onTertiary,
        tertiaryContainer = ColorPalette.md_theme_light_tertiaryContainer,
        onTertiaryContainer = ColorPalette.md_theme_light_onTertiaryContainer,
        error = ColorPalette.md_theme_light_error,
        errorContainer = ColorPalette.md_theme_light_errorContainer,
        onError = ColorPalette.md_theme_light_onError,
        onErrorContainer = ColorPalette.md_theme_light_onErrorContainer,
        background = ColorPalette.md_theme_light_background,
        onBackground = ColorPalette.md_theme_light_onBackground,
        surface = ColorPalette.md_theme_light_surface,
        onSurface = ColorPalette.md_theme_light_onSurface,
        surfaceVariant = ColorPalette.md_theme_light_surfaceVariant,
        onSurfaceVariant = ColorPalette.md_theme_light_onSurfaceVariant,
        outline = ColorPalette.md_theme_light_outline,
        inverseOnSurface = ColorPalette.md_theme_light_inverseOnSurface,
        inverseSurface = ColorPalette.md_theme_light_inverseSurface,
        inversePrimary = ColorPalette.md_theme_light_inversePrimary,
        surfaceTint = ColorPalette.md_theme_light_surfaceTint,
    )


    private val DarkColors = darkColorScheme(
        primary = ColorPalette.md_theme_dark_primary,
        onPrimary = ColorPalette.md_theme_dark_onPrimary,
        primaryContainer = ColorPalette.md_theme_dark_primaryContainer,
        onPrimaryContainer = ColorPalette.md_theme_dark_onPrimaryContainer,
        secondary = ColorPalette.md_theme_dark_secondary,
        onSecondary = ColorPalette.md_theme_dark_onSecondary,
        secondaryContainer = ColorPalette.md_theme_dark_secondaryContainer,
        onSecondaryContainer = ColorPalette.md_theme_dark_onSecondaryContainer,
        tertiary = ColorPalette.md_theme_dark_tertiary,
        onTertiary = ColorPalette.md_theme_dark_onTertiary,
        tertiaryContainer = ColorPalette.md_theme_dark_tertiaryContainer,
        onTertiaryContainer = ColorPalette.md_theme_dark_onTertiaryContainer,
        error = ColorPalette.md_theme_dark_error,
        errorContainer = ColorPalette.md_theme_dark_errorContainer,
        onError = ColorPalette.md_theme_dark_onError,
        onErrorContainer = ColorPalette.md_theme_dark_onErrorContainer,
        background = ColorPalette.md_theme_dark_background,
        onBackground = ColorPalette.md_theme_dark_onBackground,
        surface = ColorPalette.md_theme_dark_surface,
        onSurface = ColorPalette.md_theme_dark_onSurface,
        surfaceVariant = ColorPalette.md_theme_dark_surfaceVariant,
        onSurfaceVariant = ColorPalette.md_theme_dark_onSurfaceVariant,
        outline = ColorPalette.md_theme_dark_outline,
        inverseOnSurface = ColorPalette.md_theme_dark_inverseOnSurface,
        inverseSurface = ColorPalette.md_theme_dark_inverseSurface,
        inversePrimary = ColorPalette.md_theme_dark_inversePrimary,
        surfaceTint = ColorPalette.md_theme_dark_surfaceTint,
    )

    private val shapes = Shapes(
        small = RoundedCornerShape(2.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(8.dp)
    )


    object ColorPalette {

        val metal = Color(0xFF828DA9)

        val cyan = Color(0xFF0C9D9A)

        val lightBlue = Color(0xFF0098C6)

        val sky = Color(0xFF4E89FF)

        val electricBlue = Color(0xFF7E7EFF)

        val magenta = Color(0xFFE734FF)

        val rose = Color(0xFFFF3F8A)

        val yellow = Color(0xFFFBC01E)

        val lightGreen = Color(0xFF7FE030)

        val green = Color(0xFF7FE030)


        val md_theme_light_primary = Color(0xFF265EA7)
        val md_theme_light_onPrimary = Color(0xFFFFFFFF)
        val md_theme_light_primaryContainer = Color(0xFFD6E3FF)
        val md_theme_light_onPrimaryContainer = Color(0xFF001B3D)
        val md_theme_light_secondary = Color(0xFF555F71)
        val md_theme_light_onSecondary = Color(0xFFFFFFFF)
        val md_theme_light_secondaryContainer = Color(0xFFD9E3F8)
        val md_theme_light_onSecondaryContainer = Color(0xFF121C2B)
        val md_theme_light_tertiary = Color(0xFF6F5675)
        val md_theme_light_onTertiary = Color(0xFFFFFFFF)
        val md_theme_light_tertiaryContainer = Color(0xFFF9D8FE)
        val md_theme_light_onTertiaryContainer = Color(0xFF28132F)
        val md_theme_light_error = Color(0xFFBA1A1A)
        val md_theme_light_errorContainer = Color(0xFFFFDAD6)
        val md_theme_light_onError = Color(0xFFFFFFFF)
        val md_theme_light_onErrorContainer = Color(0xFF410002)
        val md_theme_light_background = Color(0xFFFDFBFF)
        val md_theme_light_onBackground = Color(0xFF1A1C1E)
        val md_theme_light_surface = Color(0xFFFDFBFF)
        val md_theme_light_onSurface = Color(0xFF1A1C1E)
        val md_theme_light_surfaceVariant = Color(0xFFE0E2EC)
        val md_theme_light_onSurfaceVariant = Color(0xFF43474E)
        val md_theme_light_outline = Color(0xFF74777F)
        val md_theme_light_inverseOnSurface = Color(0xFFF1F0F4)
        val md_theme_light_inverseSurface = Color(0xFF2F3033)
        val md_theme_light_inversePrimary = Color(0xFFA8C8FF)
        val md_theme_light_shadow = Color(0xFF000000)
        val md_theme_light_surfaceTint = Color(0xFF265EA7)

        val md_theme_dark_primary = Color(0xFFA8C8FF)
        val md_theme_dark_onPrimary = Color(0xFF003062)
        val md_theme_dark_primaryContainer = Color(0xFF00468A)
        val md_theme_dark_onPrimaryContainer = Color(0xFFD6E3FF)
        val md_theme_dark_secondary = Color(0xFFBDC7DC)
        val md_theme_dark_onSecondary = Color(0xFF273141)
        val md_theme_dark_secondaryContainer = Color(0xFF3E4758)
        val md_theme_dark_onSecondaryContainer = Color(0xFFD9E3F8)
        val md_theme_dark_tertiary = Color(0xFFDCBCE1)
        val md_theme_dark_onTertiary = Color(0xFF3E2845)
        val md_theme_dark_tertiaryContainer = Color(0xFF563E5C)
        val md_theme_dark_onTertiaryContainer = Color(0xFFF9D8FE)
        val md_theme_dark_error = Color(0xFFFFB4AB)
        val md_theme_dark_errorContainer = Color(0xFF93000A)
        val md_theme_dark_onError = Color(0xFF690005)
        val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
        val md_theme_dark_background = Color(0xFF1A1C1E)
        val md_theme_dark_onBackground = Color(0xFFE3E2E6)
        val md_theme_dark_surface = Color(0xFF1A1C1E)
        val md_theme_dark_onSurface = Color(0xFFE3E2E6)
        val md_theme_dark_surfaceVariant = Color(0xFF43474E)
        val md_theme_dark_onSurfaceVariant = Color(0xFFC4C6CF)
        val md_theme_dark_outline = Color(0xFF8E9099)
        val md_theme_dark_inverseOnSurface = Color(0xFF1A1C1E)
        val md_theme_dark_inverseSurface = Color(0xFFE3E2E6)
        val md_theme_dark_inversePrimary = Color(0xFF265EA7)
        val md_theme_dark_shadow = Color(0xFF000000)
        val md_theme_dark_surfaceTint = Color(0xFFA8C8FF)


        val seed = Color(0xFF4C6793)

        fun getColorByNumber(number: Int): Color {

            return when (number) {
                0 -> metal
                1 -> lightGreen
                2 -> lightBlue
                3 -> rose
                4 -> electricBlue
                5 -> magenta
                6 -> sky
                7 -> yellow
                8 -> cyan
                else -> green
            }

        }

    }

    object Typography {

        private val raleway = FontFamily(
            Font(R.font.raleway_light, FontWeight.Light),
            Font(R.font.raleway_medium, FontWeight.Medium),
            Font(R.font.raleway_bold, FontWeight.Bold)
        )

        private val primaryFont = raleway

        val displayLarge = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Normal,
            fontSize = 57.sp
        )

        val displayMedium = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Normal,
            fontSize = 45.sp
        )

        val displaySmall = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp
        )

        val headlineLarge = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Normal,
            fontSize = 32.sp
        )

        val headlineMedium = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Normal,
            fontSize = 28.sp
        )

        val headlineSmall = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp
        )

        val titleLarge = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp
        )

        val titleMedium = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )

        val titleSmall = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )

        val bodyLarge = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )

        val bodyMedium = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )

        val bodySmall = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        )

        val labelLarge = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )

        val labelMedium = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        )

        val labelSmall = TextStyle(
            fontFamily = primaryFont,
            fontWeight = FontWeight.Normal,
            fontSize = 11.sp
        )

        val bold14 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

    }

}
