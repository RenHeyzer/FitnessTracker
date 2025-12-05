package com.azim.fitness.data.repository

import com.azim.fitness.db.dao.NotesDao
import com.azim.fitness.db.entity.Note

class NotesRepository(private val notesDao: NotesDao) {

    fun getTodayNotes(date: String) = notesDao.getTodayNotes(date)

    suspend fun addNote(note: Note) = notesDao.addNote(note)
}