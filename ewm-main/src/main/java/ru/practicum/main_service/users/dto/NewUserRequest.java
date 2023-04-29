package ru.practicum.main_service.users.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @NotBlank
    @Size(max = 50)
    private String name;
    @Email
    @NotBlank
    @Size(max = 50)
    private String email;
}
