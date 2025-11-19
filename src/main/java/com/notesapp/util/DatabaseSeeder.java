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
    CommandLineRunner seed(AppUserRepository userRepository, NoteRepository noteRepository) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }

            AppUser admin = AppUser.builder()
                    .username("admin")
                    .passwordHash(passwordEncoder.encode("admin123"))
                    .role(Role.ROLE_USER)
                    .build();

            AppUser root = AppUser.builder()
                    .username("root")
                    .passwordHash(passwordEncoder.encode("rootroot"))
                    .role(Role.ROLE_USER)
                    .build();

            admin = userRepository.save(admin);
            root = userRepository.save(root);

            createRandomNotes(admin, noteRepository);
            createRandomNotes(root, noteRepository);
        };
    }

    private void createRandomNotes(AppUser user, NoteRepository noteRepository) {
        Random random = new Random();
        String[] words = {"Java", "Spring", "Docker", "MongoDB", "API", "Note", "Hello", "World", "Swagger"};
        List<Set<NoteTag>> tags = List.of(
                Set.of(NoteTag.BUSINESS),
                Set.of(NoteTag.PERSONAL),
                Set.of(NoteTag.IMPORTANT)
        );

        for (int i = 1; i <= 15; i++) {
            Note note = new Note();
            note.setTitle(user.getUsername() + " Note " + i);

            StringBuilder text = new StringBuilder();
            for (int j = 0; j < 20; j++)
                text.append(words[random.nextInt(words.length)]).append(" ");

            note.setText(text.toString());
            note.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(20)));
            note.setTags(tags.get(i % tags.size()));
            note.setUserId(user.getId());

            noteRepository.save(note);
        }
    }
}
