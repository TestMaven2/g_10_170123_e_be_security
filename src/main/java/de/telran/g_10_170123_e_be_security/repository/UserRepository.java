package de.telran.g_10_170123_e_be_security.repository;

import de.telran.g_10_170123_e_be_security.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Integer> {

    UserDetails findByUsername(String username);
}