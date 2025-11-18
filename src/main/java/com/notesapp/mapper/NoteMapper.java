package com.notesapp.mapper;

import com.notesapp.dto.NoteCreateDto;
import com.notesapp.dto.NoteDto;
import com.notesapp.dto.NoteResponseDto;
import com.notesapp.model.Note;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface NoteMapper {

    Note toModel(NoteCreateDto noteDto);

    NoteResponseDto toDto(Note note);

    NoteDto toNoteDto(Note note);

    default Page<NoteDto> toDtoPage(Page<Note> notes) {
        return notes.map(this::toNoteDto);
    }
}
