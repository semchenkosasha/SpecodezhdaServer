package com.app.app_user.converter;

import com.app.app_user.AppUser;
import com.app.app_user.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<AppUser, UserDto> {

    @Override
    public UserDto convert(AppUser source) {
        return new UserDto(
                source.getId(),
                source.getUsername(),
                source.getRole().name(),

                source.getFio(),

                source.getImg(),

                source.getWorkwearSize().name(),
                source.getWorkwearSize().getName(),

                source.getPositionId(),
                source.getPositionName()
        );
    }
}
