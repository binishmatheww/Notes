package com.binishmatheww.notes.models.pager

import com.binishmatheww.notes.models.Note

data class PagedNotes(val previousId : Long?, val notes : List<Note>, val nextId : Long?)