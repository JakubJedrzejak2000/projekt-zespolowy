package com.example.demo.quotes.repository;

import com.example.demo.quotes.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    Quote findByDescription(String description);

    @Query(
            value = "SELECT * FROM quote u WHERE u.category_type = ?1 AND approved = false ORDER BY RANDOM() LIMIT 1;",
            nativeQuery = true)
    Optional<Quote> findByCategoryTypeAndApproved(String categoryType);
    Quote findFirstByApproved(boolean approved);
    int countByCategoryType(String categoryType);
}
