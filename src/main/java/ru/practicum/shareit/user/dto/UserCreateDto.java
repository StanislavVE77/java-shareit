package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {

    @NotBlank(message = "Имя/Логин не может быть пустым")
    private String name;

    @Email(message = "Электронная почта не соответствует формату")
    @NotBlank(message = "Электронная почта не может быть пустой")
    private String email;

}