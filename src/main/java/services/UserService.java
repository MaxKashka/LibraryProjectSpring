package services;

import dto.UserDTO;
import dto.DTOConverter;
import networklist1.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(DTOConverter::convertToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(DTOConverter::convertToUserDTO)
                .orElse(null);
    }

    public UserDTO createUser(UserDTO userDTO) {
        UserEntity user = DTOConverter.convertToUserEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        UserEntity savedUser = userRepository.save(user);
        return DTOConverter.convertToUserDTO(savedUser);
    }

    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);

        if (userEntityOptional.isPresent()) {
            UserEntity user = userEntityOptional.get();
            user.setEmail(userDTO.getEmail());
            user.setName(userDTO.getName());
            user.setUsername(userDTO.getUsername());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            UserEntity updatedUser = userRepository.save(user);
            return DTOConverter.convertToUserDTO(updatedUser);
        } else {
            return null;
        }
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
