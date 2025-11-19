package com.notesapp.model;

import com.notesapp.enums.NoteTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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

    private String title;

    private String text;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Indexed
    private Set<NoteTag> tags;

    @Field("user_id")
    private String userId;
}
