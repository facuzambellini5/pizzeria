//package com.example.pizzeria;
//
//import com.example.pizzeria.enums.UserRole;
//import com.example.pizzeria.models.User;
//import com.example.pizzeria.repositories.IUserRepository;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//@Component
//public class DataInitializer {
//
//    @Autowired
//    private IUserRepository userRepo;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @PostConstruct
//    @Transactional
//    public void init() {
//        crearUsuarioSiNoExiste("Mostrador", "1234", UserRole.MANAGER);
//        crearUsuarioSiNoExiste("Dueño", "1234", UserRole.OWNER);
//        crearUsuarioSiNoExiste("Cocina", "1234", UserRole.COOKER);
//    }
//
//    private void crearUsuarioSiNoExiste(String username, String rawPassword, UserRole role) {
//        if (!userRepo.existsByUsername(username)) {
//            // ¡Aquí ocurre la magia! Encriptamos antes de guardar
//            String passwordEncriptado = passwordEncoder.encode(rawPassword);
//
//            User nuevoUsuario = new User();
//            nuevoUsuario.setUsername(username);
//            nuevoUsuario.setPassword(passwordEncriptado);
//            nuevoUsuario.setRole(role);
//
//            userRepo.save(nuevoUsuario);
//            System.out.println("Usuario " + username + " creado con éxito.");
//        }
//    }
//}
