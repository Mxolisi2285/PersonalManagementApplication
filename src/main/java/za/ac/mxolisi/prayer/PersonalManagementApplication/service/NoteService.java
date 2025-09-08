package za.ac.mxolisi.prayer.PersonalManagementApplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.mxolisi.prayer.PersonalManagementApplication.model.Note;
import za.ac.mxolisi.prayer.PersonalManagementApplication.repository.NoteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    // Get all notes for a user
    public List<Note> getNotesByUser(String username) {
        return noteRepository.findByUsername(username);
    }

    // Create a new note
    public Note createNote(String username, String title, String content) {
        Note note = new Note();
        note.setUsername(username);
        note.setTitle(title);
        note.setContent(content);
        return noteRepository.save(note);
    }

    // Get a note by ID
    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }

    // Update a note
    public Note updateNote(Long id, String title, String content) {
        Optional<Note> noteOpt = noteRepository.findById(id);
        if (noteOpt.isPresent()) {
            Note note = noteOpt.get();
            note.setTitle(title);
            note.setContent(content);
            return noteRepository.save(note);
        }
        return null;
    }

    // Delete a note
    public void deleteNote(Long id, String username) {
        Optional<Note> noteOpt = noteRepository.findById(id);
        if (noteOpt.isPresent() && noteOpt.get().getUsername().equals(username)) {
            noteRepository.deleteById(id);
        }
    }
}
