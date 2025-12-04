package com.azim.fitness.data.repository

import com.azim.fitness.db.dao.NotesDao
import com.azim.fitness.db.entity.Note

class NotesRepository(private val notesDao: NotesDao) {

    fun getNotes() = notesDao.getNotes()

    suspend fun addNote(note: Note) = notesDao.addNote(note)
}