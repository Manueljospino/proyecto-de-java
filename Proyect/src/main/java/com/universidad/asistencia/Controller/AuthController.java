package com.universidad.asistencia.Controller;
import com.universidad.asistencia.Entities.*;
import com.universidad.asistencia.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.login(request.getNumberId(), request.getPassword());
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
             authService.register(request);
            return ResponseEntity.ok("Usuario registrado exitosamente!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<?> perfil() {
        return ResponseEntity.ok("Acceso autorizado");
    }


    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            authService.resetPassword(request.getNumberId(), request.getNewPassword());
            return ResponseEntity.ok("Contraseña actualizada exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            String numberId = SecurityContextHolder.getContext().getAuthentication().getName();
            authService.changePassword(numberId, request.getCurrentPassword(), request.getNewPassword());
            return ResponseEntity.ok("Contraseña actualizada exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{numberId}")
    public ResponseEntity<?> deleteUser(@PathVariable String numberId) {
        try {
            authService.deleteUser(numberId);
            return ResponseEntity.ok("Usuario eliminado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/find/{numberId}")
    public ResponseEntity<?> findUser(@PathVariable String numberId) {
        try {
            Person person = authService.findUser(numberId);
            return ResponseEntity.ok(person);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(authService.getAllUsers());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}