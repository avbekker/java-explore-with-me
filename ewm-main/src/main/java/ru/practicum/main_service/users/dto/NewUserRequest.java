package ru.practicum.main_service.users.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
}
