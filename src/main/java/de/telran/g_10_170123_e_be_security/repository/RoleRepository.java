package de.telran.g_10_170123_e_be_security.repository;

import de.telran.g_10_170123_e_be_security.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}