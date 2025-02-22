package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ItemCreateDto {

    private Long id;

    @NotBlank(message = "Наименование не может быть пустым")
    private String name;

    @NotNull(message = "Должно быть описание")
    private String description;

    @NotNull(message = "Должен быть статус")
    private Boolean available;

    private Long requestId;

}