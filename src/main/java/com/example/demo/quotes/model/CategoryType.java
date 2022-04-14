package com.example.demo.quotes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CategoryType {
    TECHNOLOGY("TECHNOLOGY"),
    LIFE("LIFE");

    @Getter
    private final String category;
}
