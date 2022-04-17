package com.example.demo.quotes.service;

import com.example.demo.quotes.dto.QuoteDto;
import com.example.demo.quotes.exception.QuoteException;
import com.example.demo.quotes.model.CategoryType;
import com.example.demo.quotes.model.Quote;
import com.example.demo.quotes.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class QuoteService {
    private final QuoteRepository quoteRepository;

    @Autowired
    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public ResponseEntity<String> addQuote(QuoteDto quoteDto) {
        if (quoteDto.getDescription() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Description cannot be null");
        }
        if (validateQuote(quoteDto)) {
            String unknownPerson = "Unknown";
            Quote quote = new Quote();

            quote.setDescription(quoteDto.getDescription());
            String authorName = quoteDto.getAuthor() == null ? unknownPerson : quoteDto.getAuthor();
            quote.setAuthor(authorName);

            String username = quoteDto.getAddedBy() == null ? unknownPerson : quoteDto.getAddedBy();
            quote.setAddedBy(username);
            quote.setDate(new Date());
            quote.setApproved(false);
            quote.setCategoryType(quoteDto.getCategoryType().getCategory());

            quoteRepository.save(quote);
            return ResponseEntity.status(HttpStatus.CREATED).body("Quote has been created");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quote with this description already exists");
    }

    public ResponseEntity<QuoteDto> getRandomQuoteByCategory(CategoryType categoryType) {
        Quote quote = quoteRepository.findByCategoryTypeAndApproved(categoryType.getCategory());
        if(quote == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(QuoteDto.mapIntoQuoteDto(quote));
    }

    public ResponseEntity<String> approveQuote(long id) {
        Optional<Quote> findQuote = quoteRepository.findById(id);
        if(findQuote.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quote not found");
        }
        Quote quote = findQuote.get();
        quote.setApproved(true);
        quoteRepository.save(quote);
        return ResponseEntity.status(HttpStatus.OK).body("Quote has been updated");
    }

    public int getNumberOfQuotesByCategory(CategoryType categoryType) {
        return quoteRepository.countByCategoryType(categoryType.getCategory());
    }

    private boolean validateQuote(QuoteDto quoteDto) {
        Quote findQuiteByDescription = quoteRepository.findByDescription(quoteDto.getDescription());
        return findQuiteByDescription == null;
    }

    public ResponseEntity<Quote> getUnApprovedQuoteFromDatabase() {
        Quote quote = quoteRepository.findFirstByApproved(false);

        if(quote == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(quote);
    }
}
