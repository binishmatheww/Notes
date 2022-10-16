package com.binishmatheww.notes.core.widgets

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.Action
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.background
import androidx.glance.background
import androidx.glance.layout.padding
import androidx.glance.text.*
import androidx.glance.unit.ColorProvider
import com.binishmatheww.notes.R
import com.binishmatheww.notes.core.themes.WidgetTheme
import com.binishmatheww.notes.views.LauncherActivity

class NotesWidget : GlanceAppWidget() {

    @Composable
    override fun Content() {

        WidgetTheme.NotesTheme() {

            val context = LocalContext.current

            Button(
                text = context.resources.getString(R.string.addNote),
                onClick = actionLaunchActivity(),
                modifier = GlanceModifier
                    .padding(R.dimen.dp_8)
                    .background(MaterialTheme.colorScheme.background),
                style = TextStyle(
                    color = ColorProvider(MaterialTheme.colorScheme.primary),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic,
                    textDecoration = TextDecoration.None,
                    textAlign = TextAlign.Center
                )
            )

        }

    }

    private fun actionLaunchActivity(): Action = actionStartActivity(LauncherActivity::class.java)

}

class NotesWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = NotesWidget()
}