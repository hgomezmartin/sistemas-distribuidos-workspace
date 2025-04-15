package com.example.books.books.repository;

import com.example.books.books.model.books;
import org.springframework.data.jpa.repository.JpaRepository;

public interface booksrepo extends JpaRepository<books,Long> {
}
