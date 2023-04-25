package ru.practicum.main_service.users.admin_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.users.admin_service.service.AdminUserService;
import ru.practicum.main_service.users.dto.NewUserRequest;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Validated
@Slf4j
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService service;

    @PostMapping
    public ResponseEntity<Object> create(@Validated @RequestBody NewUserRequest newUserRequest) {
        log.info("AdminUserController: POST request received for new user {}", newUserRequest.getName());
        return new ResponseEntity<>(service.create(newUserRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestParam(required = false) List<Long> ids,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
                                         @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("AdminUserController: GET request received.");
        return new ResponseEntity<>(service.getAll(ids, page, size), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable Long userId) {
        log.info("AdminUserController: DELETE request received for user with id = {}", userId);
        service.remove(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
