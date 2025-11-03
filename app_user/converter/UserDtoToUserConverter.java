package com.app.app_user.converter;

import com.app.app_user.AppUser;
import com.app.app_user.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, AppUser> {
    @Override
    public AppUser convert(UserDto source) {
        return new AppUser(
                source.username()
        );
    }
}
