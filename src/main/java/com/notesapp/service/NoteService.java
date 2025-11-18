package com.notesapp.service;

import com.notesapp.model.Note;
import com.notesapp.enums.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoteService {

    Note createNote(Note note);

    Note updateNote(String id, Note note);

    void deleteNote(String id);

    Note getNoteById(String id);

    List<Note> getAllNotes();

    Page<Note> getNoteByTag(Tag tag,  Pageable pageable);

}
