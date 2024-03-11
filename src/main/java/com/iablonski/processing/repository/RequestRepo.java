package com.iablonski.processing.repository;

import com.iablonski.processing.domain.Request;
import com.iablonski.processing.domain.StatusEnum;
import com.iablonski.processing.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequestRepo extends JpaRepository<Request, UUID> {
    Optional<Request> findRequestByIdAndStatus(UUID id, StatusEnum status);
    @Query("select r from Request r where :username is null or :username='' or lower(r.user.username) " +
            "like lower(concat('%', :username, '%')) and r.status = 'SENT'")
    Page<Request> findRequestsByOperatorParams(@Param("username") String username, Pageable pageable);

    @Query("select r from Request r where (r.status = 'SENT' or r.status = 'ACCEPTED' or r.status = 'REJECTED') " +
            "and (:username is null or :username = '' or lower(r.user.username) " +
            "like lower(concat('%', :username, '%'))) order by r.createdAt desc")
    Page<Request> findRequestsByAdminParams(@Param("username") String username, Pageable pageable);

    Page<Request> findAllRequestsByStatus(StatusEnum statusEnum, Pageable pageable);
    Page<Request> findAllByUser(User user, Pageable pageable);
}