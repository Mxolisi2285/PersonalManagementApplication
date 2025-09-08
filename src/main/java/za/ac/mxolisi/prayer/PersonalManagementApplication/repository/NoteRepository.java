package za.ac.mxolisi.prayer.PersonalManagementApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.mxolisi.prayer.PersonalManagementApplication.model.Note;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUsername(String username);
}
