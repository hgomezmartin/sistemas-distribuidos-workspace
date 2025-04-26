package com.ubu.p2.users.service;

import com.ubu.p2.users.model.User;
import com.ubu.p2.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional; //probar a quitar

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * comprueba si existe un user con username y password que coincidan
     */
    public boolean checkRegistered(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    /**
     * retorna el usuario que tenga ese username o null si no existe
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);   // ya puede devolver null
    }

    /**
     * retorna el usuario con el id dado o null si no existe
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * actualiza el perfil de un usuario (username, contraseña o email) segun el id
     */
    public void updateProfile(Long id, String username, String password, String email) {
        User user = getUserById(id);
        if (user != null) {
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            userRepository.save(user);
        }
    }

    /**
     * retorna todos los usuarios de la BD.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
