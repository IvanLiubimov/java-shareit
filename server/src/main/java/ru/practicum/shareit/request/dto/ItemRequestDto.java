package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.practicum.shareit.item.dto.ItemDtoToRequest;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequestDto {
    private long id;
    private String description;

    @JsonProperty("created")
    private LocalDateTime dateTime;

    @JsonProperty("items")
    private List<ItemDtoToRequest> itemsList;
}