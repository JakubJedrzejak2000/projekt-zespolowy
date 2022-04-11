package com.example.demo.quotes.service;

import com.example.demo.quotes.dto.QuoteDto;
import com.example.demo.quotes.model.CategoryType;
import com.example.demo.quotes.model.Quote;
import com.example.demo.quotes.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class QuoteService {
    private final QuoteRepository quoteRepository;

    @Autowired
    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public void addQuote(Quote quote) {
        if (quote.getDescription() == null)
            return;

        if (validateQuote(quote)) {
            if (quote.getAuthor() == null) {
                quote.setAuthor("Unknown");
            }
            if (quote.getAddedBy() == null) {
                quote.setAddedBy("Unknown");
            }

            quote.setDate(new Date());
            quote.setApproved(false);

            quoteRepository.save(quote);
        }
    }

    public QuoteDto getRandomQuoteByCategory(CategoryType categoryType) {
        Quote quote = quoteRepository.findByCategoryTypeAndApproved(categoryType);
        return QuoteDto.mapIntoQuoteDto(quote);
    }

    private boolean validateQuote(Quote quote) {
        Quote findQuiteByDescription = quoteRepository.findByDescription(quote.getDescription());
        return findQuiteByDescription != null;
    }
}
