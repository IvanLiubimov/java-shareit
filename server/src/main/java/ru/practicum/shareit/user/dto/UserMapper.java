package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper {

    public static UserDto toUserDto(User user) {
        if (user == null) return null;
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserDto userDto) {
        if (userDto == null) return null;
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public static void updateUserFromDto(UserDto newUserDto, User oldUser) {
        if (newUserDto.getEmail() != null && !newUserDto.getEmail().isBlank()) {
            oldUser.setEmail(newUserDto.getEmail());
        }
        if (newUserDto.getName() != null && !newUserDto.getName().isBlank()) {
            oldUser.setName(newUserDto.getName());
        }
    }
}
