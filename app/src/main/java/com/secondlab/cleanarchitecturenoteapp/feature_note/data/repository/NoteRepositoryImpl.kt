package com.secondlab.cleanarchitecturenoteapp.feature_note.data.repository

import com.secondlab.cleanarchitecturenoteapp.feature_note.data.data_source.NoteDao
import com.secondlab.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.secondlab.cleanarchitecturenoteapp.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val dao: NoteDao,
) : NoteRepository {

    override fun getNotes(): Flow<List<Note>> {
        return dao.getNote()
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        return dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        return dao.deleteNote(note)
    }
}