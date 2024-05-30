package com.example.SysteMall_backend.controller;


import com.example.SysteMall_backend.DTOs.CreateUserDTO;
import com.example.SysteMall_backend.entity.Role;
import com.example.SysteMall_backend.entity.User;
import com.example.SysteMall_backend.repository.RoleRepository;
import com.example.SysteMall_backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    @PostMapping("/users")
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDTO createUserDTOdto){

        var basicRoleOptional = roleRepository.findByName(Role.Values.ADMIN.name());
        if (basicRoleOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Role BASIC not found");
        } else {
            System.out.println("Basic role found: " + basicRoleOptional.get());


        }

        if (userRepository.existsByUsername(createUserDTOdto.username())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User already exists");
        }

        var user = new User();
        user.setUsername(createUserDTOdto.username());
        user.setPassword(passwordEncoder.encode(createUserDTOdto.password()));

        Role basicRole = basicRoleOptional.get();
        Set<Role> roles = new HashSet<>();
        roles.add(basicRole);
        user.setRoles(roles);

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> listUsers() {
        var users = userRepository.findAll();
        return ResponseEntity.ok(users);


    }





}
