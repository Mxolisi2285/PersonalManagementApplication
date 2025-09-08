package za.ac.mxolisi.prayer.PersonalManagementApplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.ac.mxolisi.prayer.PersonalManagementApplication.model.User;
import za.ac.mxolisi.prayer.PersonalManagementApplication.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ------------------ REGISTER ------------------
    public String registerUser(String username, String email, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            return "Username already exists!";
        }
        if (userRepository.findByEmail(email).isPresent()) {
            return "Email already exists!";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // hash password
        userRepository.save(user);

        return null; // null = success
    }

    // ------------------ AUTHENTICATE ------------------
    public boolean authenticate(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        return userOpt.filter(user -> passwordEncoder.matches(password, user.getPassword())).isPresent();
    }

    // ------------------ FIND BY EMAIL ------------------
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ------------------ PASSWORD RESET TOKEN ------------------
    public String createPasswordResetToken(User user) {
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepository.save(user);
        return token;
    }

    public Optional<User> findByResetToken(String token) {
        return userRepository.findByResetToken(token);
    }

    // ------------------ UPDATE PASSWORD ------------------
    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword)); // hash new password
        user.setResetToken(null); // clear token after use
        userRepository.save(user);
    }

    // ------------------ SAVE USER ------------------
    public User saveUser(User user) {
        return userRepository.save(user);
    }

}
