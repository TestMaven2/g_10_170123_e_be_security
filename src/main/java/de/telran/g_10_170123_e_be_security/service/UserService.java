package de.telran.g_10_170123_e_be_security.service;

import de.telran.g_10_170123_e_be_security.domain.entity.Role;
import de.telran.g_10_170123_e_be_security.domain.entity.User;
import de.telran.g_10_170123_e_be_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = repository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }

        return user;
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public void save(User user) {
        UserDetails foundUser = repository.findByUsername(user.getUsername());

        if (foundUser != null) {
            return;
        }

        Set<Role> roles = Set.of(new Role(2, "ROLE_USER"));
        user.setRoles(roles);

        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        repository.save(user);
    }
}