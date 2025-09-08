package za.ac.mxolisi.prayer.PersonalManagementApplication.model;

import jakarta.persistence.*;

@Entity
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    private String username; // associate note with user

    // ---------------- Constructors ----------------
    public Note() {
    }

    public Note(String title, String content, String username) {
        this.title = title;
        this.content = content;
        this.username = username;
    }

    // ---------------- Getters ----------------
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUsername() {
        return username;
    }

    // ---------------- Setters ----------------
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
