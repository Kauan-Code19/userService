package com.kprojetos.userService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Usuário não encontrado com o e-mail: "
                                + username
                ));
    }

    @Transactional
    public UserResponseDTO createUser(UserDTO dto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(dto.email());
        userEntity.setPassword(passwordEncoder.encode(dto.password()));
        userEntity.setProfile(dto.profile());

        UserEntity savedUser = userRepository.save(userEntity);

        return new UserResponseDTO(savedUser.getId(), savedUser.getEmail(), savedUser.getProfile());
    }
}
