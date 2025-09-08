package za.ac.mxolisi.prayer.PersonalManagementApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import za.ac.mxolisi.prayer.PersonalManagementApplication.model.Note;
import za.ac.mxolisi.prayer.PersonalManagementApplication.service.EmailService;
import za.ac.mxolisi.prayer.PersonalManagementApplication.service.NoteService;
import za.ac.mxolisi.prayer.PersonalManagementApplication.service.UserService;

import java.security.Principal;

@Controller
public class AppController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NoteService noteService;

    // ---------------- LOGIN ----------------
    @GetMapping("/login")
    public String showLogin(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String registered,
                            Model model) {
        if (error != null) model.addAttribute("errorMessage", "Invalid credentials!");
        if (registered != null) model.addAttribute("successMessage", "Registration successful! Please login.");
        return "login";
    }

    // ---------------- SIGNUP ----------------
    @GetMapping("/signup")
    public String showSignup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@RequestParam String username,
                                @RequestParam String email,
                                @RequestParam String password,
                                Model model) {
        String error = userService.registerUser(username, email, password);
        if (error != null) {
            model.addAttribute("errorMessage", error);
            return "signup";
        }
        return "redirect:/login?registered=true";
    }

    // ---------------- FORGOT PASSWORD ----------------
    @GetMapping("/forgotPassword")
    public String showForgotPassword() {
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String processForgotPassword(@RequestParam String email, Model model) {
        return userService.findByEmail(email)
                .map(user -> {
                    String token = userService.createPasswordResetToken(user);
                    String resetLink = "http://localhost:8080/resetPassword?token=" + token;
                    emailService.sendEmail(email, "Password Reset Link",
                            "Click the link to reset your password: " + resetLink);
                    return "redirect:/login?resetLinkSent=true";
                })
                .orElse("redirect:/forgotPassword?error=true");
    }

    // ---------------- RESET PASSWORD ----------------
    @GetMapping("/resetPassword")
    public String showResetPassword(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "resetPassword";
    }

    @PostMapping("/resetPassword")
    public String processResetPassword(@RequestParam String token,
                                       @RequestParam String newPassword) {
        return userService.findByResetToken(token)
                .map(user -> {
                    userService.updatePassword(user, newPassword);
                    return "redirect:/login?passwordReset=true";
                })
                .orElse("redirect:/forgotPassword?error=true");
    }

    // ---------------- HOME / VIEW NOTES ----------------
    @GetMapping({"/home", "/notes"})
    public String getNotes(Model model, Principal principal) {
        model.addAttribute("notes", noteService.getNotesByUser(principal.getName()));
        return "home";
    }

    // ---------------- CREATE NOTE ----------------
    @PostMapping("/notes/create")
    public String createNote(@RequestParam String title,
                             @RequestParam String content,
                             Principal principal) {
        noteService.createNote(principal.getName(), title, content);
        return "redirect:/notes";
    }

    // ---------------- DELETE NOTE ----------------
    @GetMapping("/notes/delete/{id}")
    public String deleteNote(@PathVariable Long id, Principal principal) {
        noteService.deleteNote(id, principal.getName());
        return "redirect:/notes";
    }

    // ---------------- EDIT NOTE PAGE ----------------
    @GetMapping("/notes/edit/{id}")
    public String editNote(@PathVariable Long id, Model model) {
        Note note = noteService.getNoteById(id).orElse(null);
        if (note != null) {
            model.addAttribute("note", note);
            return "editNote";
        }
        return "redirect:/notes";
    }

    // ---------------- UPDATE NOTE ----------------
    @PostMapping("/notes/update")
    public String updateNote(@RequestParam Long id,
                             @RequestParam String title,
                             @RequestParam String content) {
        noteService.updateNote(id, title, content);
        return "redirect:/notes";
    }
}
