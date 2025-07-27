package com.alura.literalura.services;


import com.alura.literalura.apiclient.ApiClient;
import com.alura.literalura.dtos.GutendexApiResponse;
import com.alura.literalura.entities.AluraAuthor;
import com.alura.literalura.entities.AluraBook;
import com.alura.literalura.repositories.AuthorRepo;
import com.alura.literalura.repositories.BookRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class BookService
{
    private static final String API_URL = "https://gutendex.com/books/";
    private final BookRepo bookRepo;
    private final ApiClient apiClient;
    private final AuthorRepo authorRepo;


    public BookService(BookRepo bookRepo, ApiClient apiClient, AuthorRepo authorRepo) {
        this.bookRepo = bookRepo;
        this.apiClient = apiClient;
        this.authorRepo = authorRepo;

    }


    private <T> T convertData(String json, Class<T> classType) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, classType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir JSON: " + e.getMessage());
        }
    }
    public AluraBook fetchAndSaveBook(String bookTitle) {
        String json = apiClient.getData(API_URL + "?search=" + bookTitle.replace(" ", "+"));
        GutendexApiResponse response = convertData(json, GutendexApiResponse.class);

        GutendexApiResponse.Book apiBook  = response
                .results()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No book found with title: " + bookTitle));

        AluraBook book = mapToAluraBook(apiBook);


        if (book.getAuthor() != null) {

            AluraAuthor existingAuthor = authorRepo.findAuthorByName(book.getAuthor().getName());

            if (existingAuthor == null) {

                existingAuthor = authorRepo.save(book.getAuthor());
            }


            book.setAuthor(existingAuthor);
        }

        return bookRepo.save(book);
    }

    @Transactional
    public List<AluraBook> findBooksByTitle(String title) {
        return bookRepo.findByTitleContainingIgnoreCase(title.trim()); // Assuming you have this method in your repository
    }

    private AluraBook mapToAluraBook(GutendexApiResponse.Book apiBook) {
        AluraBook book = new AluraBook();
        book.setTitle(apiBook.title());
        book.setLanguage(apiBook.getPrimaryLanguage());
        book.setNumberOfDownloads(apiBook.downloadCount());

        if (!apiBook.authors().isEmpty()) {
            GutendexApiResponse.Author apiAuthor = apiBook.authors().getFirst();
            AluraAuthor author = new AluraAuthor();
            author.setName(apiAuthor.name());
            author.setBirthYear(apiAuthor.birthYear());
            author.setDeathYear(apiAuthor.deathYear());
            book.setAuthor(author);
        }

        return book;
    }


    public List<AluraBook> getAllBooks() {
        return bookRepo.findAll();
    }

    public List<AluraBook> getBooksByLanguage(String languageCode) {

        return bookRepo.findBooksByLanguage(languageCode.toLowerCase());
    }


    public void displayAuthorDetails(AluraAuthor author) {
        System.out.println("\n----------------------------------");
        System.out.println("Autor: " + author.getName());
        System.out.println("Fecha de nacimiento: " + author.getBirthYear());
        System.out.println("Fecha de fallecimiento: " + author.getDeathYear());

        System.out.println("Libros:");
        if (author.getBooks() != null && !author.getBooks().isEmpty()) {
            author.getBooks().forEach(book ->
                    System.out.println("  - " + book.getTitle())
            );
        } else {
            System.out.println("  (No hay libros registrados)");
        }
        System.out.println("----------------------------------");
    }

}
