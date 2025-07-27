package com.alura.literalura.app;

import com.alura.literalura.entities.AluraAuthor;
import com.alura.literalura.entities.AluraBook;
import com.alura.literalura.services.AuthorService;
import com.alura.literalura.services.BookService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;


@Component
public class MenuManager
{
    private final Scanner sc = new Scanner(System.in);
    private final BookService bookService;
    private final AuthorService authorService;

    public MenuManager(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    public void mostrarMenu() {
        var opcion = -1;
        var menu = """
            ---------------------------------------------
                              LITERALURA
            ---------------------------------------------
            1- buscar libro por título
            2- listar libros registrados
            3- listar autores registrados
            4- listar autores vivos en un determinado año
            5- listar libros por idioma
            6- Top 10 libros más descargados
            0- salir
            ----------------------------------------------
            
            Elija una opción:
            """;

        while (opcion != 0) {
            System.out.println(menu);
            try {
                opcion = Integer.parseInt(sc.nextLine());
                switch (opcion) {
                    case 1 -> searchBookByTitle();
                    case 2 -> listAllBooks();
                    case 3 -> listAllAuthors();
                    case 4 -> searchAliveAuthorsByYear();
                    case 5 -> searchBooksByLanguage();
                    case 6 -> showTop10Downloads();
                    case 0 -> System.out.println("Hasta pronto.");
                    default -> System.out.println("Opción inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida: " + e.getMessage());
            }
        }
    }

    private void searchBookByTitle() {
        System.out.println("Escriba el nombre del libro:");
        String title = sc.nextLine();
        try {
            List<AluraBook> books = bookService.findBooksByTitle(title);

            if (books.isEmpty()) {
                System.out.println("No se encontró el libro. ¿Desea buscarlo en la API? (s/n)");
                String choice = sc.nextLine();
                if (choice.equalsIgnoreCase("s")) {
                    AluraBook newBook = bookService.fetchAndSaveBook(title);
                    displayBookDetails(newBook);
                }
            } else if (books.size() == 1) {
                displayBookDetails(books.getFirst());
            } else {
                System.out.println("Varios libros encontrados:");
                books.forEach(b -> System.out.println("- " + b.getTitle()));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void displayBookDetails(AluraBook book) {
        System.out.println("--------- LIBRO ---------");
        System.out.println("Título: " + book.getTitle());
        System.out.println("Autor: " + (book.getAuthor().getName()));
        System.out.println("Idioma: " + book.getLanguage());
        System.out.println("Número de descargas: " + book.getNumberOfDownloads());
        System.out.println("-------------------------");
    }

    private void listAllBooks() {
        try {
            List<AluraBook> books = bookService.getAllBooks();
            if (books.isEmpty()) {
                System.out.println("No hay libros registrados.");
            } else {
                books.forEach(this::displayBookDetails
                );
            }
        } catch (Exception e) {
            System.out.println("Error al listar libros: " + e.getMessage());
        }
    }

    private void listAllAuthors() {
        try {
            List<AluraAuthor> authors = authorService.getAllAuthors();
            if (authors.isEmpty()) {
                System.out.println("No hay autores registrados.");
            } else {
                authors.forEach(bookService::displayAuthorDetails);
            }
        } catch (Exception e) {
            System.out.println("Error al listar autores: " + e.getMessage());
        }
    }

    private void searchAliveAuthorsByYear() {
        System.out.println("Ingrese el año:");
        try {
            int year = Integer.parseInt(sc.nextLine());
            List<AluraAuthor> authors = authorService.getAliveAuthorsInYear(year);

            if (authors.isEmpty()) {
                System.out.println("No se encontraron autores vivos en el año " + year);
            } else {
                System.out.println("Autores vivos en " + year + ":");
                authors.forEach(bookService::displayAuthorDetails);
            }
        } catch (NumberFormatException e) {
            System.out.println("Año inválido. Por favor ingrese un número.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void searchBooksByLanguage() {
        System.out.println("""
            Idiomas disponibles:
            es -> Español
            en -> Inglés
            fr -> Francés
            pt -> Portugués
            """);
        System.out.println("Ingrese el código del idioma:");
        String languageCode = sc.nextLine().toLowerCase();
        try {
            List<AluraBook> books = bookService.getBooksByLanguage(languageCode);
            if (books.isEmpty()) {
                System.out.println("No hay libros en el idioma seleccionado.");
            } else {
                books.forEach(this::displayBookDetails);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showTop10Downloads() {
        try {
            List<AluraBook> books = bookService.getTop10DownloadedBooks();
            System.out.println("\n=== TOP 10 LIBROS MAS DESCARRGADOS ===");
            books.forEach(book -> System.out.printf(
                    "- %s (%s downloads)\n",
                    book.getTitle(),
                    book.getNumberOfDownloads()
            ));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
