package com.example.demo.quotes.controller;

import com.example.demo.quotes.dto.QuoteDto;
import com.example.demo.quotes.model.CategoryType;
import com.example.demo.quotes.model.Quote;
import com.example.demo.quotes.service.QuoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/quote")
public class QuoteController {
    private final QuoteService quoteService;

    @Autowired
    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @Operation(summary = "Add new quote to approve", description = "This endpoint allows to add new quote to the service" +
            "but when it is added, it will remain as 'Awaiting for verification' which means that someone will have to " +
            "verify if quote is valid. Also if something goes wrong then it will return http status 400")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new quote in the system"),
            @ApiResponse(responseCode = "400", description = "Bad request, quote already exists in the database")})
    @PostMapping("/")
    public void addQuote(@Parameter @RequestBody QuoteDto quoteDto) {
        quoteService.addQuote(quoteDto);
    }

    @Operation(summary = "Approve quote", description = "This endpoints allows to approve quote which was added before")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful approved"),
            @ApiResponse(responseCode = "404", description = "Quote not found")})
    @PostMapping("/approve/{id}")
    public ResponseEntity<String> approveQuote(@PathVariable long id) {
        return quoteService.approveQuote(id);
    }

    @Operation(summary = "Get random quote by category type", description = "This endpoint allows to get random quote from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful got quote"),
            @ApiResponse(responseCode = "404", description = "Couldn't find any quotes by this category")})
    @GetMapping("/quote/{categoryType}")
    public ResponseEntity<QuoteDto> randomQuote(@PathVariable CategoryType categoryType) {
        return quoteService.getRandomQuoteByCategory(categoryType);
    }

    @Operation(summary = "Count quotes by category type", description = "This endpoint counts how many quotes are added into database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned number of quotes")})
    @GetMapping("/count/{categoryType}")
    public int countQuotes(@PathVariable CategoryType categoryType) {
        return quoteService.getNumberOfQuotesByCategory(categoryType);
    }

    @Operation(summary = "Get unapproved quote", description = "This endpoint returns first unapproved quote from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful got unapproved quote"),
            @ApiResponse(responseCode = "404", description = "There is unapproved quotes")})
    @GetMapping("/unapproved")
    public ResponseEntity<Quote> getUnapprovedQuote() {
        return quoteService.getUnApprovedQuoteFromDatabase();
    }
}
