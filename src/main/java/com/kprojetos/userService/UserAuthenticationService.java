package com.kprojetos.userService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService {
    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    public UserAuthenticationService(
            UserService userService,
            TokenService tokenService
    ) {
        this.userService = userService;
        this.tokenService = tokenService;
    }


    public Authentication authenticateUserByLoginPassword(
            UserAuthenticatedDTO dto,
            AuthenticationManager authenticationManager
    ) {
        return authenticationManager.authenticate(
                criarTokenAutenticacao(dto)
        );
    }


    private UsernamePasswordAuthenticationToken criarTokenAutenticacao(
            UserAuthenticatedDTO dto
    ) {
        return new UsernamePasswordAuthenticationToken(
                dto.email(),
                dto.password()
        );
    }


    public Authentication authenticateByToken(String token) {
        String email = tokenService.getSubject(token);
        UserEntity userEntity = (UserEntity) userService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(
                userEntity,
                null,
                userEntity.getAuthorities()
        );
    }
}
