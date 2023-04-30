package ru.practicum.main_service.users.mapper;

import ru.practicum.main_service.users.dto.NewUserRequest;
import ru.practicum.main_service.users.dto.UserDto;
import ru.practicum.main_service.users.dto.UserShortDto;
import ru.practicum.main_service.users.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    private UserMapper() {
    }

    public static User toUser(NewUserRequest newUserRequest) {
        return User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .build();
    }

    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static List<UserDto> toUserDtoList(List<User> users) {
        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}
