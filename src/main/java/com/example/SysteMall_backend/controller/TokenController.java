package com.example.SysteMall_backend.controller;


import com.example.SysteMall_backend.DTOs.LoginRequestDTO;
import com.example.SysteMall_backend.DTOs.LoginResponseDTO;
import com.example.SysteMall_backend.entity.Role;
import com.example.SysteMall_backend.entity.User;
import com.example.SysteMall_backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "https://emporioverdegraos.vercel.app")
public class TokenController {

    private JwtEncoder jwtEncoder;
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    public TokenController(JwtEncoder jwtEncoder, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login (@RequestBody LoginRequestDTO loginRequestDTO){
        Optional<User> userOptional = userRepository.findByUsername(loginRequestDTO.username());

           if(userOptional.isEmpty() || !userOptional.get().isLoginCorrect(loginRequestDTO, passwordEncoder)) {
               throw new BadCredentialsException("User or password is invalid!");
           }


           var now = Instant.now();
           var expiresIn = 9000000000L;

           var scopes = userOptional.get().getRoles()
                   .stream()
                   .map(Role::getName)
                   .collect(Collectors.joining(" "));



           var claims = JwtClaimsSet.builder()
                   .issuer("mybackend")
                   .subject(userOptional.get().getId().toString())
                   .expiresAt(now.plusSeconds(expiresIn))
                   .issuedAt(now)
                   .expiresAt(now.plusSeconds(expiresIn))
                   .claim("scope", scopes)
                   .build();

           var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

           return ResponseEntity.ok(new LoginResponseDTO(jwtValue, expiresIn));


    }


}
