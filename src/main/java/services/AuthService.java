package services;

import dto.LoginDTO;
import dto.LoginResponseDTO;
import dto.RegisterDTO;
import dto.RegisterResponseDTO;
import networklist1.AuthEntity;
import networklist1.UserEntity;
import networklist1.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repositories.AuthRepository;
import repositories.UserRepository;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public RegisterResponseDTO register(RegisterDTO dto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(dto.getEmail());
        userEntity.setName(dto.getName());
        userEntity.setUsername(dto.getUsername());
        userEntity.setRole(UserRole.valueOf(dto.getRole())); // Converting String to UserRole
        UserEntity createdUser = userRepository.save(userEntity);

        AuthEntity authEntity = new AuthEntity();
        authEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        authEntity.setUsername(dto.getUsername());
        authEntity.setRole(UserRole.valueOf(dto.getRole())); // Converting String to UserRole
        authEntity.setUser(createdUser);
        authRepository.save(authEntity);

        return new RegisterResponseDTO("User registered successfully");
    }

    public LoginResponseDTO login(LoginDTO dto) {
        return authRepository.findByUsername(dto.getUsername())
                .filter(authEntity -> passwordEncoder.matches(dto.getPassword(), authEntity.getPassword()))
                .map(authEntity -> new LoginResponseDTO(jwtService.generateToken(authEntity)))
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
    }
}


