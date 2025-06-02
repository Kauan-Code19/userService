package com.kprojetos.userService;

public record UserDTO(
    String email,
    String password,
    UserProfile profile
) {
}
