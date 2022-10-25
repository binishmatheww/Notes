package com.binishmatheww.notes.core.widgets

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
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
import com.binishmatheww.notes.core.themes.ColorPalette
import com.binishmatheww.notes.core.themes.WidgetTheme
import com.binishmatheww.notes.models.Note
import com.binishmatheww.notes.views.LauncherActivity

class NotesWidget : GlanceAppWidget() {

    companion object{

        const val KEY_NOTES = "notes"

        suspend fun notifyNotesWidget( context: Context, notes : List<Note> ) {

            GlanceAppWidgetManager(context).getGlanceIds(NotesWidget::class.java).forEach { glanceId ->
                updateAppWidgetState(
                    context = context,
                    definition = NotesWidgetStateDefinition,
                    glanceId = glanceId) { pref ->
                    pref.toMutablePreferences().apply {
                        this[stringSetPreferencesKey(KEY_NOTES)] = notes.map { it.toString() }.toSet()
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

            val pref = currentState<Preferences>()

            val notes = remember { pref[stringSetPreferencesKey(KEY_NOTES)]?.map { Note.fromString(it) } ?: emptyList() }

            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {

                LazyColumn(
                    modifier = GlanceModifier
                        .padding(10.dp)
                        .background(ImageProvider(R.drawable.note_widget_background))
                        .height(200.dp),
                    content = {

                        items(notes){ note ->
                            NoteLayout(
                                modifier = GlanceModifier
                                    .clickable(
                                        onClick = actionStartActivity<LauncherActivity>(
                                            parameters = actionParametersOf(
                                                ActionParameters.Key<Long>("id") to note.id
                                            )
                                        )
                                    ),
                                note = note
                            )
                        }

                    }
                )

                Spacer(
                    modifier = GlanceModifier
                        .height(12.dp)
                )

                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                ) {

                    Row(
                        modifier = GlanceModifier
                            .height(50.dp)
                            .defaultWeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            modifier = GlanceModifier,
                            text = LocalContext.current.getString(R.string.spacedAppName),
                            style = TextStyle(
                                color = ColorProvider(Color.White),
                                fontWeight = androidx.glance.text.FontWeight.Normal,
                                fontSize = 32.sp,
                            )
                        )

                        Spacer(
                            modifier = GlanceModifier
                                .width(6.dp)
                        )

                        Image(
                            modifier = GlanceModifier
                                .size(25.dp),
                            provider = ImageProvider(R.drawable.note),
                            contentDescription = LocalContext.current.getString(R.string.addNote),
                        )

                    }

                    Box(
                        modifier = GlanceModifier
                            .size(50.dp)
                            .background(ImageProvider(R.drawable.note_widget_background))
                            .clickable(actionStartActivity(LauncherActivity::class.java)),
                        contentAlignment = Alignment.Center
                    ) {

                        Image(
                            modifier = GlanceModifier
                                .size(28.dp),
                            provider = ImageProvider(R.drawable.note_add_icon),
                            contentDescription = LocalContext.current.getString(R.string.addNote),
                        )

                    }

                }

            }

        }

    }

    @Composable
    private fun NoteLayout(
        modifier: GlanceModifier = GlanceModifier,
        note : Note,
    ){

        Column(
            modifier = modifier
        ) {

            Row(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .background(ImageProvider(R.drawable.note_item_background))
                    .height(50.dp),
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
                verticalAlignment = Alignment.Vertical.CenterVertically
            ) {

                Text(
                    text = note.title,
                    modifier = GlanceModifier
                        .fillMaxWidth(),
                    style = TextStyle(
                        color = ColorProvider(Color.White.copy(alpha = 0.9f)),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 3
                )

            }

            Spacer(
                modifier = GlanceModifier
                    .height(12.dp)
            )

        }

    }

}