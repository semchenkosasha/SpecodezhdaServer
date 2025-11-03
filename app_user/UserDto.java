package com.app.app_user;

public record UserDto(
        Long id,
        String username,
        String role,

        String fio,

        String img,

        String workwearSize,
        String workwearSizeName,

        Long positionId,
        String positionName
) {
}
