package ru.practicum.main_service.comments.services.private_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.main_service.comments.services.private_service.service.CommentPrivateService;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comment")
public class CommentPrivateController {
    private final CommentPrivateService service;


}
