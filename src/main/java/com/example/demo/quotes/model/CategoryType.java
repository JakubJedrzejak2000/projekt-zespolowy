package com.example.demo.quotes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CategoryType {
    TECHNOLOGY("technology"),
    LIFE("life");

    @Getter
    private final String category;
}
