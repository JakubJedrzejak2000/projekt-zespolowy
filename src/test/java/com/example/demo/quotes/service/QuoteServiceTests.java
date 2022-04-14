package com.example.demo.quotes.service;

import com.example.demo.quotes.dto.QuoteDto;
import com.example.demo.quotes.exception.QuoteException;
import com.example.demo.quotes.model.CategoryType;
import com.example.demo.quotes.model.Quote;
import com.example.demo.quotes.repository.QuoteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;

class QuoteServiceTests {
    private static QuoteService quoteService;
    private static QuoteRepository quoteRepository;
    @BeforeEach
    void setup() {
        quoteRepository = Mockito.mock(QuoteRepository.class);
        quoteService = new QuoteService(quoteRepository);
    }

    @Test
    void verifyAddQuoteWithEmptyDescriptionThrowsQuoteException() {
        QuoteDto quoteDto = new QuoteDto();
        quoteDto.setDescription(null);
        Assertions.assertThrows(QuoteException.class, () -> quoteService.addQuote(quoteDto));
    }

    @Test
    void verifyAddQuoteWithDuplicatedDescriptionThrowsQuoteException() {
        QuoteDto quoteDto = new QuoteDto();
        quoteDto.setDescription("Some description");
        Mockito.when(quoteRepository.findByDescription(Mockito.any(String.class))).thenReturn(new Quote());
        Assertions.assertThrows(QuoteException.class, () -> quoteService.addQuote(quoteDto));
    }

    @Test
    void verifyAddQuoteThrowsQuoteException() {
        QuoteDto quoteDto = new QuoteDto();
        quoteDto.setDescription("Some description");
        Mockito.when(quoteRepository.findByDescription(Mockito.any(String.class))).thenReturn(new Quote());

        Quote quote = new Quote();
        Mockito.when(quoteRepository.save(Mockito.any(Quote.class))).thenReturn(quote);
        Assertions.assertThrows(QuoteException.class, () -> quoteService.addQuote(quoteDto));
    }

    @Test
    void verifyAddQuoteAddsDataToDatabaseSuccessfully() {
        QuoteDto quoteDto = new QuoteDto();
        quoteDto.setCategoryType(CategoryType.LIFE);
        quoteDto.setDescription("Some description");
        Mockito.when(quoteRepository.findByDescription(Mockito.any(String.class))).thenReturn(null);
        Quote quote = new Quote();
        quote.setId(10L);
        Mockito.when(quoteRepository.save(Mockito.any(Quote.class))).thenReturn(quote);
        Assertions.assertDoesNotThrow(() -> quoteService.addQuote(quoteDto));
    }

    @Test
    void verifyApproveQuoteThrowsNullPointerException() {
        Mockito.when(quoteRepository.findById(Mockito.any(Long.class))).thenReturn(null);
        Assertions.assertThrows(NullPointerException.class, () -> quoteService.approveQuote(1));
    }

    @Test
    void verifyApproveQuoteThrowsQuoteException() {
        Mockito.when(quoteRepository.findById(Mockito.any(Long.class))).thenReturn(java.util.Optional.of(new Quote()));

        Quote quote = new Quote();
        quote.setId(0L);
        Mockito.when(quoteRepository.save(Mockito.any(Quote.class))).thenReturn(quote);
        Assertions.assertThrows(QuoteException.class, () -> quoteService.approveQuote(1));
    }

    @Test
    void verifyGetRandomQuoteByCategoryThrowsNullPointException() {
        Mockito.when(quoteRepository.findByCategoryTypeAndApproved(Mockito.any(String.class))).thenReturn(null);
        Assertions.assertThrows(NullPointerException.class, () -> quoteService.getRandomQuoteByCategory(CategoryType.LIFE));
    }
    @Test
    void verifyGetRandomQuoteByCategoryReturnsDtoObject() {
        Quote quote = new Quote();
        quote.setCategoryType(CategoryType.LIFE.getCategory());
        quote.setAuthor("fgdfds");
        quote.setAddedBy("fgddffds");
        quote.setDate(new Date());
        Mockito.when(quoteRepository.findByCategoryTypeAndApproved(Mockito.any(String.class))).thenReturn(java.util.Optional.of(quote));
        Assertions.assertDoesNotThrow(() -> quoteService.getRandomQuoteByCategory(CategoryType.LIFE));
    }

    @Test
    void verifyGetNumberOfQuotesByCategoryReturnsValue(){
        Mockito.when(quoteRepository.countByCategoryType(Mockito.any(String.class))).thenReturn(10);
        Assertions.assertEquals(10, quoteService.getNumberOfQuotesByCategory(CategoryType.LIFE));
    }

    @Test
    void verifyGetUnApprovedQuoteFromDatabase(){
        Mockito.when(quoteRepository.findFirstByApproved(Mockito.any(Boolean.class))).thenReturn(new Quote());
        Assertions.assertNotNull(quoteService.getUnApprovedQuoteFromDatabase());
    }


}
