package com.binishmatheww.notes.core.widgets

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.layout.*
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
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

        private val notes = mutableListOf(
            "Sample",
            "data",
            "added",
            "to",
            "demonstrate",
            "how",
            "this",
            "widget",
            "will",
            "display",
            "notes",
            "added",
            "by",
            "you",
        )

    }

    override val stateDefinition: GlanceStateDefinition<*>
        get() = NotesWidgetStateDefinition

    @Composable
    override fun Content() {

        WidgetTheme.NotesTheme {

            val context = LocalContext.current

            //val pref = currentState<Preferences>()

            //val noteTitle = remember { pref[stringPreferencesKey("noteTitle")] ?: "" }

            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
            ) {

                LazyColumn(
                    modifier = GlanceModifier
                        .padding(R.dimen.dp_10),
                    content = {

                        items(notes){ note ->
                            NoteLayout(
                                note = note
                            )
                        }

                    }
                )

                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = context.getString(R.string.addNote),
                        modifier = GlanceModifier
                            .background(MaterialTheme.colorScheme.background)
                            .padding(R.dimen.dp_8)
                            .clickable(actionStartActivity(LauncherActivity::class.java)),
                        style = TextStyle(
                            color = ColorProvider(MaterialTheme.colorScheme.primary),
                            fontSize = 12.sp,
                        )
                    )
                }

            }

        }

    }

    @Composable
    private fun NoteLayout(
        note : String,
    ){

        Column(
            modifier = GlanceModifier
                .fillMaxWidth()
                .background(ImageProvider(R.drawable.dp_4_round_cornered_white_background))
                .padding(
                    bottom = 20.dp
                )
                .clickable(actionStartActivity(LauncherActivity::class.java))
        ) {

            Text(
                text = note,
                modifier = GlanceModifier.fillMaxWidth(),
                style = TextStyle(
                    color = ColorProvider(MaterialTheme.colorScheme.primary),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            )

        }

    }

}