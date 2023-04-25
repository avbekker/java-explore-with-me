package ru.practicum.main_service.users.admin_service.service;

import ru.practicum.main_service.users.dto.NewUserRequest;
import ru.practicum.main_service.users.dto.UserDto;

import java.util.List;

public interface AdminUserService {
    UserDto create(NewUserRequest newUserRequest);

    List<UserDto> getAll(List<Long> ids, int page, int size);

    void remove(Long userId);
}
