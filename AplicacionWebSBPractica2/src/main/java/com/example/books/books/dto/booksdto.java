package com.example.books.books.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class booksdto {
    private long id;
    private String title;
    private String author;
}
