package com.bangnhi.server.controller;

import com.bangnhi.server.model.Note;
import com.bangnhi.server.repository.NoteRepository;
import com.bangnhi.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class NoteController {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/notes")
    public @ResponseBody Iterable<Note> getNotes() {
        return noteRepository.findAll();
    }

    @PostMapping("/addNote")
    public ResponseEntity<String> addNote(@RequestParam String title, @RequestParam String description) {
        if (title.isEmpty()) {
            return new ResponseEntity<>("Title is Empty!", null, HttpStatus.BAD_REQUEST);
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Note newNote = new Note(userRepository.findByUsername(username), title, description, new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
        noteRepository.save(newNote);
        return new ResponseEntity<>("Add Note Success", null, HttpStatus.OK);
    }

    @GetMapping("/note/{noteId}")
    public @ResponseBody Note getNote(@PathVariable Long noteId) {
        return noteRepository.findNoteById(noteId);
    }

    @PostMapping("/editNote/{noteId}")
    public ResponseEntity<String> editNote(@PathVariable Long noteId, @RequestParam String title, @RequestParam String description) {
        if (title.isEmpty()) {
            return new ResponseEntity<>("Title is Empty!", null, HttpStatus.BAD_REQUEST);
        }
        Note note = noteRepository.findNoteById(noteId);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!note.getUser().getId().equals(userRepository.findByUsername(username).getId())) {
            return new ResponseEntity<>("Edit Failed!", null, HttpStatus.BAD_REQUEST);
        }
        note.setTitle(title);
        note.setDescription(description);
        noteRepository.save(note);
        return new ResponseEntity<>("Edit Note Success", null, HttpStatus.OK);
    }

    @PostMapping("/removeNote/{noteId}")
    public ResponseEntity<String> removeNote(@PathVariable Long noteId) {
        noteRepository.deleteNoteById(noteId);
        return new ResponseEntity<>("Delete Note Success", null, HttpStatus.OK);
    }
}
