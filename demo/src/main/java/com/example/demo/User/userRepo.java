package com.example.demo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface userRepo extends JpaRepository<user,Long> {
    Optional<user> findByUsername(String username);
    Optional<user> findByphoneNumber(String phoneNumber);
}
