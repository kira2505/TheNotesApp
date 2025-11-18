package com.notesapp.service;

import com.notesapp.enums.NoteTag;
import com.notesapp.exception.InvalidTagException;
import com.notesapp.exception.NoteNotFoundException;
import com.notesapp.model.Note;
import com.notesapp.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TextService textService;

    @Autowired
    private AppUserService appUserService;

    @Override
    public Note createNote(Note note) {
        isTagValid(note);
        return noteRepository.save(note);
    }

    @Override
    public Note updateNote(String id, Note note) {
        Note existing = getNoteById(id);
        existing.setTitle(note.getTitle());
        existing.setText(note.getText());

        isTagValid(note);
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
    public Page<Note> getAllNotes(Pageable pageable) {
        return noteRepository.findByUserId(appUserService.getAppUser().getId(), pageable);
    }

    @Override
    public Page<Note> getNotesByTag(NoteTag tag, Pageable pageable) {
        return noteRepository.findByUserIdAndTags(appUserService.getAppUser().getId(),
                tag, pageable);
    }

    @Override
    public Map<String, Integer> getCountOfNoteWords(String id) {
        return textService.getCountOfUniqWords(getNoteById(id).getText());
    }

    public void isTagValid(Note note) {
        for (NoteTag tag : note.getTags()) {
            if (!EnumSet.of(NoteTag.BUSINESS, NoteTag.PERSONAL, NoteTag.IMPORTANT).contains(tag)) {
                throw new InvalidTagException("Invalid tag: " + tag);
            }
        }
    }
}
