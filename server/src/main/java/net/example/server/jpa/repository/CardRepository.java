package net.example.server.jpa.repository;

import net.example.server.jpa.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("select c from Card c " +
            "left join fetch c.cardHolder " +
            "where c.cardNumber = :cardNumber")
    Card findCardWithCardHolderByCardNumber(@Param("cardNumber") String cardNumber);
}
