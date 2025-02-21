package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.ValidatorGroups;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

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

    //private Long owner;

    private Long requestId;

}
