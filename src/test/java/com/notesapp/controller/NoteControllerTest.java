package com.notesapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notesapp.dto.NoteCreateDto;
import com.notesapp.dto.NoteDto;
import com.notesapp.dto.NoteResponseDto;
import com.notesapp.enums.NoteTag;
import com.notesapp.mapper.NoteMapper;
import com.notesapp.model.AppUser;
import com.notesapp.model.Note;
import com.notesapp.repository.NoteRepository;
import com.notesapp.security.JwtService;
import com.notesapp.service.AppUserService;
import com.notesapp.service.NoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
@AutoConfigureMockMvc(addFilters = false)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NoteService noteService;

    @MockBean
    private AppUserService appUserService;

    @MockBean
    private NoteMapper noteMapper;

    @MockBean
    private NoteRepository noteRepository;

    @Test
    void testGetNotesWithoutTag() throws Exception {
        AppUser mockUser = new AppUser();
        mockUser.setId("user1");
        when(appUserService.getAppUser()).thenReturn(mockUser);

        Note note = new Note();
        note.setId("id1");
        note.setTitle("title");
        note.setText("text");
        note.setTags(Set.of(NoteTag.PERSONAL));
        note.setCreatedAt(LocalDateTime.now());

        Page<Note> notePage = new PageImpl<>(List.of(note));

        when(noteRepository.findByUserId(eq("user1"), any(Pageable.class))).thenReturn(notePage);

        NoteDto dto = new NoteDto();
        dto.setTitle("title");
        dto.setCreatedAt(note.getCreatedAt());
        when(noteMapper.toNoteDto(any())).thenReturn(dto);

        mockMvc.perform(get("/notes?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("title"));
    }

    @Test
    void testGetNotesWithTag() throws Exception {
        AppUser mockUser = new AppUser();
        mockUser.setId("user1");
        when(appUserService.getAppUser()).thenReturn(mockUser);

        Note note = new Note();
        note.setId("id1");
        note.setTitle("title");
        note.setText("text");
        note.setTags(Set.of(NoteTag.IMPORTANT));
        note.setCreatedAt(LocalDateTime.now());

        Page<Note> notePage = new PageImpl<>(List.of(note));

        when(noteRepository.findByUserIdAndTags(eq("user1"), eq(NoteTag.IMPORTANT), any(Pageable.class)))
                .thenReturn(notePage);

        NoteDto dto = new NoteDto();
        dto.setTitle("title");
        dto.setCreatedAt(note.getCreatedAt());
        when(noteMapper.toNoteDto(any())).thenReturn(dto);

        mockMvc.perform(get("/notes?page=0&size=10&tag=IMPORTANT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("title"));
    }

    @Test
    void testCreateNote() throws Exception {
        NoteCreateDto createDto = new NoteCreateDto();
        createDto.setTitle("title");
        createDto.setText("text");
        createDto.setTags(Set.of(NoteTag.PERSONAL));

        Note note = new Note();
        note.setId("id1");
        note.setTitle("title");
        note.setText("text");
        note.setTags(createDto.getTags());
        note.setCreatedAt(LocalDateTime.now());

        NoteResponseDto responseDto = new NoteResponseDto();
        responseDto.setId("id1");
        responseDto.setTitle("title");
        responseDto.setText("text");
        responseDto.setTags(createDto.getTags());
        responseDto.setCreatedAt(note.getCreatedAt());

        when(noteMapper.toModel(any(NoteCreateDto.class))).thenReturn(note);
        when(noteService.createNote(any(Note.class))).thenReturn(note);
        when(noteMapper.toDto(any(Note.class))).thenReturn(responseDto);

        mockMvc.perform(post("/notes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("id1"))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.tags[0]").value("PERSONAL"));
    }

    @Test
    void testUpdateNote() throws Exception {
        NoteCreateDto updateDto = new NoteCreateDto();
        updateDto.setTitle("new title");
        updateDto.setText("new text");
        updateDto.setTags(Set.of(NoteTag.IMPORTANT));

        Note note = new Note();
        note.setId("id1");
        note.setTitle("new title");
        note.setText("new text");
        note.setTags(updateDto.getTags());
        note.setCreatedAt(LocalDateTime.now());

        NoteResponseDto responseDto = new NoteResponseDto();
        responseDto.setId("id1");
        responseDto.setTitle("new title");
        responseDto.setText("new text");
        responseDto.setTags(updateDto.getTags());
        responseDto.setCreatedAt(note.getCreatedAt());

        when(noteMapper.toModel(any(NoteCreateDto.class))).thenReturn(note);
        when(noteService.updateNote(eq("id1"), any(Note.class))).thenReturn(note);
        when(noteMapper.toDto(any(Note.class))).thenReturn(responseDto);

        mockMvc.perform(patch("/notes/id1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("new title"))
                .andExpect(jsonPath("$.tags[0]").value("IMPORTANT"));
    }

    @Test
    void testDeleteNote() throws Exception {
        doNothing().when(noteService).deleteNote("id1");

        mockMvc.perform(delete("/notes/id1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetCountOfUniqWords() throws Exception {
        Map<String, Integer> stats = Map.of("Java", 3, "Spring", 2);

        when(noteService.getCountOfNoteWords("id1")).thenReturn(stats);

        mockMvc.perform(get("/notes/id1/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Java").value(3))
                .andExpect(jsonPath("$.Spring").value(2));
    }

    @Test
    void testGetNoteById_success() throws Exception {
        Note note = new Note();
        note.setId("id1");
        note.setTitle("Test Note");
        note.setText("Some text");
        note.setTags(Set.of(NoteTag.PERSONAL));
        note.setCreatedAt(LocalDateTime.now());

        NoteResponseDto responseDto = new NoteResponseDto();
        responseDto.setId("id1");
        responseDto.setTitle("Test Note");
        responseDto.setText("Some text");
        responseDto.setTags(Set.of(NoteTag.PERSONAL));
        responseDto.setCreatedAt(note.getCreatedAt());

        when(noteService.getNoteById("id1")).thenReturn(note);
        when(noteMapper.toDto(note)).thenReturn(responseDto);

        mockMvc.perform(get("/notes/id1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("id1"))
                .andExpect(jsonPath("$.title").value("Test Note"))
                .andExpect(jsonPath("$.tags[0]").value("PERSONAL"));
    }
}