package net.example.server.jpa.repository;

import net.example.server.jpa.entity.CardHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardHolderRepository extends JpaRepository<CardHolder, Long> {

    @Query("select ch FROM CardHolder ch " +
            "WHERE ch.firstName =:firstName " +
            "AND ch.lastName = :lastName")
    CardHolder findByFirstAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
