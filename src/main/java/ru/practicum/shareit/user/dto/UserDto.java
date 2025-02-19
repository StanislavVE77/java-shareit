package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.ValidatorGroups;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(groups = {ValidatorGroups.Create.class},
            message = "Имя/Логин не может быть пустым")
    private String name;

    @Email(groups = {ValidatorGroups.Create.class, ValidatorGroups.Update.class},
            message = "Электронная почта не соответствует формату")
    @NotBlank(groups = {ValidatorGroups.Create.class},
            message = "Электронная почта не может быть пустой")
    private String email;

}