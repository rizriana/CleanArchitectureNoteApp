package com.secondlab.cleanarchitecturenoteapp.feature_note.domain.use_case

import com.secondlab.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.secondlab.cleanarchitecturenoteapp.feature_note.domain.repository.NoteRepository
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private val repository: NoteRepository,
) {

    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}