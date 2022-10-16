package com.binishmatheww.notes.core.themes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.glance.appwidget.unit.ColorProvider
import androidx.glance.unit.ColorProvider

/**
 * Temporary implementation of theme object for Glance-appwidgets.
 *
 * Important: It will change!
 */
object WidgetTheme {

    private val colors: ColorProviders
        @Composable
        @ReadOnlyComposable
        get() = LocalColorProviders.current

    private val LocalColorProviders = staticCompositionLocalOf { dynamicThemeColorProviders() }

    @Composable
    fun NotesTheme(colors: ColorProviders = WidgetTheme.colors, content: @Composable () -> Unit) {
        CompositionLocalProvider(LocalColorProviders provides colors) {
            content()
        }
    }

    data class ColorProviders(
        val primary: ColorProvider,
        val onPrimary: ColorProvider,
        val primaryContainer: ColorProvider,
        val onPrimaryContainer: ColorProvider,
        val secondary: ColorProvider,
        val onSecondary: ColorProvider,
        val secondaryContainer: ColorProvider,
        val onSecondaryContainer: ColorProvider,
        val tertiary: ColorProvider,
        val onTertiary: ColorProvider,
        val tertiaryContainer: ColorProvider,
        val onTertiaryContainer: ColorProvider,
        val error: ColorProvider,
        val errorContainer: ColorProvider,
        val onError: ColorProvider,
        val onErrorContainer: ColorProvider,
        val background: ColorProvider,
        val onBackground: ColorProvider,
        val surface: ColorProvider,
        val onSurface: ColorProvider,
        val surfaceVariant: ColorProvider,
        val onSurfaceVariant: ColorProvider,
        val outline: ColorProvider,
        val inverseOnSurface: ColorProvider,
        val inverseSurface: ColorProvider,
        val inversePrimary: ColorProvider,
    )


    fun dynamicThemeColorProviders() : ColorProviders {

        val colors = ColorPalette.lightColorScheme

        return ColorProviders(
            primary = ColorProvider(colors.primary),
            onPrimary = ColorProvider(colors.onPrimary),
            primaryContainer = ColorProvider(colors.primaryContainer),
            onPrimaryContainer = ColorProvider(colors.onPrimaryContainer),
            secondary = ColorProvider(colors.secondary),
            onSecondary = ColorProvider(colors.onSecondary),
            secondaryContainer = ColorProvider(colors.secondaryContainer),
            onSecondaryContainer = ColorProvider(colors.onSecondaryContainer),
            tertiary = ColorProvider(colors.tertiary),
            onTertiary = ColorProvider(colors.onTertiary),
            tertiaryContainer = ColorProvider(colors.tertiaryContainer),
            onTertiaryContainer = ColorProvider(colors.onTertiaryContainer),
            error = ColorProvider(colors.error),
            errorContainer = ColorProvider(colors.errorContainer),
            onError = ColorProvider(colors.onError),
            onErrorContainer = ColorProvider(colors.onErrorContainer),
            background = ColorProvider(colors.background),
            onBackground = ColorProvider(colors.onBackground),
            surface = ColorProvider(colors.surface),
            onSurface = ColorProvider(colors.onSurface),
            surfaceVariant = ColorProvider(colors.surfaceVariant),
            onSurfaceVariant = ColorProvider(colors.onSurfaceVariant),
            outline = ColorProvider(colors.outline),
            inverseOnSurface = ColorProvider(colors.inverseOnSurface),
            inverseSurface = ColorProvider(colors.inverseSurface),
            inversePrimary = ColorProvider(colors.inversePrimary),
        )
    }

}
