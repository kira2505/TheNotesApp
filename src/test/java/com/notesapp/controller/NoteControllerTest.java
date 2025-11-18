package com.notesapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notesapp.dto.NoteCreateDto;
import com.notesapp.dto.NoteResponseDto;
import com.notesapp.enums.NoteTag;
import com.notesapp.mapper.NoteMapper;
import com.notesapp.model.Note;
import com.notesapp.security.JwtService;
import com.notesapp.service.NoteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
@AutoConfigureMockMvc(addFilters = false)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private NoteMapper noteMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateNote() throws Exception {
        NoteCreateDto createDto = new NoteCreateDto("new title", "new text", Set.of(NoteTag.BUSINESS));
        Note model = new Note();
        NoteResponseDto response = new NoteResponseDto();
        response.setId("id1");
        response.setTitle("new title");
        response.setText("new text");
        response.setTags(Set.of(NoteTag.BUSINESS));

        Mockito.when(noteMapper.toModel(any())).thenReturn(model);
        Mockito.when(noteService.createNote(any())).thenReturn(model);
        Mockito.when(noteMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(post("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated()) // 201
                .andExpect(jsonPath("$.id").value("id1"))
                .andExpect(jsonPath("$.title").value("new title"))
                .andExpect(jsonPath("$.text").value("new text"))
                .andExpect(jsonPath("$.tags[0]").value("BUSINESS"));
    }

    @Test
    void testUpdateNote() throws Exception {
        NoteCreateDto updateDto = new NoteCreateDto("updated title", "updated text", Set.of(NoteTag.PERSONAL));
        Note model = new Note();
        NoteResponseDto response = new NoteResponseDto();
        response.setId("id1");
        response.setTitle("updated title");
        response.setText("updated text");
        response.setTags(Set.of(NoteTag.PERSONAL));

        Mockito.when(noteMapper.toModel(any())).thenReturn(model);
        Mockito.when(noteService.updateNote(eq("id1"), any())).thenReturn(model);
        Mockito.when(noteMapper.toDto(model)).thenReturn(response);

        mockMvc.perform(put("/notes/id1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("id1"))
                .andExpect(jsonPath("$.title").value("updated title"))
                .andExpect(jsonPath("$.text").value("updated text"))
                .andExpect(jsonPath("$.tags[0]").value("PERSONAL"));
    }

    @Test
    void testDeleteNote() throws Exception {
        mockMvc.perform(delete("/notes/id1"))
                .andExpect(status().isNoContent()); // 204 No Content

        Mockito.verify(noteService).deleteNote("id1");
    }

    @Test
    void testGetNotesWithoutTag() throws Exception {
        Note note = new Note();
        note.setId("id1");
        note.setTitle("title");
        note.setText("text");

        NoteResponseDto response = new NoteResponseDto();
        response.setId("id1");
        response.setTitle("title");
        response.setText("text");
        response.setTags(Set.of(NoteTag.PERSONAL));

        Page<Note> page = new PageImpl<>(List.of(note));

        Mockito.when(noteService.getAllNotes(any(Pageable.class))).thenReturn(page);
        Mockito.when(noteMapper.toDtoPage(any(Page.class)))
                .thenAnswer(invocation -> {
                    Page<Note> notesPage = invocation.getArgument(0);
                    return notesPage.map(n -> response);
                });

        mockMvc.perform(get("/notes?page=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("id1"))
                .andExpect(jsonPath("$.content[0].title").value("title"))
                .andExpect(jsonPath("$.content[0].tags[0]").value("PERSONAL"));
    }

    @Test
    void testGetNotesWithTag() throws Exception {
        Note note = new Note();
        note.setId("id1");
        note.setTitle("title");
        note.setText("text");

        NoteResponseDto response = new NoteResponseDto();
        response.setId("id1");
        response.setTitle("title");
        response.setText("text");
        response.setTags(Set.of(NoteTag.IMPORTANT));

        Page<Note> page = new PageImpl<>(List.of(note));

        Mockito.when(noteService.getNotesByTag(eq(NoteTag.IMPORTANT), any(Pageable.class)))
                .thenReturn(page);

        Mockito.when(noteMapper.toDtoPage(any(Page.class)))
                .thenAnswer(invocation -> {
                    Page<Note> notesPage = invocation.getArgument(0);
                    return notesPage.map(n -> response);
                });

        mockMvc.perform(get("/notes?page=0&tag=IMPORTANT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("id1"))
                .andExpect(jsonPath("$.content[0].tags[0]").value("IMPORTANT"));
    }

    @Test
    void testGetCountOfUniqWords() throws Exception {
        Map<String, Integer> words = Map.of("hello", 2, "world", 1);
        Mockito.when(noteService.getCountOfNoteWords("id1")).thenReturn(words);

        mockMvc.perform(get("/notes/{id}/stats", "id1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hello").value(2))
                .andExpect(jsonPath("$.world").value(1));
    }
}