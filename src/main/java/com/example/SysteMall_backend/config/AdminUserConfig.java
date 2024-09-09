package com.example.SysteMall_backend.config;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.SysteMall_backend.entity.Role;
import com.example.SysteMall_backend.entity.User;
import com.example.SysteMall_backend.repository.RoleRepository;
import com.example.SysteMall_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(RoleRepository roleRepository,
                           UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Verifica se o papel de administrador já existe no banco de dados
        var roleAdminOptional = roleRepository.findByName(Role.Values.ADMIN.name());

        // Se o papel de administrador não existir, cria-o
        if (roleAdminOptional.isEmpty()) {
            Role roleAdmin = new Role();
            roleAdmin.setName(Role.Values.ADMIN.name());
            roleRepository.save(roleAdmin);
            roleAdminOptional = Optional.of(roleAdmin);
        }

        // Verifica se o usuário admin já existe no banco de dados
        var userAdminOptional = userRepository.findByUsername("admin2");

        Optional<Role> finalRoleAdminOptional = roleAdminOptional;
        userAdminOptional.ifPresentOrElse(
                user -> {
                    System.out.println("Admin já existe");
                },
                () -> {
                    // Se o usuário admin não existir, cria-o
                    var user = new User();
                    user.setUsername("admin2");
                    user.setPassword(passwordEncoder.encode("password"));
                    // Obtém o papel de administrador do Optional
                    Role roleAdmin = finalRoleAdminOptional.get();
                    // Adiciona o papel de administrador ao usuário
                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                }
        );
    }
}