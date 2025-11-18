package com.notesapp.dto;

import com.notesapp.enums.NoteTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteResponseDto {

    private String id;

    private String title;

    private String text;

    private LocalDateTime createdAt;

    private Set<NoteTag> tags;
}
