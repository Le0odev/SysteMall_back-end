package com.example.SysteMall_backend.entity;

import com.example.SysteMall_backend.DTOs.LoginRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_ids")

    )


    private Set<Role> roles;

    public boolean isLoginCorrect(LoginRequestDTO loginRequestDTO, PasswordEncoder passwordEncoder) {
       return passwordEncoder.matches(loginRequestDTO.password(), this.password);


    }


}
