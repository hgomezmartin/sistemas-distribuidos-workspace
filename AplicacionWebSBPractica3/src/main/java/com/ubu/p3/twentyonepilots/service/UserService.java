package com.ubu.p3.twentyonepilots.service;

import com.ubu.p3.twentyonepilots.model.User;
import com.ubu.p3.twentyonepilots.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;


    public User register(String username, String rawPass, String email) {
        repo.findByUsername(username).ifPresent(u -> {
            throw new IllegalArgumentException("El usuario ya existe");
        });

        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(rawPass));
        u.setEmail(email);
        u.setRole("ROLE_USER");          // rol por defecto
        return repo.save(u);
    }


    public List<User> list() {
        return repo.findAll();
    }


    public User get(Long id) {
        return repo.findById(id).orElseThrow();
    }


    public User updateProfile(Long id,
                              String username,
                              String rawPass,
                              String email) {

        User u = get(id);

        if (username != null && !username.isBlank()) {
            u.setUsername(username);
        }
        if (email != null && !email.isBlank()) {
            u.setEmail(email);
        }
        if (rawPass != null && !rawPass.isBlank()) {
            u.setPassword(encoder.encode(rawPass));
        }
        return repo.save(u);
    }


    public void toggleRole(Long id) {
        User u = get(id);
        u.setRole(u.getRole().equals("ROLE_USER") ? "ROLE_ADMIN" : "ROLE_USER");
        repo.save(u);
    }


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return repo.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario " + username + " no encontrado"));
    }
}


