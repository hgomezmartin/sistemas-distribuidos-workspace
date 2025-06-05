package com.ubu.p3.twentyonepilots.service;

import com.ubu.p3.twentyonepilots.model.User;
import com.ubu.p3.twentyonepilots.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  Registro de nuevos usuarios, cambio de perfil / rol
 */

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    //alta de usuario
    public User register(String username, String rawPass, String email) {
        repo.findByUsername(username).ifPresent(u -> {
            throw new IllegalArgumentException("El usuario ya existe");
        });

        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(rawPass)); //BCrypt
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

    //prefil propio, no implementado por no tener tiempo
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

    //implementado cambio de rol, unico crud q me dio tiempo a implementar (el mas sencillo)
    public void updateRole(Long id, String newRole) {
        if(!newRole.equals("ROLE_USER") && !newRole.equals("ROLE_ADMIN"))
            throw new IllegalArgumentException("Rol no válido");
        User u = get(id);
        u.setRole(newRole);
        repo.save(u);
    }

    //sin implementar
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // el builder quita automticamente el prefijo ROLE_
        return org.springframework.security.core.userdetails.User
                .withUsername(u.getUsername())
                .password(u.getPassword())
                .roles(u.getRole().replace("ROLE_", ""))   // ← “ADMIN” o “USER”
                .build();
    }
}


