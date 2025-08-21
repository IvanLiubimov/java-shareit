package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.Instant;
import java.util.Collection;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :CurrentUserId " +
            "AND (:state = 'ALL' " +
            "OR (:state = 'CURRENT' AND b.start <= CURRENT_TIMESTAMP AND b.end >= CURRENT_TIMESTAMP) " +
            "OR (:state = 'PAST' AND b.end < CURRENT_TIMESTAMP) " +
            "OR (:state = 'FUTURE' AND b.start > CURRENT_TIMESTAMP) " +
            "OR (:state = 'WAITING' AND b.status = ru.practicum.shareit.booking.model.Status.WAITING) " +
            "OR (:state = 'REJECTED' AND b.status = ru.practicum.shareit.booking.model.Status.REJECTED)) " +
            "ORDER BY b.start DESC")
    Collection<Booking> findAll(@Param("state") String state,
                                @Param("CurrentUserId") Long userId);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item i " +
            "WHERE i.owner.id = :ownerId " +
            "AND (:state = 'ALL' " +
            "OR (:state = 'CURRENT' AND b.start <= CURRENT_TIMESTAMP AND b.end >= CURRENT_TIMESTAMP) " +
            "OR (:state = 'PAST' AND b.end < CURRENT_TIMESTAMP) " +
            "OR (:state = 'FUTURE' AND b.start > CURRENT_TIMESTAMP) " +
            "OR (:state = 'WAITING' AND b.status = 'WAITING') " +
            "OR (:state = 'REJECTED' AND b.status = 'REJECTED')) " +
            "ORDER BY b.start DESC")
    Collection<Booking> getBookingsForOwnerItems(@Param("state") String state,
                                              @Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId")
    Collection<Booking> findAllByItemId(@Param("itemId") Long itemId);

    boolean existsByItemIdAndBookerIdAndEndBefore(Long itemId, Long bookerId, Instant endBefore);

    @Query("""
    SELECT b FROM Booking b
    WHERE b.item.id IN :itemIds
      AND b.start < CURRENT_TIMESTAMP
    ORDER BY b.start DESC
""")
    Collection<Booking> findLastBookings(@Param("itemIds") Collection<Long> itemIds);

    @Query("""
    SELECT b FROM Booking b
    WHERE b.item.id IN :itemIds
      AND b.start > CURRENT_TIMESTAMP
    ORDER BY b.start ASC
""")
    Collection<Booking> findNextBookings(@Param("itemIds") Collection<Long> itemIds);
}

