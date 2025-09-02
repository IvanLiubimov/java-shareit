package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;
import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {


    @Query("SELECT r FROM ItemRequest r " +
            "WHERE r.requestor.id = :userId " +
            "ORDER BY r.created DESC")
    Collection<ItemRequest> findAllRequestsByUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM ItemRequest r " +
            "WHERE r.requestor.id <> :requestor_id " +
            "ORDER BY r.created DESC")
    List<ItemRequest> findAll(@Param("requestor_id") Long userId);
}
