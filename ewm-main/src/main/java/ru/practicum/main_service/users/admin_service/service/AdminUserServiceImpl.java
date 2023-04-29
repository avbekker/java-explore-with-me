package ru.practicum.main_service.users.admin_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.users.dto.NewUserRequest;
import ru.practicum.main_service.users.dto.UserDto;
import ru.practicum.main_service.users.model.User;
import ru.practicum.main_service.users.repository.UserRepository;

import java.util.List;

import static ru.practicum.main_service.users.mapper.UserMapper.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository repository;

    @Transactional
    @Override
    public UserDto create(NewUserRequest newUserRequest) {
        User user = toUser(newUserRequest);
        user = repository.save(user);
        UserDto userDto = toUserDto(user);
        log.info("AdminUserServiceImpl: new user {} {} created.", userDto.getId(), userDto.getName());
        return userDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAll(List<Long> ids, int page, int size) {
        List<User> users = repository.findByIdIn(ids, PageRequest.of(page, size)).getContent();
        log.info("AdminUserServiceImpl: GET request received for {}.", ids);
        return toUserDtoList(users);
    }

    @Transactional
    @Override
    public void remove(Long userId) {
        repository.deleteById(userId);
        log.info("AdminUserServiceImpl: user with id = {} deleted.", userId);
    }
}
