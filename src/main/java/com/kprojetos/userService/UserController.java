package com.kprojetos.userService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserAuthenticationService userAuthenticationService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Autowired
    public UserController(
            UserService userService,
            UserAuthenticationService userAuthenticationService,
            AuthenticationManager authenticationManager,
            TokenService tokenService
    ) {
        this.userService = userService;
        this.userAuthenticationService = userAuthenticationService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }


    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticatedResponseDTO> authenticateUser(@RequestBody UserAuthenticatedDTO dto) {
        Authentication userResponse = userAuthenticationService
                .authenticateUserByLoginPassword(dto, authenticationManager);

        String token =  tokenService.generateUserToken(
                (UserEntity) userResponse.getPrincipal()
        );

        return ResponseEntity.ok(new UserAuthenticatedResponseDTO(token));
    }


    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserDTO dto) {
        UserResponseDTO userResponse = userService.createUser(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userResponse.id())
                .toUri();

        return ResponseEntity.created(uri).body(userResponse);
    }
}
