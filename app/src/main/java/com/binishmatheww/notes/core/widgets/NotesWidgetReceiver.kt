package com.binishmatheww.notes.core.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.binishmatheww.notes.models.repositories.NoteRepository
import kotlinx.coroutines.MainScope
import javax.inject.Inject

class NotesWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = NotesWidget()

    private val coroutineScope = MainScope()


}