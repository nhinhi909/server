package com.bangnhi.server.repository;

import com.bangnhi.server.model.Note;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NoteRepository extends CrudRepository<Note, Integer> {
    Note findNoteById(Long id);
    void deleteNoteById(Long id);
}
