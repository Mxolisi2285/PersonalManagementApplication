package za.ac.mxolisi.prayer.PersonalManagementApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.mxolisi.prayer.PersonalManagementApplication.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByResetToken(String token);

}

