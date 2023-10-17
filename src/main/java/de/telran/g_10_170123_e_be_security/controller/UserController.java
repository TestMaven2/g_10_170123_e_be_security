package de.telran.g_10_170123_e_be_security.controller;

import de.telran.g_10_170123_e_be_security.domain.entity.User;
import de.telran.g_10_170123_e_be_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    // Доступ имеют все. В том числе незарегистрированные пользователи.
    @GetMapping("/all")
    public List<User> getAll() {
        return service.getAll();
    }

    // Доступ имеют только зарегистрированные пользователи.
    @GetMapping("/name/{name}")
    public UserDetails getByName(@PathVariable String name) {
        return service.loadUserByUsername(name);
    }

    // Доступ имеют только пользователи с ролью админ
    @PostMapping("/add")
    public void add(@RequestBody User user) {
        service.save(user);
    }
}