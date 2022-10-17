package com.binishmatheww.notes.core.widgets

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.binishmatheww.notes.R
import com.binishmatheww.notes.core.themes.WidgetTheme
import com.binishmatheww.notes.views.LauncherActivity

class NotesWidget : GlanceAppWidget() {

    companion object{

        suspend fun notifyNotesWidget( context: Context, noteTitle : String ) {
            GlanceAppWidgetManager(context).getGlanceIds(NotesWidget::class.java).forEach { glanceId ->
                updateAppWidgetState(
                    context = context,
                    definition = NotesWidgetStateDefinition,
                    glanceId = glanceId) { pref ->
                    pref.toMutablePreferences().apply {
                        this[stringPreferencesKey("noteTitle")] = noteTitle
                    }
                }
                NotesWidget().updateAll(context)
            }
        }

    }

    override val stateDefinition: GlanceStateDefinition<*>
        get() = NotesWidgetStateDefinition

    @Composable
    override fun Content() {

        WidgetTheme.NotesTheme {

            val context = LocalContext.current

            val pref = currentState<Preferences>()

            val noteTitle = remember { pref[stringPreferencesKey("noteTitle")] ?: "" }

            Text(
                text = noteTitle.ifBlank { context.getString(R.string.addNotesAndTheyWillBeShownHere) },
                modifier = GlanceModifier
                    .padding(R.dimen.dp_8)
                    .background(MaterialTheme.colorScheme.background)
                    .clickable(actionStartActivity(LauncherActivity::class.java)),
                style = TextStyle(
                    color = ColorProvider(MaterialTheme.colorScheme.primary),
                    fontSize = 12.sp,
                )
            )

        }

    }

}