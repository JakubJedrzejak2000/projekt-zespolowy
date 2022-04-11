package com.example.demo.quotes.controller;

import com.example.demo.quotes.dto.QuoteDto;
import com.example.demo.quotes.model.CategoryType;
import com.example.demo.quotes.model.Quote;
import com.example.demo.quotes.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class QuoteController {
    private final QuoteService quoteService;

    @Autowired
    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping("/quote")
    public void addQuote(@RequestBody Quote quote){
        quoteService.addQuote(quote);
    }

    @GetMapping("/quote/{categoryType}")
    public QuoteDto randomQuote(@PathVariable CategoryType categoryType){
        return quoteService.getRandomQuoteByCategory(categoryType);
    }
}
