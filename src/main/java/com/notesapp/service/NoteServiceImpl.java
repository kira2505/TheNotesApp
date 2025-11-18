package com.notesapp.service;

import com.notesapp.enums.Tag;
import com.notesapp.exception.NoteNotFoundException;
import com.notesapp.model.Note;
import com.notesapp.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Override
    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    @Override
    public Note updateNote(String id, Note note) {
        Note existing = getNoteById(id);
        existing.setTitle(note.getTitle());
        existing.setText(note.getText());
        existing.setTags(note.getTags());
        return noteRepository.save(existing);
    }

    @Override
    public void deleteNote(String id) {
        Note note = getNoteById(id);
        noteRepository.delete(note);
    }

    @Override
    public Note getNoteById(String id) {
        return noteRepository.findById(id).orElseThrow(()
                -> new NoteNotFoundException("Note with id: " + id + " not found"));
    }

    @Override
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @Override
    public Page<Note> getNoteByTag(Tag tag, Pageable pageable) {
        return noteRepository.findByTags(tag, pageable);
    }
}
