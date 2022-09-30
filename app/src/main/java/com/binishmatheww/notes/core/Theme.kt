package com.binishmatheww.notes.core

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.binishmatheww.notes.R

object Theme{

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun NotesTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit
    ) {
        val colors = if (darkTheme) {
            DarkColorPalette
        } else {
            LightColorPalette
        }

        val systemUiController = rememberSystemUiController()

        if(darkTheme){
            systemUiController.setSystemBarsColor(
                color = colors.primary
            )
        }else{
            systemUiController.setSystemBarsColor(
                color = colors.primary
            )
        }


        MaterialTheme(
            colors = colors,
            typography = Typography(
                h1 = Typography.h1,
                h2 = Typography.h2,
                h3 = Typography.h3,
                h4 = Typography.h4,
                h5 = Typography.h5,
                h6 = Typography.h6,
                subtitle1 = Typography.subtitle1,
                subtitle2 = Typography.subtitle2,
                body1 = Typography.body1,
                body2 = Typography.body2,
                button = Typography.button,
                caption = Typography.caption,
                overline = Typography.overline
            ),
            shapes = shapes,
        ){
            CompositionLocalProvider(
                LocalOverScrollConfiguration provides null,
                content = content
            )
        }
    }

    private val DarkColorPalette = darkColors(
        primary = ColorPalette.primaryColor,
        primaryVariant = ColorPalette.primaryColor,
        secondary = ColorPalette.secondaryColor,
        onSurface = ColorPalette.secondaryColor,
        background = ColorPalette.primaryColor,
    )

    private val LightColorPalette = lightColors(
        primary = ColorPalette.primaryColor,
        primaryVariant = ColorPalette.primaryColor,
        secondary = ColorPalette.secondaryColor,
        onSurface = ColorPalette.secondaryColor,
        background = ColorPalette.primaryColor

        /* Other default colors to override
        background = Color.White,
        surface = Color.White,
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black,
        */
    )

    private val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )


    object ColorPalette{

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


        val primaryColor = Color(0xFFE6F0FA)
        val primaryColorVariant = Color(0xFFE2E2FF)
        val secondaryColor = Color(0xFF000000)


        fun getColorByNumber( number : Int ) : Color {

            return when(number){
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

        val raleway = FontFamily(
            Font(R.font.raleway_light, FontWeight.Light),
            Font(R.font.raleway_medium, FontWeight.Medium),
            Font(R.font.raleway_bold, FontWeight.Bold)
        )

        val h1 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.SemiBold,
            fontSize = 56.sp
        )

        val h2 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.SemiBold,
            fontSize = 48.sp
        )

        val h3 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp
        )
        val h4 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.SemiBold,
            fontSize = 30.sp
        )

        val h5 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp
        )

        val h6 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )

        val subtitle1 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 26.sp
        )
        val subtitle2 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
        val body1 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp
        )
        val body2 = TextStyle(
            fontFamily = raleway,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
        val button = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 4.sp
        )

        val caption = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        )

        val overline = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        )

        val bold34 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp
        )

        val bold24 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        val bold20 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        val bold14 = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

    }

}
