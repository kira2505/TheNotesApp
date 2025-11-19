package com.notesapp.service;

import com.notesapp.enums.NoteTag;
import com.notesapp.model.Note;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface NoteService {

    Note createNote(Note note);

    Note updateNote(String id, Note note);

    void deleteNote(String id);

    Note getNoteById(String id);

    List<Note> getAllNotes(Pageable pageable);

    List<Note> getNotesByTag(NoteTag tag, Pageable pageable);

    Map<String, Integer> getCountOfNoteWords(String id);
}
