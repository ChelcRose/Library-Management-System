package com.usc.libraryms.repo;

import com.usc.libraryms.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {

    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrCategoryContainingIgnoreCase(
            String t, String a, String c
    );

    @Query("SELECT b.id FROM Book b")
    List<String> findAllBookIds();
}
