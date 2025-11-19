package com.notesapp.util;

import com.notesapp.enums.NoteTag;
import com.notesapp.enums.Role;
import com.notesapp.model.AppUser;
import com.notesapp.model.Note;
import com.notesapp.repository.AppUserRepository;
import com.notesapp.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

@Configuration
public class DatabaseSeeder {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedDatabase(AppUserRepository userRepository, NoteRepository noteRepository) {
        return args -> {
            AppUser alice = AppUser.builder()
                    .username("admin")
                    .passwordHash(passwordEncoder.encode("admin123"))
                    .role(Role.ROLE_USER)
                    .build();

            AppUser bob = AppUser.builder()
                    .username("root")
                    .passwordHash(passwordEncoder.encode("rootroot"))
                    .role(Role.ROLE_USER)
                    .build();

            alice = userRepository.save(alice);
            bob = userRepository.save(bob);

            createRandomNotesForUser(alice, noteRepository);
            createRandomNotesForUser(bob, noteRepository);
        };
    }

    private void createRandomNotesForUser(AppUser user, NoteRepository noteRepository) {
        Random random = new Random();
        String[] words = {"Java", "Spring", "Docker", "MongoDB", "API", "Note", "Hello", "World", "Swagger"};
        List<Set<NoteTag>> tagCombinations = List.of(
                Set.of(NoteTag.BUSINESS),
                Set.of(NoteTag.PERSONAL),
                Set.of(NoteTag.IMPORTANT)
        );

        for (int i = 1; i <= 15; i++) {
            Note note = new Note();
            note.setTitle(user.getUsername() + " Note " + i);

            StringBuilder text = new StringBuilder();
            for (int j = 0; j < 20; j++) {
                text.append(words[random.nextInt(words.length)]).append(" ");
            }
            note.setText(text.toString().trim());
            note.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(30)));
            note.setTags(tagCombinations.get(i % tagCombinations.size()));
            note.setUserId(user.getId());

            noteRepository.save(note);
        }
    }
}
