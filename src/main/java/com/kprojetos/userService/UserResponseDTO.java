package com.kprojetos.userService;

public record UserResponseDTO(
    Long id,
    String email,
    UserProfile profile
) {
}
