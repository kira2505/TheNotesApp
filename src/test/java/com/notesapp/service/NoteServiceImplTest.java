package com.notesapp.service;

import com.notesapp.enums.NoteTag;
import com.notesapp.exception.InvalidTagException;
import com.notesapp.exception.NoteNotFoundException;
import com.notesapp.model.AppUser;
import com.notesapp.model.Note;
import com.notesapp.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceImplTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private TextService textService;

    @Mock
    private AppUserService appUserService;

    @InjectMocks
    private NoteServiceImpl noteService;

    @Test
    void testGetNoteById_found() {
        Note note = new Note();
        note.setId("123");

        when(noteRepository.findById("123")).thenReturn(Optional.of(note));

        Note result = noteService.getNoteById("123");

        assertEquals("123", result.getId());
    }

    @Test
    void testGetNoteById_notFound() {
        when(noteRepository.findById("123")).thenReturn(Optional.empty());
        assertThrows(NoteNotFoundException.class, () -> noteService.getNoteById("123"));
    }

    @Test
    void testCreateNote_success() {
        Note note = new Note();
        note.setTags(Set.of(NoteTag.PERSONAL));

        when(noteRepository.save(note)).thenReturn(note);

        Note saved = noteService.createNote(note);

        verify(noteRepository).save(note);
        assertNotNull(saved);
    }

    @Test
    void testUpdateNote_success() {
        Note existing = new Note();
        existing.setId("1");
        existing.setTitle("Old");
        existing.setText("Old text");
        existing.setTags(Set.of(NoteTag.PERSONAL));

        Note updated = new Note();
        updated.setTitle("New");
        updated.setText("New text");
        updated.setTags(Set.of(NoteTag.IMPORTANT));

        when(noteRepository.findById("1")).thenReturn(Optional.of(existing));
        when(noteRepository.save(existing)).thenReturn(existing);

        Note result = noteService.updateNote("1", updated);

        assertEquals("New", result.getTitle());
        assertEquals("New text", result.getText());
        assertEquals(Set.of(NoteTag.IMPORTANT), result.getTags());
    }

    @Test
    void testDeleteNote_success() {
        Note note = new Note();
        when(noteRepository.findById("1")).thenReturn(Optional.of(note));

        noteService.deleteNote("1");

        verify(noteRepository).delete(note);
    }

    @Test
    void testGetAllNotes() {
        Page<Note> page = mock(Page.class);
        Pageable pageable = mock(Pageable.class);

        var user = new AppUser();
        user.setId("user123");

        when(appUserService.getAppUser()).thenReturn(user);
        when(noteRepository.findByUserId("user123", pageable)).thenReturn(page);

        Page<Note> result = noteService.getAllNotes(pageable);

        assertEquals(page, result);
    }

    @Test
    void testGetNotesByTag() {
        Page<Note> page = mock(Page.class);
        Pageable pageable = mock(Pageable.class);

        var user = new AppUser();
        user.setId("u1");

        when(appUserService.getAppUser()).thenReturn(user);
        when(noteRepository.findByUserIdAndTags("u1", NoteTag.IMPORTANT, pageable))
                .thenReturn(page);

        Page<Note> result = noteService.getNotesByTag(NoteTag.IMPORTANT, pageable);

        assertEquals(page, result);
    }

    @Test
    void testGetCountOfNoteWords() {
        Note note = new Note();
        note.setId("1");
        note.setText("hello world");

        when(noteRepository.findById("1")).thenReturn(Optional.of(note));
        when(textService.getCountOfUniqWords("hello world"))
                .thenReturn(Map.of("hello", 1, "world", 1));

        Map<String, Integer> result = noteService.getCountOfNoteWords("1");

        assertEquals(2, result.size());
        verify(textService).getCountOfUniqWords("hello world");
    }

    @Test
    void testTagValid_success() {
        Note note = new Note();
        note.setTags(Set.of(NoteTag.BUSINESS, NoteTag.PERSONAL));
        assertDoesNotThrow(() -> noteService.isTagValid(note));
    }

    @Test
    void testTagValid_invalidTag() {
        Note note = new Note();

        Set<NoteTag> tags = new HashSet<>();
        tags.add(NoteTag.BUSINESS);
        tags.add(null); // здесь эмулируем невалидный тег

        note.setTags(tags);

        assertThrows(InvalidTagException.class, () -> noteService.isTagValid(note));
    }
}