package com.notesapp.repository;

import com.notesapp.enums.Tag;
import com.notesapp.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    Page<Note> findByTags(Tag tag, Pageable pageable);


}
