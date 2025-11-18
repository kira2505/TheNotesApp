package com.notesapp.repository;

import com.notesapp.enums.NoteTag;
import com.notesapp.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    Page<Note> findByUserId(String userId, Pageable pageable);

    Page<Note> findByUserIdAndTags(String userId, NoteTag tag, Pageable pageable);
}
