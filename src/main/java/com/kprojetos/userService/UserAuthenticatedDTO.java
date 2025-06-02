package com.kprojetos.userService;

public record UserAuthenticatedDTO(
    String email,
    String password
) {
}
