package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private ItemRequest request;

    public void updateFrom(Item newItem) {
        if (newItem.getName() != null && !newItem.getName().isBlank()) {
            this.name = newItem.getName();
        }
        if (newItem.getDescription() != null && !newItem.getDescription().isBlank()) {
            this.description = newItem.getDescription();
        }
        if (newItem.getAvailable() != null) {
            this.available = newItem.getAvailable();
        }
    }
}
