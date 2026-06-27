package com.jozo.multitenancy2.user;

import com.jozo.multitenancy2.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public record RegisterRequest(String email, String senha) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Se desejar reativar o cadastro de usuários no seu fork, comente a linha abaixo e descomente o restante do método:
        return ResponseEntity.status(405).body("Cadastro desativado na demonstração pública do portfólio por motivos de privacidade/LGPD.");
        
        /*
        User user = User.builder()
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .build();
        User saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
        */
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Se desejar reativar o login de usuários no seu fork, comente a linha abaixo e descomente o restante do método:
        return ResponseEntity.status(405).body("Login desativado na demonstração pública do portfólio.");

        /*
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha()));

        String token = jwtService.generateToken(loginRequest.getEmail());
        return ResponseEntity.ok(token);
        */
    }
}
