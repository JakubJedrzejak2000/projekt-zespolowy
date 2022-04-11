package com.example.demo.quotes.dto;

import com.example.demo.quotes.model.Quote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuoteDto {
    private String description;
    private String author;
    private String addedBy;
    private Date date;

    public static QuoteDto mapIntoQuoteDto(Quote quote){
        QuoteDto quoteDto = new QuoteDto();
        quoteDto.setDescription(quote.getDescription());
        quoteDto.setAuthor(quote.getAuthor());
        quoteDto.setAddedBy(quote.getAddedBy());
        quoteDto.setDate(quote.getDate());

        return quoteDto;
    }
}
