package com.trabajo_practico.gestion_comercial.controller;

import com.trabajo_practico.gestion_comercial.model.Usuario;
import com.trabajo_practico.gestion_comercial.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${api.version}/usuarios")
public class UsuarioController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(token);
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
    // Endpoint para crear un nuevo usuario
    //@PostMapping
    //public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
    //    Usuario savedUsuario = UsuarioService.save(usuario);
     //   return ResponseEntity.ok(savedUsuario);
    //}
}
