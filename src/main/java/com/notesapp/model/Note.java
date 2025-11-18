package com.notesapp.model;

import com.notesapp.enums.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notes")
public class Note {

    @Id
    private String id;

    @NotBlank
    private String title;

    @NotBlank
    private String text;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private Set<Tag> tags;
}
