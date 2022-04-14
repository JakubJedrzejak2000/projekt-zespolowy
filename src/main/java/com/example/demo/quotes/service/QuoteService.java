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

    public void addQuote(QuoteDto quoteDto) {
        if (quoteDto.getDescription() == null)
            return;
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
        }
    }

    public QuoteDto getRandomQuoteByCategory(CategoryType categoryType) {
        Quote quote = quoteRepository.findByCategoryTypeAndApproved(categoryType.getCategory());
        return QuoteDto.mapIntoQuoteDto(quote);
    }

    public void approveQuote(long id) {
        Quote quote = quoteRepository.findById(id).orElse(null);

        if(quote != null){
            quote.setApproved(true);

            quoteRepository.save(quote);
        }
    }

    public int getNumberOfQuotesByCategory(CategoryType categoryType){
        return quoteRepository.countByCategoryType(categoryType.getCategory());
    }

    private boolean validateQuote(QuoteDto quoteDto) {
        Quote findQuiteByDescription = quoteRepository.findByDescription(quoteDto.getDescription());
        return findQuiteByDescription == null;
    }
}
