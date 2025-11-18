package com.notesapp.controller;

import com.notesapp.dto.NoteCreateDto;
import com.notesapp.dto.NoteDto;
import com.notesapp.dto.NoteResponseDto;
import com.notesapp.enums.NoteTag;
import com.notesapp.mapper.NoteMapper;
import com.notesapp.model.Note;
import com.notesapp.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/notes")
public class NoteController implements NoteApi {

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteMapper noteMapper;

    @Override
    public NoteResponseDto createNote(@Valid @RequestBody NoteCreateDto note) {
        return noteMapper.toDto(noteService.createNote(noteMapper.toModel(note)));
    }

    @Override
    public NoteResponseDto updateNote(@PathVariable String id, @Valid @RequestBody NoteCreateDto note) {
        return noteMapper.toDto(noteService.updateNote(id, noteMapper.toModel(note)));
    }

    @Override
    public void deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
    }

    @Override
    public Page<NoteDto> getNotes(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(required = false) NoteTag tag) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Note> notes;
        if (tag != null) {
            notes = noteService.getNotesByTag(tag, pageable);
        } else {
            notes = noteService.getAllNotes(pageable);
        }

        return noteMapper.toDtoPage(notes);
    }

    @Override
    public Map<String, Integer> getCountOfUniqWords(@PathVariable String id) {
        return noteService.getCountOfNoteWords(id);
    }
}
