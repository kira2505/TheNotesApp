package com.notesapp.controller;

import com.notesapp.dto.NoteCreateDto;
import com.notesapp.dto.NoteDto;
import com.notesapp.dto.NoteResponseDto;
import com.notesapp.enums.NoteTag;
import com.notesapp.mapper.NoteMapper;
import com.notesapp.model.Note;
import com.notesapp.repository.NoteRepository;
import com.notesapp.service.AppUserService;
import com.notesapp.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notes")
public class NoteController implements NoteApi {

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private AppUserService appUserService;

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
    public List<NoteDto> getNotes(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(required = false) NoteTag tag) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Note> notesPage;
        if (tag != null) {
            notesPage = noteRepository.findByUserIdAndTags(appUserService.getAppUser().getId(), tag, pageable);
        } else {
            notesPage = noteRepository.findByUserId(appUserService.getAppUser().getId(), pageable);
        }

        return notesPage.getContent()
                .stream()
                .map(noteMapper::toNoteDto)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> getCountOfUniqWords(@PathVariable String id) {
        return noteService.getCountOfNoteWords(id);
    }

    @Override
    public NoteResponseDto getNoteById(@PathVariable String id) {
        return noteMapper.toDto(noteService.getNoteById(id));
    }
}
