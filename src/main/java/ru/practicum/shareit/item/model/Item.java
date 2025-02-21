package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.ValidatorGroups;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    private Long id;

    @NotBlank(groups = {ValidatorGroups.Create.class},
            message = "Наименование не может быть пустым")
    private String name;

    @NotNull(groups = {ValidatorGroups.Create.class},
            message = "Должно быть описание")
    private String description;

    @NotNull(groups = {ValidatorGroups.Create.class},
            message = "Должен быть статус")
    private Boolean available;

    private User owner;

    private ItemRequest request;
}
