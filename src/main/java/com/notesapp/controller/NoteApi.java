package com.notesapp.controller;

import com.notesapp.dto.NoteCreateDto;
import com.notesapp.dto.NoteDto;
import com.notesapp.dto.NoteResponseDto;
import com.notesapp.enums.NoteTag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "The Notes App", description = "API for creating, editing, and managing your notes. " +
        "Allows adding a title, text, and tags, retrieving a list of notes, filtering by tags, " +
        "and viewing statistics of unique words used in each note.")
public interface NoteApi {

    @Operation(summary = "Create a new note")
    @ApiResponse(responseCode = "201", description = "Note created")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    NoteResponseDto createNote(@RequestBody NoteCreateDto note);

    @Operation(summary = "Update an existing note by ID")
    @ApiResponse(responseCode = "200", description = "Note updated")
    @PutMapping("/{id}")
    NoteResponseDto updateNote(@PathVariable String id, @RequestBody NoteCreateDto note);

    @Operation(summary = "Delete a note by ID")
    @ApiResponse(responseCode = "204", description = "Note deleted")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteNote(@PathVariable String id);

    @Operation(summary = "Get a paginated list of notes")
    @ApiResponse(responseCode = "200", description = "Notes list returned")
    @GetMapping
    List<NoteDto> getNotes(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(required = false) NoteTag tag);

    @Operation(summary = "Get unique words count in a note")
    @ApiResponse(responseCode = "200", description = "Word count returned")
    @GetMapping("/{id}/stats")
    Map<String, Integer> getCountOfUniqWords(@PathVariable String id);
}
