package com.binishmatheww.notes.views.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.binishmatheww.notes.core.Theme

@Composable
fun ImageButton(
    modifier: Modifier =Modifier,
    color: Color = Theme.ColorPalette.primaryColorVariant,
    iconColor: Color = Theme.ColorPalette.secondaryColor,
    icon: ImageVector,
    size: Dp = 40.dp,
    iconSize:Dp = 28.dp,
    onClick: () -> Unit = {}
) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(
                    color,
                    shape = RoundedCornerShape(8.dp)
                )
                .size(size)
                .clickable(onClick = onClick)

        )
        Icon(
            imageVector = icon,
            tint= iconColor,
            contentDescription = null,
            modifier = Modifier.padding(4.dp)
                .size(iconSize)
        )
    }

}