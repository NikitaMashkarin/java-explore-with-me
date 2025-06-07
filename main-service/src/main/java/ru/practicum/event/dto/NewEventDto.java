package ru.practicum.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.practicum.location.dto.LocationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000, message = "Название категории должно быть от 20 до 2000 символов")
    private String annotation;

    private Long category;

    @NotBlank
    @Size(min = 20, max = 7000, message = "Название категории должно быть от 20 до 7000 символов")
    private String description;

    private String eventDate;

    private LocationDto location;

    private boolean paid;

    private Long participantLimit;

    private boolean requestModeration;

    private String title;
}
