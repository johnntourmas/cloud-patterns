package com.unipi.cloudpatterns.service;

import com.unipi.cloudpatterns.model.Book;
import com.unipi.cloudpatterns.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BookService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookService.class);
    private final BookRepository bookRepository;
    private final AtomicInteger retryCount = new AtomicInteger(0);

    @Autowired
    public BookService (BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        final int maxAttempts = 5;
        final long delay = 2000; // 2 seconds
        List<Book> books = null;

        for (int i = 0; i < maxAttempts; i++) {
            try {
                // Retry pattern is used here. If the operation fails, it will be retried up to maxAttempts times.
                LOGGER.info("Executing getAllBooks method, attempt number: {}", i + 1);
                // throw new RuntimeException("");
                books = bookRepository.findAll();
                LOGGER.info("Successfully retrieved all books");
                break;
            } catch (Exception e) {
                LOGGER.error("Attempt {} failed with exception: {}", i + 1, e.getMessage());
                if (i == maxAttempts - 1) {
                    // Circuit Breaker pattern is used here.
                    // If all attempts fail, the circuit is broken and the fallback method is called.
                    LOGGER.error("All attempts failed, breaking the circuit");
                    return fallbackGetAllBooks(e);
                }
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Operation interrupted", ie);
                }
            }
        }

        return books;
    }

    private List<Book> fallbackGetAllBooks(Throwable t) {
        LOGGER.error("Fallback in getAllBooks method due to {}, after {} attempts", t.getMessage(), retryCount.get());
        // Return a fallback response
        return List.of();
    }
}
