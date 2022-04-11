package com.example.demo.quotes.repository;

import com.example.demo.quotes.model.CategoryType;
import com.example.demo.quotes.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    Quote findByDescription(String description);

    @Query(
            value = "SELECT * FROM qoute u WHERE u.category_type = ?1 AND approved = 1 ORDER BY rand() limit 1",
            nativeQuery = true)
    Quote findByCategoryTypeAndApproved(CategoryType categoryType);
}
