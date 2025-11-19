package com.notesapp.service;

import com.notesapp.enums.NoteTag;
import com.notesapp.model.AppUser;
import com.notesapp.model.Note;
import com.notesapp.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TestNoteGenerator {

    private final NoteRepository noteRepository;

    private final Random random = new Random();

    private final String[] words = {"Java", "Spring", "Docker", "MongoDB", "API", "Note", "Hello", "World"};

    private final List<Set<NoteTag>> tagCombinations = List.of(
            Set.of(NoteTag.BUSINESS),
            Set.of(NoteTag.PERSONAL),
            Set.of(NoteTag.IMPORTANT),
            Set.of(NoteTag.BUSINESS, NoteTag.PERSONAL),
            Set.of(NoteTag.BUSINESS, NoteTag.IMPORTANT),
            Set.of(NoteTag.PERSONAL, NoteTag.IMPORTANT));

    public void createRandomNotesForUser(AppUser user) {
        for (int i = 1; i <= 15; i++) {
            Note note = new Note();
            note.setTitle(user.getUsername() + " Note " + i);

            StringBuilder text = new StringBuilder();
            for (int j = 0; j < 20; j++) {
                text.append(words[random.nextInt(words.length)]).append(" ");
            }
            note.setText(text.toString().trim());

            Set<NoteTag> tags = tagCombinations.get(i % tagCombinations.size());
            note.setTags(tags);

            note.setUserId(user.getId());

            noteRepository.save(note);
        }
    }
}
