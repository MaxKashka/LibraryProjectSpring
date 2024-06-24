package controllers;

import dto.LoginDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import repositories.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@RestController
public class LoginController {
    @Value("${jwt.token}")
    private String key;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public LoginController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        return userRepository.findByUsername(loginDTO.getUsername())
                .map(user -> {
                    if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                        long millis = System.currentTimeMillis();
                        String token = Jwts.builder()
                                .setIssuedAt(new Date(millis))
                                .setExpiration(new Date(millis + 3 * 60 * 1000))
                                .claim("id", user.getUserId())
                                .claim("role", user.getRole())
                                .signWith(SignatureAlgorithm.HS256, key.getBytes(StandardCharsets.UTF_8))
                                .compact();
                        return new ResponseEntity<>(token, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("Wrong login or password!", HttpStatus.UNAUTHORIZED);
                    }
                }).orElse(new ResponseEntity<>("User not found!", HttpStatus.UNAUTHORIZED));
    }
}



